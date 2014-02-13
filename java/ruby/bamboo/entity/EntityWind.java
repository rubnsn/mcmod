package ruby.bamboo.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.block.ICustomPetal;

public class EntityWind extends EntityThrowable {
    private int age;
    private int maxAge;

    public EntityWind(World par1World) {
        super(par1World);
        setSize(5F, 5F);
    }

    public EntityWind(World par1World, EntityLivingBase par2EntityLiving) {
        super(par1World, par2EntityLiving);
        yOffset = +1;
        setSize(5F, 5F);
        maxAge = 5;
    }

    /*
     * @Override public boolean canBeCollidedWith() { return !isDead; }
     *
     * @Override public AxisAlignedBB getBoundingBox() { return boundingBox; }
     */
    @Override
    protected void onImpact(MovingObjectPosition var1) {
        if (var1.entityHit instanceof EntityLivingBase) {
            if (var1.entityHit.ridingEntity == null && var1.entityHit.riddenByEntity == null) {
                if (!BambooCore.getConf().windPushPlayer && var1.entityHit instanceof EntityPlayer) {
                    return;
                }

                var1.entityHit.motionX = this.motionX;
                var1.entityHit.motionY = this.motionY;
                var1.entityHit.motionZ = this.motionZ;
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (age++ > maxAge) {
            setDead();
        }

        if ((age & 1) == 0) {
            func_145775_I();
        }
    }

    @Override
    protected void func_145775_I() {
        int var1 = MathHelper.floor_double(this.boundingBox.minX + 0.001D);
        int var2 = MathHelper.floor_double(this.boundingBox.minY + 0.001D);
        int var3 = MathHelper.floor_double(this.boundingBox.minZ + 0.001D);
        int var4 = MathHelper.floor_double(this.boundingBox.maxX - 0.001D);
        int var5 = MathHelper.floor_double(this.boundingBox.maxY - 0.001D);
        int var6 = MathHelper.floor_double(this.boundingBox.maxZ - 0.001D);

        if (this.worldObj.checkChunksExist(var1, var2, var3, var4, var5, var6)) {
            for (int var7 = var1; var7 <= var4; ++var7) {
                for (int var8 = var2; var8 <= var5; ++var8) {
                    for (int var9 = var3; var9 <= var6; ++var9) {
                        Block var10 = this.worldObj.getBlock(var7, var8, var9);

                        if (var10 != Blocks.air) {
                            if (var10.getMaterial() == Material.leaves || var10.getMaterial() == Material.vine) {
                                removeLeaves(this.worldObj, var7, var8, var9, this, var10);
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeLeaves(World par1World, int par2, int par3, int par4, EntityWind par5Entity, Block block) {
        if (par1World.isRemote) {
            EntitySakuraPetal entity = new EntitySakuraPetal(par1World, par2, par3, par4, par5Entity.motionX, par5Entity.motionY, par5Entity.motionZ, block.getRenderColor(par1World.getBlockMetadata(par2, par3, par4)));

            if (block instanceof ICustomPetal) {
                entity.setCustomPetal(((ICustomPetal) block).getCustomPetal(par1World.getBlockMetadata(par2, par3, par4)));
            }

            par1World.spawnEntityInWorld(entity);
            // par1World.setBlockWithNotify(par2, par3, par4, 0);
        } else {
            block.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }
}
