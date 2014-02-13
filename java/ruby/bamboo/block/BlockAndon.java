package ruby.bamboo.block;

import ruby.bamboo.CustomRenderHandler;
import ruby.bamboo.tileentity.TileEntityAndon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockAndon extends BlockContainer {
    public BlockAndon(int i) {
        super(i, Material.glass);
        setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.9F, 0.8F);
        setLightValue(0.90F);
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
    public void registerIcons(IconRegister par1IconRegister) {
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
    public TileEntity createNewTileEntity(World world) {
        // TODO 自動生成されたメソッド・スタブ
        return new TileEntityAndon();
    }
}
