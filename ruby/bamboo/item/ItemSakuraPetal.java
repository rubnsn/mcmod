package ruby.bamboo.item;

import net.minecraft.item.ItemBlock;

public class ItemSakuraPetal extends ItemBlock
{
    public ItemSakuraPetal(int par1)
    {
        super(par1);
        setMaxDamage(0);
        setHasSubtypes(true);
    }
    @Override
    public int getMetadata(int i)
    {
        return i;
    }
}
