package ruby.bamboo.item;

import ruby.bamboo.block.BlockCustomRenderSingleTexture;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemCustomRenderSingleTexture extends ItemBlock
{
    public ItemCustomRenderSingleTexture(int id)
    {
        super(id);
        setMaxDamage(0);
        setHasSubtypes(true);
    }
    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }
    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        int i = itemstack.getItemDamage();
        return ((BlockCustomRenderSingleTexture)(Block.blocksList[this.getBlockID()])).getUnlocalizedName(itemstack.getItemDamage());
    }
}
