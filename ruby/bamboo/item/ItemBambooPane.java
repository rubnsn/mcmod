package ruby.bamboo.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemBambooPane extends ItemBlock
{
    public ItemBambooPane(int par1)
    {
        super(par1);
        setHasSubtypes(true);
        setMaxDamage(0);
    }
    @Override
    public int getMetadata(int i)
    {
        return i;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1)
    {
        return Block.blocksList[this.getBlockID()].getIcon(0, par1);
    }
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return this.getUnlocalizedName() + par1ItemStack.getItemDamage();
    }
}
