package ruby.bamboo.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.block.IExOnBLockPlacedBy;

public class ItemRotateBlock extends ItemSimpleSubtype {

    public ItemRotateBlock(Block block) {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        boolean res = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
        if (res && field_150939_a instanceof IExOnBLockPlacedBy) {
            ((IExOnBLockPlacedBy) field_150939_a).onBlockPlacedBy(world, x, y, z, side, hitX, hitY, hitZ, player, stack, metadata);
        }
        return res;
    }
}
