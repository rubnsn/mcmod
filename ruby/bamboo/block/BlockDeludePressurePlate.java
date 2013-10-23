package ruby.bamboo.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.BambooCore;
import ruby.bamboo.CustomRenderHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.EnumMobType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class BlockDeludePressurePlate extends BlockPressurePlate implements
        IDelude {
    private boolean isIconGrass = false;

    public BlockDeludePressurePlate(int par1) {
        super(par1, BambooCore.resorceDmain + "delude", Material.ground, EnumMobType.everything);
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.deludeUID;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        Icon tex = getBlockTexture(par1IBlockAccess, par2, par3, par4, par5, 0);
        return tex != null ? tex : getDefaultIcon();
    }

    private Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5, int par6) {
        par3--;

        if (isDeludeBlock(par1IBlockAccess, par2, par3, par4)) {
            return par6 < BambooCore.getConf().deludeTexMaxReference ? getBlockTexture(par1IBlockAccess, par2, par3, par4, par5, par6 + 1) : getDefaultIcon();
        } else {
            return Block.blocksList[par1IBlockAccess.getBlockId(par2, par3, par4)] != null && par1IBlockAccess.getBlockMaterial(par2, par3, par4) != Material.water ? Block.blocksList[par1IBlockAccess.getBlockId(par2, par3, par4)].getBlockTexture(par1IBlockAccess, par2, par3, par4, par5) : getDefaultIcon();
        }
    }

    private Icon getDefaultIcon() {
        return blockIcon;
    }

    private boolean isDeludeBlock(IBlockAccess iba, int x, int y, int z) {
        return Block.blocksList[iba.getBlockId(x, y, z)] instanceof IDelude;
    }

    @Override
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        return colorMultiplier(par1IBlockAccess, par2, par3, par4, 0);
    }

    private int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        par3--;

        if (isDeludeBlock(par1IBlockAccess, par2, par3, par4)) {
            return par5 < BambooCore.getConf().deludeTexMaxReference ? colorMultiplier(par1IBlockAccess, par2, par3, par4, par5 + 1) : 0xFFFFFF;
        } else {
            return Block.blocksList[par1IBlockAccess.getBlockId(par2, par3, par4)] != null && par1IBlockAccess.getBlockMaterial(par2, par3, par4) != Material.water ? Block.blocksList[par1IBlockAccess.getBlockId(par2, par3, par4)].colorMultiplier(par1IBlockAccess, par2, par3, par4) : 0xFFFFFF;
        }
    }

    @Override
    public void setIconGrass(boolean bool) {
        isIconGrass = bool;
    }

    @Override
    public int getOriginalRenderType() {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2) {
        return !isIconGrass ? getDefaultIcon() : Block.grass.getIcon(par1, par2);
    }
}
