package ruby.bamboo.entity.magatama;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;

import ruby.bamboo.BambooUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityFlareEffect extends Entity {
    private int timer = 100;
    private boolean isFirstUpdate = true;
    private boolean isExploded = false;
    private float explodeSize = 0;
    private float alpha;
    private List<EnergeticParticle> particles = new ArrayList<EnergeticParticle>();
    private List<EntityRuneEffect> runes = new ArrayList<EntityRuneEffect>();
    private static final int EXPLODE_SIZE = 25;
    private static final boolean[][][] MATRIX;
    static {
        MATRIX = new boolean[EXPLODE_SIZE * 2][EXPLODE_SIZE * 2][EXPLODE_SIZE * 2];
        for (int i = 0; i < EXPLODE_SIZE * 2; i++) {
            for (int j = 0; j < EXPLODE_SIZE * 2; j++) {
                for (int k = 0; k < EXPLODE_SIZE * 2; k++) {
                    MATRIX[i][j][k] = BambooUtil.isInnerCircleCollision(0, 0, 0, EXPLODE_SIZE - i, EXPLODE_SIZE - j, EXPLODE_SIZE - k, EXPLODE_SIZE);
                }
            }
        }
    }

    public EntityFlareEffect(World par1World) {
        super(par1World);
        this.setSize(1F, 1f);
    }

    @Override
    public void onUpdate() {
        // super.onUpdate();
        if (isFirstUpdate) {
            isFirstUpdate = false;
            if (worldObj.isRemote) {
                EntityRuneEffect rune = new EntityRuneEffect(worldObj);
                rune.setPosition(posX, posY, posZ);
                rune.setRollSpeed(-1);
                rune.setRingsize(12);
                rune.setParentEntity(this);
                rune.setRingColor(0xEE1100);
                runes.add(rune);

                rune = rune.copy();
                rune.setPositionAndRotation(posX - 0.5F, posY + 0.5F, posZ, 90, 0);
                runes.add(rune);

                rune = rune.copy();
                rune.setPositionAndRotation(posX, posY + 0.5F, posZ + 0.5F, 0, 90);
                runes.add(rune);

                rune = rune.copy();
                rune.setPositionAndRotation(posX, posY + 0.25F, posZ, 45, 0);
                rune.setRollSpeed(1);
                runes.add(rune);

                rune = rune.copy();
                rune.setPositionAndRotation(posX, posY + 0.25F, posZ, -45, 0);
                runes.add(rune);

                rune = rune.copy();
                rune.setPositionAndRotation(posX, posY + 0.25F, posZ, 0, 45);
                runes.add(rune);

                rune = rune.copy();
                rune.setPositionAndRotation(posX, posY + 0.25F, posZ, 0, -45);
                runes.add(rune);

                for (Entity e : runes) {
                    worldObj.joinEntityInSurroundings(e);
                }
                for (int i = 0; i < 30; i++) {
                    EnergeticParticle c = new EnergeticParticle();
                    double cposX = posX + (rand.nextInt(40) - 20);
                    double cposY = posY + (rand.nextInt(20) - 10);
                    double cposZ = posZ + (rand.nextInt(40) - 20);
                    c.setTargetPosition(posX, posY, posZ);
                    c.setPosition(cposX, cposY, cposZ);
                    particles.add(c);
                }
            }
        }
        if (0 < --timer) {
            if (timer < 20) {
                explodeSize += explodeSize < 30 ? (explodeSize) + 0.4 : 0;
                this.setSize(explodeSize, explodeSize);
                alpha = timer / 20F;
                this.worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
                if (!worldObj.isRemote && !isExploded) {
                    matrixExplosion(worldObj, (int) posX, (int) posY, (int) posZ, MATRIX);
                    isExploded = true;
                }
            } else {
                if (worldObj.isRemote) {
                    for (EnergeticParticle c : particles) {
                        c.onUpdate(worldObj);
                    }

                    for (EntityRuneEffect rune : runes) {
                        if (timer > 40) {
                            rune.setRollSpeed(rune.getRollSpeed() + (rune.getRollSpeed() * 0.05F));
                        } else if (timer > 35) {
                            rune.posY = rune.posY < this.posY ? rune.posY + 0.3 : rune.posY - 0.3;
                            rune.setRingsize(rune.getRingsize() - (rune.getRingsize() * 0.3F));
                        } else {
                            rune.setDead();
                        }
                    }
                }
            }
        } else {
            particles.clear();
            this.kill();
        }
    }

    private void matrixExplosion(World world, int posX, int posY, int posZ, boolean[][][] matrix) {
        try {
            int matrixX = matrix.length;
            int matrixY = matrix[0].length;
            int matrixZ = matrix[0][0].length;
            int offsetPosX = posX - (matrixX / 2);
            int offsetPosY = posY - (matrixY / 2);
            int offsetPosZ = posZ - (matrixZ / 2);
            for (int i = 0; i < matrixX; i++) {
                for (int j = 0; j < matrixY; j++) {
                    for (int k = 0; k < matrixZ; k++) {
                        if (matrix[i][j][k]) {
                            if (!world.isAirBlock(offsetPosX + i, offsetPosY + j, offsetPosZ + k)) {
                                if (Block.blocksList[world.getBlockId(offsetPosX + i, offsetPosY + j, offsetPosZ + k)].getBlockHardness(world, offsetPosX + i, offsetPosY + j, offsetPosZ + k) != -1) {
                                    world.setBlock(offsetPosX + i, offsetPosY + j, offsetPosZ + k, 0, 0, 2);
                                }
                            }
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            FMLLog.getLogger().log(Level.WARNING, "Illegal matrix");
        }
    }

    public float getExplodeSize() {
        return explodeSize;
    }

    public float getAlpha() {
        return alpha;
    }

    @Override
    public void moveEntity(double par1, double par3, double par5) {
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
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    private class EnergeticParticle {
        private final static float time = 40;
        double posX, posY, posZ;
        double preposX, preposY, preposZ;
        double tposX, tposY, tposZ;
        private int count;

        void setPosition(double posX, double posY, double posZ) {
            this.preposX = this.posX = posX;
            this.preposY = this.posY = posY;
            this.preposZ = this.posZ = posZ;
        }

        void setTargetPosition(double posX, double posY, double posZ) {
            this.tposX = posX;
            this.tposY = posY;
            this.tposZ = posZ;
        }

        public void onUpdate(World world) {
            if (++count < time) {
                world.spawnParticle("fireworksSpark", preposX, preposY, preposZ, 0.0F, 0.0F, 0.0F);
            }
            preposX -= (posX - tposX) / time;
            preposY -= (posY - tposY) / time;
            preposZ -= (posZ - tposZ) / time;
        }
    }
}
