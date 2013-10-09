package ruby.bamboo.item;

import ruby.bamboo.block.BlockPillar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPillar extends ItemBlock {
    public ItemPillar(int par1) {
        super(par1);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        return BlockPillar.canBlockPlace(world, x, y, z, side) && super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
    }
}
