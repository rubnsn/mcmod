package ruby.bamboo.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import ruby.bamboo.BambooInit;

public class EntityBambooSpear extends EntityArrow {
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private boolean inGround;
    private Block inTile;
    private int inData;
    private int ticksInGround;
    private int ticksInAir;
    private int knockbackStrength;

    private float power;
    private int count;
    private boolean isBarrage;
    private boolean isDie;

    private boolean isExplode = false;
    private Entity hitEntity;
    private int explodeTimer;

    private int maxAge = 600;
    private static final ArrayList<Class<Entity>> ignoreList = new ArrayList<Class<Entity>>();
    static {
        String[] ignoreEntitys = { "LittleMaid" };
        for (String str : ignoreEntitys) {
            if (EntityList.stringToClassMapping.containsKey(str)) {
                ignoreList.add((Class<Entity>) EntityList.stringToClassMapping.get(str));
            }
        }

        try {
            Class fAva = Class.forName("LMM_EntityLittleMaidAvatar");
            ignoreList.add(fAva);
        } catch (ClassNotFoundException e) {
            System.out.println("LMMを検知しませんでした");
        }
    }

    public EntityBambooSpear(World par1World) {
        super(par1World);
    }

    public EntityBambooSpear(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
        canBePickedUp = 1;
    }

    public EntityBambooSpear(World par1World, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving, float par4, float par5) {
        super(par1World, par2EntityLiving, par3EntityLiving, par4, par5);
    }

    public EntityBambooSpear(World par1World, EntityLivingBase par2EntityLiving, float par3) {
        super(par1World, par2EntityLiving, par3);
        count = 0;
        power = par3;
        isBarrage = false;
        isDie = false;
        shootingEntity = par2EntityLiving;
    }

    public EntityBambooSpear(World par1World, EntityBambooSpear par2EntityBambooSpear, float par3, boolean canBePicedUp) {
        super(par1World);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = par2EntityBambooSpear;
        if (canBePicedUp) {
            this.canBePickedUp = 1;
        }

        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(par2EntityBambooSpear.posX, par2EntityBambooSpear.posY + par2EntityBambooSpear.getEyeHeight(), par2EntityBambooSpear.posZ, par2EntityBambooSpear.rotationYaw, par2EntityBambooSpear.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        this.posY -= 0.10000000149011612D;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
        this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, par3 * 1.5F, 1.0F);
    }

    // ばくはつ
    public void setExplode() {
        this.isExplode = true;
    }

    // 連射
    public void setBarrage(int attackCount) {
        this.count = attackCount;
        this.isBarrage = true;
    }

    // クリエイティブ連射負荷対策
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public double getDamage() {
        return isExplode ? 0 : super.getDamage();
    }

    @Override
    public void onUpdate() {
        onEntityUpdate();
        onUpdateArrow();

        if (0 < count) {
            worldObj.playSoundAtEntity(shootingEntity, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 1.2F) + power * 0.5F);

            if (!worldObj.isRemote) {
                EntityBambooSpear ebs;
                if (ignoreList.contains(shootingEntity.getClass())) {
                    ebs = new EntityBambooSpear(worldObj, this, power, true);
                } else {
                    ebs = new EntityBambooSpear(worldObj, (EntityLivingBase) shootingEntity, power);
                }

                if (this.isExplode) {
                    ebs.setExplode();
                }

                ebs.setIsCritical(power >= 1);
                ebs.setBarrage(0);
                ebs.setDamage(getDamage() * 0.7);
                ebs.setFire(isBurning() ? 100 : 0);
                ebs.canBePickedUp = this.canBePickedUp;
                ebs.setMaxAge(this.maxAge);
                worldObj.spawnEntityInWorld(ebs);
            }

            count--;
        } else {
            if (isDie) {
                setDead();
            }
        }

