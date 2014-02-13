package ruby.bamboo.entity.magatama;

import java.util.ArrayList;
import java.util.Arrays;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityClock extends Entity {
    private int time;
    private int preTime;
    private int tickRate;
    private int skipTickRate;
    private int effectTime;
    private boolean[] isSkip;
    private boolean isTimeStop;
    private ArrayList<EntityDummy> hookedEntitys;

    public EntityClock(World par1World) {
        super(par1World);
        setSize(4F, 8F);
        preTime = time = (int) ((par1World.getWorldTime() + 6000) % 12000);
        tickRate = 200;
        effectTime = 0;
        skipTickRate = 1;
        isTimeStop = true;
        hookedEntitys = new ArrayList<EntityDummy>();
        isSkip = new boolean[20];
        Arrays.fill(isSkip, false);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        time++;
        effectTime++;
        // worldObj.getChunkProvider().
        if (time % 10 == 0 && isTimeStop && skipTickRate < isSkip.length) {
            ++skipTickRate;
            Arrays.fill(isSkip, false);
            for (int i = 0; i < isSkip.length; i++) {
                if (i % (isSkip.length / skipTickRate) == 0) {
                    isSkip[i] = true;
                }
            }
        }
        if(!this.worldObj.isRemote){
            if (time % 10 == 0) {
                for (Object entity : worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(40, 40, 40))) {
                    if (entity instanceof Entity && !(entity instanceof EntityPlayer) && !(entity instanceof EntityDummy)) {
                        hookedEntitys.add(DummyManager.replace((Entity) entity));
                    }
                }
            }
            if (effectTime > 400) {
                for (EntityDummy e : hookedEntitys) {
                    DummyManager.restore(e);
                }
                hookedEntitys.clear();
                setDead();
            }
        }
        if (effectTime < 20 || effectTime > 380) {
            preTime += (effectTime % 2 == 0 ? 1 : 0);
        }

    }

    public int getTime() {
        return preTime;
    }

    @Override
    public void setDead() {
        super.setDead();
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
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
    }

}
