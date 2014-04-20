package ruby.bamboo.item;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBambooPane extends ItemSimpleSubtype {
    public ItemBambooPane(Block block) {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return field_150939_a.getIcon(0, par1);
    }

}
