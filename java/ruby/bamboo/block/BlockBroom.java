package ruby.bamboo.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import ruby.bamboo.BambooCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBroom extends BlockQuadRotatePillar {

    public static final String[] ICON_NAME = new String[] { "broom_straight", "broom_curve" };

    public BlockBroom() {
        super(Material.sand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        icons = new IIcon[ICON_NAME.length];
        for (int i = 0; i < this.ICON_NAME.length; ++i) {
            this.icons[i] = p_149651_1_.registerIcon(BambooCore.resourceDomain + ICON_NAME[i]);
        }
    }

}
