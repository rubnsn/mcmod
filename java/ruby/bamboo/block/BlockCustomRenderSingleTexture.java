package ruby.bamboo.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.CustomRenderHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCustomRenderSingleTexture extends Block {
    private IIcon bamboo;

    public String getUnlocalizedName(int meta) {
        return "bamboo";
    }

    public BlockCustomRenderSingleTexture() {
        super(Material.ground);
        setHardness(0.2F);
        setResistance(0.0F);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return bamboo;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.bamboo = par1IconRegister.registerIcon(BambooCore.resourceDomain + "bamboo");
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
    public int getRenderType() {
        return CustomRenderHandler.bambooBlockUID;
    }

    @Override
    public int damageDropped(int i) {
        return 0;// i;
    }
}
