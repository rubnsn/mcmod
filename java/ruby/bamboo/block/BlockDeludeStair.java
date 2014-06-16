package ruby.bamboo.block;

import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import ruby.bamboo.CustomRenderHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDeludeStair extends BlockStairs implements IDelude {
    private boolean isIcongrass = false;

    public BlockDeludeStair() {
        super(BambooInit.delude_width, 0);
        useNeighborBrightness = true;
        this.setLightOpacity(0);
    }

    @Override
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
        if (par5 == 1) {
            return 8;
        } else if (par5 == 0) {
            return 12;
        }

        return super.onBlockPlaced(par1World, par2, par3, par4, par5, par6, par7, par8, par9);
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        if (par1World.getBlockMetadata(par2, par3, par4) == 8) {
            super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLivingBase, par6ItemStack);
            par1World.setBlockMetadataWithNotify(par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4) | 8, 3);
        } else if (par1World.getBlockMetadata(par2, par3, par4) == 12) {
            super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLivingBase, par6ItemStack);
            par1World.setBlockMetadataWithNotify(par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4) | 12, 3);
        } else {
            super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLivingBase, par6ItemStack);
        }
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
    }

    @Override
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        return BambooInit.delude_width.colorMultiplier(par1IBlockAccess, par2, par3, par4);
    }

    @Override
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return BambooInit.delude_width.getIcon(par1IBlockAccess, par2, par3, par4, par5);
    }

    private IIcon getDefaultIcon() {
        return blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(BambooCore.resourceDomain + "delude");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        return !isIcongrass ? getDefaultIcon() : Blocks.grass.getIcon(par1, par2);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        return BambooInit.delude_width.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return AxisAlignedBB.getBoundingBox(par2, par3, par4, (double) par2 + 1, (double) par3 + 1, (double) par4 + 1);
    }

    @Override
    public void setIconGrass(boolean bool) {
        isIcongrass = bool;
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.deludeUID;
    }

    @Override
    public int getOriginalRenderType() {
        return 10;
    }
}
