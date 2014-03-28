package ruby.bamboo.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityThrowZabuton extends EntityThrowable implements IZabuton {
    private final static byte COLOR = 16;
    private int rollMotionZ;
    private int rollMotionY;

    private int ticksInAir;

    public EntityThrowZabuton(World par1World) {
        super(par1World);
        setSize(1F, 2 / 16F);
        prevRotationPitch = rotationPitch = 180;
        prevRotationYaw = rotationYaw = 0;
    }

    public EntityThrowZabuton(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
        setSize(1F, 2 / 16F);
        prevRotationPitch = rotationPitch = 180;
        prevRotationYaw = rotationYaw = 0;
    }

    public EntityThrowZabuton(World par1World, EntityLivingBase par2EntityLivingBase, int color) {
        super(par1World, par2EntityLivingBase);
        dataWatcher.updateObject(COLOR, (byte) color);

        prevRotationPitch = rotationPitch = 180;
        prevRotationYaw = rotationYaw = 0;
        setSize(1F, 2 / 16F);
    }

    @Override
    protected void onImpact(MovingObjectPosition var1) {
        if (var1 != null) {
            if (!worldObj.isRemote) {
                Entity entity = new EntityZabuton(worldObj, getColor());
                entity.setPositionAndRotation(posX, posY, posZ, this.rotationYaw, 180);
                if (var1.typeOfHit == MovingObjectType.ENTITY) {
                    var1.entityHit.mountEntity(entity);
                }
                worldObj.spawnEntityInWorld(entity);
            }
            this.setDead();
        }
    }

    @Override
    public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8) {
        float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= (double) f2;
        par3 /= (double) f2;
        par5 /= (double) f2;
        par1 += this.rand.nextGaussian() * 0.007499999832361937D * (double) par8;
        par3 += this.rand.nextGaussian() * 0.007499999832361937D * (double) par8;
        par5 += this.rand.nextGaussian() * 0.007499999832361937D * (double) par8;
        par1 *= (double) par7;
        par3 *= (double) par7;
        par5 *= (double) par7;
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
    }

    @Override
    public void onUpdate() {
        onTrowableUpdate();
        //rotationPitch += rollMotionZ;
        rotationYaw += rollMotionY;
    }

    public void onTrowableUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onEntityUpdate();
        ;

        if (this.throwableShake > 0) {
            --this.throwableShake;
        }

        ++this.ticksInAir;

        Vec3 vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
        Vec3 vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
        vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
        vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null) {
            vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        if (!this.worldObj.isRemote) {
            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            EntityLivingBase entitylivingbase = this.getThrower();

            for (int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity) list.get(j);

                if (entity1.canBeCollidedWith() && (entity1 != entitylivingbase || this.ticksInAir >= 5)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double) f, (double) f, (double) f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Blocks.portal) {
                this.setInPortal();
            } else {
                this.onImpact(movingobjectposition);
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

        //this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        /*for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            ;
        }*/

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        float f2 = 0.99F;
        float f3 = this.getGravityVelocity();

        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                float f4 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) f4, this.posY - this.motionY * (double) f4, this.posZ - this.motionZ * (double) f4, this.motionX, this.motionY, this.motionZ);
            }

            f2 = 0.8F;
        }

        this.motionX *= (double) f2;
        this.motionY *= (double) f2;
        this.motionZ *= (double) f2;
        this.motionY -= (double) f3;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    @Override
    public byte getColor() {
        return dataWatcher.getWatchableObjectByte(COLOR);
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObject(COLOR, (byte) 15);
        rollMotionZ = this.worldObj.rand.nextInt(10) - 5;
        rollMotionY = this.worldObj.rand.nextInt(40) + 20;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound var1) {
        super.readEntityFromNBT(var1);
        dataWatcher.updateObject(COLOR, var1.getByte("color"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound var1) {
        super.writeEntityToNBT(var1);
        var1.setByte("color", getColor());
    }
}
