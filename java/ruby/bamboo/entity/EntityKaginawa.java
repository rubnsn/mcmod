package ruby.bamboo.entity;

import ruby.bamboo.KaginawaHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityKaginawa extends EntityThrowable {
    private double vMotionX;
    private double vMotionY;
    private double vMotionZ;
    private int timelimit;

    public EntityKaginawa(World par1World) {
        super(par1World);
        this.kill();
    }

    public EntityKaginawa(World par1World, EntityLivingBase e) {
        super(par1World, e);
        timelimit = 0;
    }

    @Override
    public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8) {
        float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= f2;
        par3 /= f2;
        par5 /= f2;
        par7 *= 2;
        par1 += 0.5 * 0.007499999832361937D * par8;
        par3 += 0.5 * 0.007499999832361937D * par8;
        par5 += 0.5 * 0.007499999832361937D * par8;
        par1 *= par7;
        par3 *= par7;
        par5 *= par7;
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        float f3 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, f3) * 180.0D / Math.PI);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (timelimit++ > 15) {
            this.setDead();
        }
    }

    // rotationPitch:-90～90までの上下角度
    @Override
    protected void onImpact(MovingObjectPosition movingobjectposition) {
        vMotionX = (this.posX - this.getThrower().posX) / 7;

        if ((this.posY - this.getThrower().posY) > 0) {
            vMotionY = (this.posY - this.getThrower().posY) / (10 - Math.pow((this.posY - this.getThrower().posY - 30), 2) / 95);
        } else {
            vMotionY = 0.4;
        }

        vMotionZ = (this.posZ - this.getThrower().posZ) / 7;

        if (worldObj.isRemote) {
            this.getThrower().motionX += vMotionX;
            this.getThrower().motionY = vMotionY;
            this.getThrower().motionZ += vMotionZ;
            this.getThrower().fallDistance = 0;
        } else {
            this.getThrower().fallDistance = 0;
        }

        /*
         * this.getThrower().motionX=motionX; this.getThrower().motionY=motionY;
         * this.getThrower().motionZ=motionZ;
         */
        this.setDead();
    }

    @Override
    public void setDead() {
        if (this.getThrower() != null) {
            if (this.getThrower().worldObj.isRemote) {
                KaginawaHandler.setUsageState(false);
            } else {
                KaginawaHandler.setUsageState(this.getThrower(), false);
            }
        }

        this.isDead = true;
    }
}