        if (isExplode && hitEntity != null) {
            if (explodeTimer++ > 60 && !hitEntity.isDead) {
                hitEntity.motionY += 0.8;
                worldObj.createExplosion(this, hitEntity.posX, hitEntity.posY + 1, hitEntity.posZ, 0, true);
                this.setDead();
            }
        }
    }

    private void onUpdateArrow() {
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, var1) * 180.0D / Math.PI);
        }

        Block var16 = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);

        if (var16 != Blocks.air) {
            var16.setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB var2 = var16.getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);

            if (var2 != null && var2.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            Block var18 = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
            int var19 = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);

            if (var18 == this.inTile && var19 == this.inData) {
                ++this.ticksInGround;

                if (this.ticksInGround == maxAge) {
                    this.setDead();
                }
            } else {
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2F;
                this.motionY *= this.rand.nextFloat() * 0.2F;
                this.motionZ *= this.rand.nextFloat() * 0.2F;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            ++this.ticksInAir;
            Vec3 var17 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 var3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var4 = this.worldObj.func_147447_a(var17, var3, false, true, false);
            var17 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            var3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (var4 != null) {
                var3 = Vec3.createVectorHelper(var4.hitVec.xCoord, var4.hitVec.yCoord, var4.hitVec.zCoord);
            }

            Entity var5 = null;
            List var6 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var7 = 0.0D;
            Iterator var9 = var6.iterator();
            float var11;

            while (var9.hasNext()) {
                Entity var10 = (Entity) var9.next();

                if (var10.canBeCollidedWith() && (var10 != this.shootingEntity || this.ticksInAir >= 5)) {
                    var11 = 0.3F;
                    AxisAlignedBB var12 = var10.boundingBox.expand(var11, var11, var11);
                    MovingObjectPosition var13 = var12.calculateIntercept(var17, var3);

                    if (var13 != null) {
                        double var14 = var17.distanceTo(var13.hitVec);

                        if (var14 < var7 || var7 == 0.0D) {
                            var5 = var10;
                            var7 = var14;
                        }
                    }
                }
            }

            if (var5 != null) {
                var4 = new MovingObjectPosition(var5);
            }

            float var20;

            if (var4 != null) {
                if (var4.entityHit != null) {
                    if (!ignoreList.contains(var4.entityHit.getClass())) {
                        var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                        int var24 = MathHelper.ceiling_double_int(var20 * 0.7 * getDamage());

                        if (this.getIsCritical()) {
                            var24 += this.rand.nextInt(var24 / 2 + 2);
                        }

                        DamageSource var22 = null;

                        if (this.shootingEntity == null) {
                            var22 = DamageSource.causeArrowDamage(this, this);
                        } else {
                            var22 = DamageSource.causeArrowDamage(this, this.shootingEntity);
                        }

                        if (this.isBurning()) {
                            var4.entityHit.setFire(5);
                        }

                        if (var4.entityHit.attackEntityFrom(var22, var24)) {
                            if (var4.entityHit instanceof EntityLiving) {
                                // 対象セット
                                if (isExplode) {
                                    this.hitEntity = var4.entityHit;
                                }

                                // 連射による無敵時間削除
                                if (isBarrage) {
                                    var4.entityHit.hurtResistantTime = 0;
                                }

                                ++((EntityLiving) var4.entityHit).arrowHitTimer;

                                if (this.knockbackStrength > 0) {
                                    float var25 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                    if (var25 > 0.0F) {
                                        var4.entityHit.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579D / var25, 0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D / var25);
                                    }
                                }
                            }

                            this.worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                            if (!isExplode) {
                                this.setDead();
                            }
                        }
                    }
                } else {
                    this.xTile = var4.blockX;
                    this.yTile = var4.blockY;
                    this.zTile = var4.blockZ;
                    this.inTile = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
                    this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                    this.motionX = ((float) (var4.hitVec.xCoord - this.posX));
                    this.motionY = ((float) (var4.hitVec.yCoord - this.posY));
                    this.motionZ = ((float) (var4.hitVec.zCoord - this.posZ));
                    var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / var20 * 0.05000000074505806D;
                    this.posY -= this.motionY / var20 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / var20 * 0.05000000074505806D;
                    this.worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.arrowShake = 7;
                    this.setIsCritical(false);
                }
            }

            if (this.getIsCritical()) {
                for (int var21 = 0; var21 < 4; ++var21) {
                    this.worldObj.spawnParticle("crit", this.posX + this.motionX * var21 / 4.0D, this.posY + this.motionY * var21 / 4.0D, this.posZ + this.motionZ * var21 / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float) (Math.atan2(this.motionY, var20) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                ;
            }

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
            float var23 = 0.99F;
            var11 = 0.05F;

            if (this.isInWater()) {
                for (int var26 = 0; var26 < 4; ++var26) {
                    float var27 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * var27, this.posY - this.motionY * var27, this.posZ - this.motionZ * var27, this.motionX, this.motionY, this.motionZ);
                }

                var23 = 0.8F;
            }

            this.motionX *= var23;
            this.motionY *= var23;
            this.motionZ *= var23;
            this.motionY -= var11;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.func_145775_I();
        }
    }

    // 1本めがHit時連射が止まるため細工
    @Override
    public void setDead() {
        if (count < 1) {
            super.setDead();
        } else {
            isDie = true;
        }
    }

    // 拾った時弓矢に化ける対策
    @Override
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
        if (!this.worldObj.isRemote && inGround && this.arrowShake <= 0) {
            boolean var2 = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.capabilities.isCreativeMode;

            if (this.canBePickedUp == 1 && !par1EntityPlayer.inventory.addItemStackToInventory(getPickUpItem())) {
                var2 = false;
            }

            if (var2) {
                this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                par1EntityPlayer.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    public ItemStack getPickUpItem() {
        if (isExplode) {
            return new ItemStack(BambooInit.bambooSpear, 1, 1);
        } else {
            return new ItemStack(BambooInit.bambooSpear, 1, 0);
        }
    }
}
