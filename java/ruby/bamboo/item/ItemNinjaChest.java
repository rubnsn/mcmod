package ruby.bamboo.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

public class ItemNinjaChest extends ItemNinjaArmor {
    public ItemNinjaChest(int slot) {
        super(slot);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (player.isPotionActive(Potion.poison)) {
            player.removePotionEffect(Potion.poison.id);
        }
        if (player.isSneaking() && player.onGround && isEqNinjaArmor(player, LEG)) {
            player.motionX *= 1.5;
            player.motionZ *= 1.5;
        }

    }
}
