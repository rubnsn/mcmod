package ruby.bamboo.block;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IExOnBLockPlacedBy {
    public void onBlockPlacedBy(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, EntityLivingBase entity, ItemStack itemStack, int meta);
}
