package ruby.bamboo.entity.magatama;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityShield extends Entity {
    private Entity owner;
    private Entity thrower;
    private Entity throwObject;
    private boolean isReflection;
    private int age;

    public EntityShield(World par1World) {
        super(par1World);
        this.isReflection = false;
        age = 0;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (age == 0 && worldObj.isRemote) {
            /*EntityRuneEffect rune = new EntityRuneEffect(worldObj);
            rune.setRingsize(0.5F);
            rune.setRollSpeed(1);
            rune.setRingColor(0xff6600);
            rune.setParentEntity(this);
            rune.setPositionAndRotation(posX, posY - 0.5F, posZ, rotationYaw + 90, rotationPitch + 90);
            worldObj.joinEntityInSurroundings(rune);*/
        }
        if (age++ > 200) {
            this.setDead();
        }
        //this.setDead();
    }

    private void setThrowebleHeading(Entity throwObject) {
        double d0 = this.posX - thrower.posX;
        double d1 = this.posY - thrower.posY;
        double d2 = this.posZ - thrower.posZ;
        double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        float f = (float) (Math.atan2(d2, d0) / Math.PI * 180) + 90F;
        float f1 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
        throwObject.rotationPitch = f1;
        throwObject.rotationYaw = f;
        float f2 = 0.4F;
        throwObject.motionX = -MathHelper.sin(throwObject.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(throwObject.rotationPitch / 180.0F * (float) Math.PI) * f2;
        throwObject.motionZ = MathHelper.cos(throwObject.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(throwObject.rotationPitch / 180.0F * (float) Math.PI) * f2;
        throwObject.motionY = -MathHelper.sin(throwObject.rotationPitch / 180.0F * (float) Math.PI) * f2;

        throwObject.motionX = throwObject.motionY = throwObject.motionZ = 0;
        //((IProjectile) throwObject).setThrowableHeading(throwObject.motionX, throwObject.motionY, throwObject.motionZ, 1.5F, 1.0F);
    }

    private float updateRotation(float par1, float par2, float par3) {
        float f3 = MathHelper.wrapAngleTo180_float(par2 - par1);

        if (f3 > par3) {
            f3 = par3;
        }

        if (f3 < -par3) {
            f3 = -par3;
        }

        return par1 + f3;
    }

    @Override
    public void setDead() {
        super.setDead();
        if (throwObject != null) {
            throwObject.setDead();
        }
    }

    private void reflectionThrow(IProjectile iProjectile) {

    }

    public void setThrower(Entity thrower, Entity target) {
        this.throwObject = DummyManager.replace(target);
        this.thrower = thrower;

    }

    public void setReflection() {
        isReflection = true;
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.setDead();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {

    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    public Entity getOwner() {
        return owner;
    }
}
