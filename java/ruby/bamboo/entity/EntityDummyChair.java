package ruby.bamboo.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import ruby.bamboo.block.IChairDeadListener;

public class EntityDummyChair extends Entity {
    int cx, cy, cz;

    public EntityDummyChair(World par1World) {
        super(par1World);
        this.noClip = true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!worldObj.isRemote) {
            if (this.riddenByEntity == null) {
                this.setDead();
            }
            if (worldObj.isAirBlock(cx, cy, cz)) {
                this.setDead();
            }
            if (this.isDead) {
                Block block = worldObj.getBlock(cx, cy, cz);
                if (block instanceof IChairDeadListener) {
                    ((IChairDeadListener) block).onChairDead(worldObj, cx, cy, cz);
                }
            }
        }
        updateDummy();
    }

    @Override
    public void setDead() {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.mountEntity(null);
        }
        super.setDead();
    }

    public void setChairBlock(int x, int y, int z) {
        this.cx = x;
        this.cy = y;
        this.cz = z;
    }

    public void updateDummy() {
    }

    @Override
    public double getMountedYOffset() {
        return 0;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {
    }
}
