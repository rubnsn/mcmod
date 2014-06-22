package ruby.bamboo.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IRotateBlock {
    @SideOnly(Side.CLIENT)
    public int getuvRotateSouth(int meta);

    @SideOnly(Side.CLIENT)
    public int getuvRotateEast(int meta);

    @SideOnly(Side.CLIENT)
    public int getuvRotateWest(int meta);

    @SideOnly(Side.CLIENT)
    public int getuvRotateNorth(int meta);

    @SideOnly(Side.CLIENT)
    public int getuvRotateTop(int meta);

    @SideOnly(Side.CLIENT)
    public int getuvRotateBottom(int meta);
}
