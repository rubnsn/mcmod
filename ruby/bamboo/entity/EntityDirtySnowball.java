package ruby.bamboo.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityDirtySnowball extends EntitySnowball {
    private EnumDirtySnowball eds;

    public EntityDirtySnowball(World par1World) {
        super(par1World);
    }

    public EntityDirtySnowball(World par1World, EntityLivingBase par2EntityLiving, EnumDirtySnowball eds) {
        super(par1World, par2EntityLiving);
        this.eds = eds;
    }

    public EntityDirtySnowball(World par1World, double par2, double par4, double par6, EnumDirtySnowball eds) {
        super(par1World, par2, par4, par6);
        this.eds = eds;
    }

    @Override
    public void onImpact(MovingObjectPosition par1MovingObjectPosition) {
        if (eds == null) {
            this.kill();
            return;
        }

        if (par1MovingObjectPosition.entityHit != null) {
            byte var2 = eds.getDmg();

            /*
             * if (par1MovingObjectPosition.entityHit instanceof EntityBlaze) {
             * var2 += 3; }
             */
            if (par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), var2)) {
                ;
            }

            if (par1MovingObjectPosition.entityHit instanceof EntityLiving) {
                EntityLiving target = (EntityLiving) par1MovingObjectPosition.entityHit;

                switch (eds) {
                case ender:
                    teleportRandomly(target);
                    break;

                case poison:
                    target.addPotionEffect(new PotionEffect(Potion.poison.getId(), 30, 3));
                    break;

                case confusion:
                    target.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 200, 1));
                    break;

                case heal:
                    target.addPotionEffect(new PotionEffect(Potion.regeneration.getId(), 50, 1));
                    break;

                default:
                    break;
                }
            }
        }

        for (int var3 = 0; var3 < 8; ++var3) {
            this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }

        if (!this.worldObj.isRemote) {
            this.setDead();
        }
    }

    private boolean teleportRandomly(EntityLiving targe) {
        double var1 = this.posX + (this.rand.nextDouble() - 0.5D) * 32.0D;
        double var3 = this.posY + (this.rand.nextInt(8) - 4);
        double var5 = this.posZ + (this.rand.nextDouble() - 0.5D) * 32.0D;
        return this.teleportTo(targe, var1, var3, var5);
    }

    private boolean teleportTo(EntityLiving target, double par1, double par3, double par5) {
        double var7 = target.posX;
        double var9 = target.posY;
        double var11 = target.posZ;
        target.posX = par1;
        target.posY = par3;
        target.posZ = par5;
        boolean var13 = false;
        int var14 = MathHelper.floor_double(target.posX);
        int var15 = MathHelper.floor_double(target.posY);
        int var16 = MathHelper.floor_double(target.posZ);
        int var18;

        if (target.worldObj.blockExists(var14, var15, var16)) {
            boolean var17 = false;

            while (!var17 && var15 > 0) {
                var18 = target.worldObj.getBlockId(var14, var15 - 1, var16);

                if (var18 != 0 && Block.blocksList[var18].blockMaterial.blocksMovement()) {
                    var17 = true;
                } else {
                    --target.posY;
                    --var15;
                }
            }

            if (var17) {
                target.setPosition(target.posX, target.posY, target.posZ);

                if (target.worldObj.getCollidingBoundingBoxes(this, target.boundingBox).size() == 0 && !target.worldObj.isAnyLiquid(target.boundingBox)) {
                    var13 = true;
                }
            }
        }

        if (!var13) {
            target.setPosition(var7, var9, var11);
            return false;
        } else {
            short var30 = 128;

            for (var18 = 0; var18 < var30; ++var18) {
                double var19 = var18 / (var30 - 1.0D);
                float var21 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float var22 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float var23 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                double var24 = var7 + (target.posX - var7) * var19 + (this.rand.nextDouble() - 0.5D) * target.width * 2.0D;
                double var26 = var9 + (target.posY - var9) * var19 + this.rand.nextDouble() * target.height;
                double var28 = var11 + (target.posZ - var11) * var19 + (this.rand.nextDouble() - 0.5D) * target.width * 2.0D;
                target.worldObj.spawnParticle("portal", var24, var26, var28, var21, var22, var23);
            }

            target.worldObj.playSoundEffect(var7, var9, var11, "mob.endermen.portal", 1.0F, 1.0F);
            target.worldObj.playSoundAtEntity(this, "mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }
}
