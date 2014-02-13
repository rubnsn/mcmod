package ruby.bamboo.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import ruby.bamboo.BambooCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTudura extends Item {
    public ItemTudura() {
        super();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(BambooCore.resourceDomain + "tudura");
    }
}
