package ruby.bamboo.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ItemNinjaBoots extends ItemNinjaArmor {

    public ItemNinjaBoots(int slot) {
        super(slot);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (player.isSprinting() && isEqNinjaArmor(player, LEG)) {
            if (world.getBlock(player.getPlayerCoordinates().posX, player.getPlayerCoordinates().posY - 2, player.getPlayerCoordinates().posZ).getMaterial() == Material.water) {
                if (player.motionY < 0) {
                    player.motionY = 0;
                    player.fallDistance = 0;
                    player.onGround = true;
                }
            }
        }
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        if (source == DamageSource.fall) {
            return new ArmorProperties(10, 100, 100);
        } else {
            return super.getProperties(player, armor, source, damage, slot);
        }
    }
}
