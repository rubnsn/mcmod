package ruby.bamboo.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ruby.bamboo.CustomRenderHandler;
import ruby.bamboo.tileentity.TileEntityManeki;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockManeki extends BlockContainer {

    public BlockManeki(Material par2Material) {
        super(par2Material);
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
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon("wool_colored_red");
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityManeki();
    }
}
