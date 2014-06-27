package ruby.bamboo.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import ruby.bamboo.Config;

public class EntityFirecracker extends EntityThrowable {
    float[] explodePower = { 0.01F, 2.9F, 3.5F };

    public EntityFirecracker(World par1World) {
        super(par1World);
        setSize(1, 1);
    }

    public EntityFirecracker(World par1World, EntityLivingBase par2EntityLiving, int LV) {
        super(par1World, par2EntityLiving);
        setSize(1, 1);
        setExplodeLv((byte) LV);
    }

    public EntityFirecracker(World par1World, double par2, double par4, double par6, int LV) {
        super(par1World, par2, par4, par6);
        this.setSize(1, 1);
        setExplodeLv((byte) LV);
    }

    private void setExplodeLv(byte lv) {
        dataWatcher.updateObject(15, lv);
    }

    public byte getExplodeLv() {
        return dataWatcher.getIsBlank() ? 0 : dataWatcher.getWatchableObjectByte(15);
    }

    @Override
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
        if (par1MovingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK || par1MovingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            if (getExplodeLv() <= Config.maxExplosionLv) {
                // クライアント：常時0エフェクトのみ
                // サーバー：0～3,0爆発なし(configのみ)1黄2緑3赤
                float power = worldObj.isRemote ? 0 : getExplodeLv() == 0 ? 0 : explodePower[getExplodeLv() - 1];
                createExplosion(this, posX, posY, posZ, power);
            }
        }

