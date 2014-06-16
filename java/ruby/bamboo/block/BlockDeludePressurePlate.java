package ruby.bamboo.block;

import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import ruby.bamboo.BambooCore;
import ruby.bamboo.Config;
import ruby.bamboo.CustomRenderHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDeludePressurePlate extends BlockPressurePlate implements
        IDelude {
    private boolean isIconGrass = false;

    public BlockDeludePressurePlate() {
        super(BambooCore.resourceDomain + "delude", Material.ground, BlockPressurePlate.Sensitivity.everything);
        this.setHardness(0.1F);
        this.setResistance(1.0F);
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.deludeUID;
    }

    @Override
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        IIcon tex = getBlockTexture(par1IBlockAccess, par2, par3, par4, par5, 0);
        return tex != null ? tex : getDefaultIcon();
    }

    private IIcon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5, int par6) {
        par3--;

        if (isDeludeBlock(par1IBlockAccess, par2, par3, par4)) {
            return par6 < Config.deludeTexMaxReference ? getBlockTexture(par1IBlockAccess, par2, par3, par4, par5, par6 + 1) : getDefaultIcon();
        } else {
            return par1IBlockAccess.getBlock(par2, par3, par4) != null && par1IBlockAccess.getBlock(par2, par3, par4).getMaterial() != Material.water ? par1IBlockAccess.getBlock(par2, par3, par4).getIcon(par1IBlockAccess, par2, par3, par4, par5) : getDefaultIcon();
        }
    }

    private IIcon getDefaultIcon() {
        return blockIcon;
    }

    private boolean isDeludeBlock(IBlockAccess iba, int x, int y, int z) {
        return iba.getBlock(x, y, z) instanceof IDelude;
    }

    @Override
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        return colorMultiplier(par1IBlockAccess, par2, par3, par4, 0);
    }

    private int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        par3--;

        if (isDeludeBlock(par1IBlockAccess, par2, par3, par4)) {
            return par5 < Config.deludeTexMaxReference ? colorMultiplier(par1IBlockAccess, par2, par3, par4, par5 + 1) : 0xFFFFFF;
        } else {
            return par1IBlockAccess.getBlock(par2, par3, par4) != null && par1IBlockAccess.getBlock(par2, par3, par4).getMaterial() != Material.water ? par1IBlockAccess.getBlock(par2, par3, par4).colorMultiplier(par1IBlockAccess, par2, par3, par4) : 0xFFFFFF;
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
    public IIcon getIcon(int par1, int par2) {
        return !isIconGrass ? getDefaultIcon() : Blocks.grass.getIcon(par1, par2);
    }
}
