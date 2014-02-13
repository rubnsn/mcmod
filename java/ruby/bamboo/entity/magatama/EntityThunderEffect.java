package ruby.bamboo.entity.magatama;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityThunderEffect extends Entity {
    public double tposX, tposY, tposZ;
    private long boltVertex;
    private int lightningState;
    private int boltLivingTime;

    public EntityThunderEffect(World par1World) {
        super(par1World);
        boltVertex = this.rand.nextLong();
        this.lightningState = 2;
        this.boltLivingTime = this.rand.nextInt(3) + 1;
        setSize(20.0F, 20.0F);
    }

    @Override
    public void onUpdate() {
        --this.lightningState;
        if (this.lightningState < 0) {
            if (this.boltLivingTime == 0) {
                this.boltLivingTime = this.rand.nextInt(3) + 1;
                // this.setDead();
            } else if (this.lightningState < -this.rand.nextInt(10)) {
                --this.boltLivingTime;
                this.lightningState = this.rand.nextInt(4);
                this.boltVertex = this.rand.nextLong();
            }
        }
    }

    public long getBoltVertex() {
        return boltVertex;
    }

    @Override
    public boolean isInRangeToRender3d(double dx, double dy, double dz) {
        return this.lightningState >= 0;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    }

}