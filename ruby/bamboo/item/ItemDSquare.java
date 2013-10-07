package ruby.bamboo.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemDSquare extends ItemBlock
{
    public ItemDSquare(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
    }
    @Override
    public int getMetadata(int i)
    {
        return i;
    }
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int i = par1ItemStack.getItemDamage();

        if (i == 12)
        {
            return super.getUnlocalizedName() + "." + "Tatami(NonBorder)";
        }
        else if (i == 10)
        {
            return super.getUnlocalizedName() + "." + "Tatami(NonBorder+Tan)";
        }
        else if (i == 8)
        {
            return super.getUnlocalizedName() + "." + "Tatami(Tan)";
        }
        else if (i == 4)
        {
            return super.getUnlocalizedName() + "." + "Kayabuki";
        }
        else
        {
            return super.getUnlocalizedName() + "." + "Tatami";
        }
    }
}
