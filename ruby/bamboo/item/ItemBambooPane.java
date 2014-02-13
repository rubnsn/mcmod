package ruby.bamboo.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBambooPane extends ItemBlock {
    public ItemBambooPane(Block block) {
        super(block);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public int getMetadata(int i) {
        return i;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return field_150939_a.getIcon(0, par1);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return this.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }
}
