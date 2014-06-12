package ruby.bamboo.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.tileentity.TileEntityMultiBlock;

public class ItemMultiBlock extends ItemBlock {

    public ItemMultiBlock(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        boolean res = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
        if (res) {
            ((TileEntityMultiBlock) world.getTileEntity(x, y, z)).setSlotLength((byte) stack.getItemDamage());
        }
        return res;
    }

}
