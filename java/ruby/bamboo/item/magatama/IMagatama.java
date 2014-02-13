package ruby.bamboo.item.magatama;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IMagatama {
    int getColor();

    Class getEffectClass();

    int getReality();

    void holdingEffect(Entity entity, int invIndex);

    void useItem(World world, ItemStack itemStack, EntityPlayer entityPlayer);

    boolean isDecrease();
}
