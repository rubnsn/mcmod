package ruby.bamboo.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.block.BlockDecorations;
import ruby.bamboo.block.BlockTwoDirections;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemDecorationBlocks extends ItemBlock
{
    public ItemDecorationBlocks(int par1)
    {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    @Override
    public int getMetadata(int meta)
    {
        return meta & 7;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        int i = itemstack.getItemDamage();
        return Block.blocksList[this.getBlockID()].getUnlocalizedName() + i;
    }
}
