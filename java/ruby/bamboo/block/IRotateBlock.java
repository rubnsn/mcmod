package ruby.bamboo.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IRotateBlock {
    @SideOnly(Side.CLIENT)
    public int getRotateMeta(int meta);
}
