package ruby.bamboo.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemNinjaLeg extends ItemNinjaArmor {

    public ItemNinjaLeg(int slot) {
        super(slot);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (player.onGround) {
            player.motionX *= 1.05;
            player.motionZ *= 1.05;
        }
        if (player.isInWater()) {
            player.motionX *= 1.05;
            player.motionZ *= 1.05;
        }
    }
}
