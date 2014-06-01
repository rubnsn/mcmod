package ruby.bamboo.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.tileentity.TileEntityMultiPot;

public class ItemMultiPot extends ItemBlock {

    public ItemMultiPot(Block p_i45329_1_) {
        super(p_i45329_1_);
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        boolean res = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
        if (res) {
            ((TileEntityMultiPot) world.getTileEntity(x, y, z)).addSlot(TileEntityMultiPot.getSlotPositionNumber(hitX, hitZ));
        }
        return res;
    }
}
