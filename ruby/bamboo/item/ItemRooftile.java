package ruby.bamboo.item;

import net.minecraft.item.ItemBlock;

public class ItemRooftile extends ItemBlock
{
    public ItemRooftile(int i)
    {
        super(i);
        // TODO 自動生成されたコンストラクター・スタブ
        setMaxDamage(0);
        setHasSubtypes(true);
    }
    @Override
    public int getMetadata(int i)
    {
        return i;
    }
}
