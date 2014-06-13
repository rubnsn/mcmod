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
import ruby.bamboo.Config;
import ruby.bamboo.CustomRenderHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDeludeStair extends BlockStairs implements IDelude {
    private boolean isIcongrass = false;

    public BlockDeludeStair() {
        super(BambooInit.delude_width, 0);
        useNeighborBrightness = true;
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
        return colorMultiplier(par1IBlockAccess, par2, par3, par4, 0);
    }

    private int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int loop) {
        switch (getBlockMetadata(par1IBlockAccess, par2, par3, par4)) {
        case 0:
            par2++;
            break;

        case 1:
            par2--;
            break;

        case 2:
            par4++;
            break;

        case 3:
            par4--;
            break;

        case 4:
            par3++;
            break;

        case 5:
            par3--;
            break;
        }

        if (isDeludeBlock(par1IBlockAccess, par2, par3, par4)) {
            return loop < Config.deludeTexMaxReference ? colorMultiplier(par1IBlockAccess, par2, par3, par4, loop + 1) : 0xFFFFFF;
        } else {
            return par1IBlockAccess.getBlock(par2, par3, par4) != null ? par1IBlockAccess.getBlock(par2, par3, par4).colorMultiplier(par1IBlockAccess, par2, par3, par4) : 0xFFFFFF;
        }
    }

    @Override
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        IIcon tex = getIcon(par1IBlockAccess, par2, par3, par4, par5, 0);
        return tex != null ? (!tex.getIconName().equals("water") && !tex.getIconName().equals("water_flow")) ? tex : getDefaultIcon() : getDefaultIcon();
    }

    private IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5, int loop) {
        switch (getBlockMetadata(par1IBlockAccess, par2, par3, par4)) {
        case 0:
            par2++;
            break;

        case 1:
            par2--;
            break;

        case 2:
            par4++;
            break;

        case 3:
            par4--;
            break;

        case 4:
            par3++;
            break;

        case 5:
            par3--;
            break;
        }

        if (isDeludeBlock(par1IBlockAccess, par2, par3, par4)) {
            return loop < Config.deludeTexMaxReference ? getIcon(par1IBlockAccess, par2, par3, par4, par5, loop + 1) : getDefaultIcon();
        } else {
            return par1IBlockAccess.getBlock(par2, par3, par4) != null ? par1IBlockAccess.getBlock(par2, par3, par4).getIcon(par1IBlockAccess, par2, par3, par4, par5) : getDefaultIcon();
        }
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
        return onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9, 0);
    }

    private boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9, int loop) {
        switch (getBlockMetadata(par1World, par2, par3, par4)) {
        case 0:
            par2++;
            break;

        case 1:
            par2--;
            break;

        case 2:
            par4++;
            break;

        case 3:
            par4--;
            break;

        case 4:
            par3++;
            break;

        case 5:
            par3--;
            break;
        }

        if (isDeludeBlock(par1World, par2, par3, par4)) {
            return loop < Config.deludeMaxReference ? onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9, loop + 1) : false;
        } else {
            return par1World.getBlock(par2, par3, par4) != null ? par1World.getBlock(par2, par3, par4).onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9) : false;
        }
    }

    private int getBlockMetadata(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        if (par1IBlockAccess.getBlock(par2, par3, par4) != this && isDeludeBlock(par1IBlockAccess, par2, par3, par4)) {
            return par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 7;
        } else {
            int meta = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
            return (meta & 8) != 8 ? meta & 3 : (meta & 12) == 8 ? 5 : 4;
        }
    }

    private boolean isDeludeBlock(IBlockAccess iba, int x, int y, int z) {
        return iba.getBlock(x, y, z) instanceof IDelude;
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
