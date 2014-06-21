package ruby.bamboo.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ruby.bamboo.block.BlockMultiBlock;
import ruby.bamboo.tileentity.TileEntityMultiBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMultiBlock extends ItemBlock {

    public ItemMultiBlock(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        boolean res = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
        if (res && !world.isRemote) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileEntityMultiBlock && ((BlockMultiBlock) this.field_150939_a).canUse(player)) {
                ((TileEntityMultiBlock) tile).setSlotLength((byte) stack.getItemDamage());
                if (stack.hasTagCompound()) {
                    ((TileEntityMultiBlock) tile).readFromSlotNBT(stack.stackTagCompound);
                }
            }
        }
        return res;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getSpriteNumber() {
        return 0;
    }

    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack) {
        StringBuffer stb = new StringBuffer(super.getItemStackDisplayName(par1ItemStack));
        stb.append(par1ItemStack.getItemDamage()).append("x");
        if (par1ItemStack.hasTagCompound()) {
            stb.append(" Copy");
        }
        return stb.toString();
    }
}
