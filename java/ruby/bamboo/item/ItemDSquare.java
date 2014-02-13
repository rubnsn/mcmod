package ruby.bamboo.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemDSquare extends ItemBlock {
    public ItemDSquare(int i) {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int i) {
        return i;
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }
}
