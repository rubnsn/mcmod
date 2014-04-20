package ruby.bamboo.block;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import ruby.bamboo.BambooInit;

public class BlockLiangBamboo extends BlockLiangBase {

    public BlockLiangBamboo(float minWidth, float maxWidth, float minHeight, float maxHeight) {
        super(minWidth, maxWidth, minHeight, maxHeight);
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        icons = new IIcon[] { BambooInit.sakuralog.getIcon(0, 1) };
    }

}
