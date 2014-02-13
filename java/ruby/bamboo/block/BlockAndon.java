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
import ruby.bamboo.tileentity.TileEntityAndon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAndon extends BlockContainer {
    public BlockAndon() {
        super(Material.glass);
        setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.9F, 0.8F);
        setLightLevel(0.90F);
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.andonUID;
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        int var6 = MathHelper.floor_double(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, var6, 3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon("wool_colored_white");
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
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityAndon();
    }
}
