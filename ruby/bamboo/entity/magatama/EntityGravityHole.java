package ruby.bamboo.entity.magatama;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ruby.bamboo.BambooUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityGravityHole extends Entity {
    private boolean isFirstUpdate;
    private List<EntityThunderEffect> effectList;
    private int timer;
    private float scale = 0;
    private static final int EXPLODE_SIZE = 75;
    private static final TreeMap<Float, ArrayList<Position>> matrixMap;
    private Iterator<Entry<Float, ArrayList<Position>>> iterator;
    private int blockX;
    private int blockY;
    private int blockZ;
    private List<Entity> hooks;
    static {
        matrixMap = new TreeMap<Float, ArrayList<Position>>();
        float key;
        for (int i = 0; i < EXPLODE_SIZE * 2; i++) {
            for (int j = 0; j < EXPLODE_SIZE * 2; j++) {
                for (int k = 0; k < EXPLODE_SIZE * 2; k++) {
                    key = BambooUtil.getInnerCircleCollision(0, 0, 0, EXPLODE_SIZE - i, EXPLODE_SIZE - j, EXPLODE_SIZE - k);
                    if (key <= EXPLODE_SIZE) {
                        if (!matrixMap.containsKey(key)) {
                            matrixMap.put(key, new ArrayList<Position>());
                        }
                        matrixMap.get(key).add(new Position(i, j, k));
                    }
                }
            }
        }
    }

    public EntityGravityHole(World par1World) {
        super(par1World);
        this.setSize(20.0F, 20.0F);
        effectList = new ArrayList<EntityThunderEffect>();
        hooks = new LinkedList<Entity>();
        iterator = matrixMap.entrySet().iterator();
        isFirstUpdate = true;
        timer = 1300;
        scale = 0;
    }

    @Override
    public void onUpdate() {
        if (this.isDead) {
            return;
        }
        if (isFirstUpdate) {
            if (worldObj.isRemote) {
                for (int i = 0; i < 10; i++) {
                    EntityThunderEffect tp = new EntityThunderEffect(worldObj);
                    tp.setPosition(this.posX, this.posY, this.posZ);
                    effectList.add(tp);
                    worldObj.joinEntityInSurroundings(tp);
                }
            }
            blockX = (int) Math.floor(posX) - EXPLODE_SIZE;
            blockY = (int) Math.floor(posY) - EXPLODE_SIZE;
            blockZ = (int) Math.floor(posZ) - EXPLODE_SIZE;
            isFirstUpdate = false;
        }
        if (iterator.hasNext() && timer < 100) {
            timer = 100;
        }
        if (iterator.hasNext()) {
            Entry<Float, ArrayList<Position>> entry = iterator.next();
            for (Position pos : entry.getValue()) {
                if (!BambooUtil.isUnbreakBlock(worldObj, blockX + pos.getPosX(), blockY + pos.getPosY(), blockZ + pos.getPosZ())) {
                    setToAirBlock(worldObj, blockX + pos.getPosX(), blockY + pos.getPosY(), blockZ + pos.getPosZ());
                }
            }
        }

        if (worldObj.isRemote) {
            Iterator<Entity> blocksIterator = hooks.iterator();
            while (blocksIterator.hasNext()) {
                Entity entity = blocksIterator.next();
                entity.motionX += (posX - entity.posX) < 0 ? -0.1 : 0.1;
                entity.motionY += ((posY + (this.height / 2)) - entity.posY) < 0 ? -0.15 : 0.15;
                entity.motionZ += (posZ - entity.posZ) < 0 ? -0.1 : 0.1;
                if (getDistanceSqToEntity(entity) < scale * 8) {
                    entity.setDead();
                }
                if (entity.isDead) {
                    blocksIterator.remove();
                }
            }
        }
        if (timer-- > 0) {
            if (timer < 10) {
                scale = scale > 0.5F ? scale -= scale / 10 : 0;
            } else if (timer < 1250) {
                scale += scale < 5 ? 0.01 : 0.002;
                /*
                 * this.setSize(scale,scale); boundingBox.offset(0, -scale, 0);
                 */
            } else {

            }
        } else {
            this.setDead();
        }
    }

    @Override
    public double getDistanceSqToEntity(Entity par1Entity) {
        double d0 = this.posX - par1Entity.posX;
        double d1 = posY + (this.height / 2) - par1Entity.posY;
        double d2 = this.posZ - par1Entity.posZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    @Override
    public void setDead() {
        if (!effectList.isEmpty()) {
            for (EntityThunderEffect e : effectList) {
                e.setDead();
            }
            effectList.clear();
        }
        if (!hooks.isEmpty()) {
            for (Entity entity : hooks) {
                entity.setDead();
            }
            hooks.clear();
        }
        super.setDead();
    }

    private void setToAirBlock(World world, int posX, int posY, int posZ) {
        if (world.isRemote) {
            if (rand.nextFloat() < 0.02) {
                EntityFallingSand entityfallingsand = new EntityFallingSand(world, posX + 0.5F, posY + 0.5F, posZ + 0.5F, world.getBlockId(posX, posY, posZ), world.getBlockMetadata(posX, posY, posZ));
                entityfallingsand.fallTime = 2;
                entityfallingsand.shouldDropItem = false;
                hooks.add(entityfallingsand);
                world.joinEntityInSurroundings(entityfallingsand);
            }
        }
        worldObj.setBlock(posX, posY, posZ, 0, 0, 4);
    }

    public float getScale() {
        return scale;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        setDead();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    }

    @Override
    public void moveEntity(double par1, double par3, double par5) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    private static class Position {
        private final int posX, posY, posZ;

        Position(int posX, int posY, int posZ) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
        }

        public int getPosX() {
            return posX;
        }

        public int getPosY() {
            return posY;
        }

        public int getPosZ() {
            return posZ;
        }
    }
}