        this.setDead();
    }

    private void createExplosion(EntityFirecracker entityFirecracker, double posX, double posY, double posZ, float power) {
        Explosion explosion = new Explosion(worldObj, entityFirecracker, posX, posY, posZ, power) {
            private int field_77289_h = 16;
            private Map field_77288_k = new HashMap();
            private Random explosionRNG = new Random();
            HashSet destroyedBlockPositions = new HashSet();

            @Override
            public void doExplosionA() {
                float var1 = this.explosionSize;
                HashSet var2 = new HashSet();
                int var3;
                int var4;
                int var5;
                double var15;
                double var17;
                double var19;

                for (var3 = 0; var3 < this.field_77289_h; ++var3) {
                    for (var4 = 0; var4 < this.field_77289_h; ++var4) {
                        for (var5 = 0; var5 < this.field_77289_h; ++var5) {
                            if (var3 == 0 || var3 == this.field_77289_h - 1 || var4 == 0 || var4 == this.field_77289_h - 1 || var5 == 0 || var5 == this.field_77289_h - 1) {
                                double var6 = var3 / (this.field_77289_h - 1.0F) * 2.0F - 1.0F;
                                double var8 = var4 / (this.field_77289_h - 1.0F) * 2.0F - 1.0F;
                                double var10 = var5 / (this.field_77289_h - 1.0F) * 2.0F - 1.0F;
                                double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
                                var6 /= var12;
                                var8 /= var12;
                                var10 /= var12;
                                float var14 = this.explosionSize * (0.7F + worldObj.rand.nextFloat() * 0.6F);
                                var15 = this.explosionX;
                                var17 = this.explosionY;
                                var19 = this.explosionZ;

                                for (float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F) {
                                    int var22 = MathHelper.floor_double(var15);
                                    int var23 = MathHelper.floor_double(var17);
                                    int var24 = MathHelper.floor_double(var19);
                                    Block var25 = worldObj.getBlock(var22, var23, var24);

                                    if (var25 == Blocks.flowing_water || var25 == Blocks.water || var25 == Blocks.obsidian || var25 == Blocks.bedrock) {
                                        continue;
                                    }

                                    if (var1 < 3 && var25 != Blocks.air) {
                                        var14 -= (var25.getExplosionResistance(this.exploder) + 0.3F) * var21;
                                    }

                                    if (!worldObj.isRemote && var14 > 0.0F) {
                                        var2.add(new ChunkPosition(var22, var23, var24));
                                    }

                                    var15 += var6 * var21;
                                    var17 += var8 * var21;
                                    var19 += var10 * var21;
                                }
                            }
                        }
                    }
                }

                this.affectedBlockPositions.addAll(var2);
                this.explosionSize *= 2.0F;
                var3 = MathHelper.floor_double(this.explosionX - this.explosionSize - 1.0D);
                var4 = MathHelper.floor_double(this.explosionX + this.explosionSize + 1.0D);
                var5 = MathHelper.floor_double(this.explosionY - this.explosionSize - 1.0D);
                int var27 = MathHelper.floor_double(this.explosionY + this.explosionSize + 1.0D);
                int var7 = MathHelper.floor_double(this.explosionZ - this.explosionSize - 1.0D);
                int var28 = MathHelper.floor_double(this.explosionZ + this.explosionSize + 1.0D);
                List var9 = worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getBoundingBox(var3, var5, var7, var4, var27, var28));
                Vec3 var29 = Vec3.createVectorHelper(this.explosionX, this.explosionY, this.explosionZ);

                for (int var11 = 0; var11 < var9.size(); ++var11) {
                    Entity var30 = (Entity) var9.get(var11);
                    double var13 = var30.getDistance(this.explosionX, this.explosionY, this.explosionZ) / this.explosionSize;

                    if (var13 <= 1.0D) {
                        var15 = var30.posX - this.explosionX;
                        var17 = var30.posY + var30.getEyeHeight() - this.explosionY;
                        var19 = var30.posZ - this.explosionZ;
                        double var32 = MathHelper.sqrt_double(var15 * var15 + var17 * var17 + var19 * var19);

                        if (var32 != 0.0D) {
                            var15 /= var32;
                            var17 /= var32;
                            var19 /= var32;
                            double var31 = worldObj.getBlockDensity(var29, var30.boundingBox);
                            double var33 = (1.0D - var13) * var31;
                            var30.attackEntityFrom(DamageSource.setExplosionSource(this), (int) ((var33 * var33 + var33) / 2.0D * 8.0D * this.explosionSize + 1.0D));
                            var30.motionX += var15 * var33;
                            var30.motionY += var17 * var33;
                            var30.motionZ += var19 * var33;

                            if (var30 instanceof EntityPlayer) {
                                this.field_77288_k.put(var30, Vec3.createVectorHelper(var15 * var33, var17 * var33, var19 * var33));
                            }
                        }
                    }
                }

                this.explosionSize = var1;
            }

            @Override
            public void doExplosionB(boolean par1) {
                worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
                worldObj.spawnParticle("hugeexplosion", this.explosionX, this.explosionY, this.explosionZ, 0.0D, 0.0D, 0.0D);
                Iterator var2 = this.affectedBlockPositions.iterator();
                ChunkPosition var3;
                int var4;
                int var5;
                int var6;
                Block var7;

                while (var2.hasNext()) {
                    var3 = (ChunkPosition) var2.next();
                    var4 = var3.chunkPosX;
                    var5 = var3.chunkPosY;
                    var6 = var3.chunkPosZ;
                    var7 = worldObj.getBlock(var4, var5, var6);

                    if (par1) {
                        double var8 = var4 + worldObj.rand.nextFloat();
                        double var10 = var5 + worldObj.rand.nextFloat();
                        double var12 = var6 + worldObj.rand.nextFloat();
                        double var14 = var8 - this.explosionX;
                        double var16 = var10 - this.explosionY;
                        double var18 = var12 - this.explosionZ;
                        double var20 = MathHelper.sqrt_double(var14 * var14 + var16 * var16 + var18 * var18);
                        var14 /= var20;
                        var16 /= var20;
                        var18 /= var20;
                        double var22 = 0.5D / (var20 / this.explosionSize + 0.1D);
                        var22 *= worldObj.rand.nextFloat() * worldObj.rand.nextFloat() + 0.3F;
                        var14 *= var22;
                        var16 *= var22;
                        var18 *= var22;
                        worldObj.spawnParticle("explode", (var8 + this.explosionX * 1.0D) / 2.0D, (var10 + this.explosionY * 1.0D) / 2.0D, (var12 + this.explosionZ * 1.0D) / 2.0D, var14, var16, var18);
                        worldObj.spawnParticle("smoke", var8, var10, var12, var14, var16, var18);
                    }

                    if (var7 != Blocks.air) {
                        if (explosionSize < 3 && explosionRNG.nextInt(1) == 0) {
                            var7.dropBlockAsItemWithChance(worldObj, var4, var5, var6, worldObj.getBlockMetadata(var4, var5, var6), 0.3F, 0);
                        }
                        worldObj.setBlockToAir(var4, var5, var6);

                        var7.onBlockDestroyedByExplosion(worldObj, var4, var5, var6, this);
                    }
                }
            }
        };
        explosion.isFlaming = false;
        explosion.doExplosionA();
        explosion.doExplosionB(true);
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObject(15, (byte) 0);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setByte("power", dataWatcher.getWatchableObjectByte(15));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("power")) {
            dataWatcher.updateObject(15, nbttagcompound.getByte("power"));
        }
    }

}
