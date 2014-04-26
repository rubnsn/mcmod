package ruby.bamboo.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EntityFirefly extends EntityFlying {
    private boolean firstUpdate;
    private double homePosX;
    private double homePosY;
    private double homePosZ;
    private float targetX;
    private float targetY;
    private float targetZ;
    private int courseChangeCooldown;
    private float alpha;
    private boolean f;

    public EntityFirefly(World par1World) {
        super(par1World);

        setSize(0.4F, 0.4F);
        firstUpdate = true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!worldObj.isRemote) {
            float f = this.getBrightness(1.0F);

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))) {
                setDead();
            }
            this.despawnEntity();
            if (firstUpdate) {
                homePosX = posX;
                homePosY = posY;
                homePosZ = posZ;
                targetX = (float) (homePosX + rand.nextFloat() * 6 - 3);
                targetY = (float) (homePosY + rand.nextFloat() * 3);
                targetZ = (float) (homePosZ + rand.nextFloat() * 6 - 3);
                firstUpdate = false;
            }
            double d0 = (double) targetX - this.posX;
            double d1 = (double) targetY - this.posY;
            double d2 = (double) targetZ - this.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 < 1.0D || d3 > 3600.0D) {
                targetX = (float) (homePosX + rand.nextFloat() * 6 - 3);
                targetY = (float) (homePosY + rand.nextFloat() * 3);
                targetZ = (float) (homePosZ + rand.nextFloat() * 6 - 3);
            }

            d3 = (double) MathHelper.sqrt_double(d3);
            if (this.isCourseTraversable(this.targetX, this.targetY, this.targetZ, d3)) {
                this.motionX = d0 / d3 * 0.1D;
                this.motionY = d1 / d3 * 0.1D;
                this.motionZ = d2 / d3 * 0.1D;
            }
        } else {
            if (this.courseChangeCooldown-- <= 0) {
                alpha += f ? 0.05F : -0.05F;
                f = f ? !(alpha > 1) : !(0 < alpha);
                if (alpha < 0) {
                    this.courseChangeCooldown += this.rand.nextInt(100) + 100;
                }
            }
        }
        //setDead();
    }

    @Override
    protected boolean interact(EntityPlayer par1EntityPlayer) {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
        //そのうち捕まえられるかも？
        return true;
    }

    public float getAlpha() {
        return alpha;
    }

    private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
        double d4 = (this.targetX - this.posX) / par7;
        double d5 = (this.targetY - this.posY) / par7;
        double d6 = (this.targetZ - this.posZ) / par7;
        AxisAlignedBB axisalignedbb = this.boundingBox.copy();

        for (int i = 1; (double) i < par7; ++i) {
            axisalignedbb.offset(d4, d5, d6);

            if (!this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity par1Entity) {
    }

    @Override
    protected void collideWithNearbyEntities() {
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.isValidLightLevel() && this.worldObj.getBlock((int) this.posX, (int) this.posY - 1, (int) this.posZ).getMaterial() == Material.water && super.getCanSpawnHere();
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 10;
    }

    protected boolean isValidLightLevel() {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);

        if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > this.rand.nextInt(32)) {
            return false;
        } else {
            int l = this.worldObj.getBlockLightValue(i, j, k);

            if (this.worldObj.isThundering()) {
                int i1 = this.worldObj.skylightSubtracted;
                this.worldObj.skylightSubtracted = 10;
                l = this.worldObj.getBlockLightValue(i, j, k);
                this.worldObj.skylightSubtracted = i1;
            }

            return l <= this.rand.nextInt(8);
        }
    }
}
