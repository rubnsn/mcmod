package ruby.bamboo.item.enchant;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EnchantTeleport extends EnchantBase {

    public EnchantTeleport(int id, String name, int maxLevel, float weight, float tp) {
        super(id, name, maxLevel, weight, tp);
    }

    @Override
    void effect(ItemStack itemStack, World world, int posX, int posY, int posZ, EntityLivingBase entity, int enchantLvl) {
        int tposX, tposY, tposZ;
        tposX = rand.nextInt(enchantLvl * 20) - enchantLvl * 10;
        tposY = rand.nextInt(enchantLvl * 20) - enchantLvl * 10;
        tposZ = rand.nextInt(enchantLvl * 20) - enchantLvl * 10;
        teleportTo(entity, entity.posX + tposX, entity.posY + tposY, entity.posZ + tposZ);
    }

    @Override
    public float getTriggerPercent(int enchantLvl) {
        return tp + (enchantLvl * 0.01F);
    }

    private boolean teleportTo(EntityLivingBase target, double par1, double par3, double par5) {

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
        Block var18;

        if (target.worldObj.blockExists(var14, var15, var16)) {
            boolean var17 = false;

            while (!var17 && var15 > 0) {
                var18 = target.worldObj.getBlock(var14, var15 - 1, var16);

                if (var18 != Blocks.air && var18.getMaterial().blocksMovement()) {
                    var17 = true;
                } else {
                    --target.posY;
                    --var15;
                }
            }

            if (var17) {
                target.setPositionAndUpdate(target.posX, target.posY, target.posZ);

                if (target.worldObj.getCollidingBoundingBoxes(target, target.boundingBox).size() == 0 && !target.worldObj.isAnyLiquid(target.boundingBox)) {
                    var13 = true;
                }
            }
        }

        if (!var13) {
            target.setPositionAndUpdate(var7, var9, var11);
            return false;
        } else {
            short var30 = 128;

            for (int i = 0; i < var30; ++i) {
                double var19 = i / (var30 - 1.0D);
                float var21 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float var22 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float var23 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                double var24 = var7 + (target.posX - var7) * var19 + (this.rand.nextDouble() - 0.5D) * target.width * 2.0D;
                double var26 = var9 + (target.posY - var9) * var19 + this.rand.nextDouble() * target.height;
                double var28 = var11 + (target.posZ - var11) * var19 + (this.rand.nextDouble() - 0.5D) * target.width * 2.0D;
                target.worldObj.spawnParticle("portal", var24, var26, var28, var21, var22, var23);
            }

            target.worldObj.playSoundEffect(var7, var9, var11, "mob.endermen.portal", 1.0F, 1.0F);
            target.worldObj.playSoundAtEntity(target, "mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }
}
