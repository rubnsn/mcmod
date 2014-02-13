package ruby.bamboo.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.CustomRenderHandler;
import ruby.bamboo.ManekiHandler;
import ruby.bamboo.tileentity.TileEntityManeki;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockManeki extends BlockContainer {
    public ManekiHandler maneki = ManekiHandler.instance;

    public BlockManeki(int par1, Material par2Material) {
        super(par1, par2Material);
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        int meta = 0;

        switch (MathHelper.floor_double(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) {
        case 0:
            meta = 2;
            break;

        case 1:
            meta = 1;
            break;

        case 2:
            meta = 0;
            break;

        case 3:
            meta = 3;
            break;
        }

        par1World.setBlockMetadataWithNotify(par2, par3, par4, meta, 3);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int posX, int posY, int posZ) {
        return world.provider.dimensionId == 0;
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.manekiUID;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityManeki();
    }

    public boolean addManeki(int posX, int posZ) {
        return maneki.addManeki(posX, posZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon("wool_colored_red");
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
        if (!((TileEntityManeki) par1World.getBlockTileEntity(par2, par3, par4)).isDestry) {
            maneki.removeManeki(par2, par4);
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
}
