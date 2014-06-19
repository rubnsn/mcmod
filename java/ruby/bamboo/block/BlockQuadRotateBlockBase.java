package ruby.bamboo.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ruby.bamboo.BambooUtil;
import ruby.bamboo.CustomRenderHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockQuadRotateBlockBase extends Block implements
        IRotateBlock {
    //1ブロック辺り4個まで!
    IIcon[] icons;

    public BlockQuadRotateBlockBase(Material p_i45394_1_) {
        super(p_i45394_1_);
        this.setHardness(0.2F);
        this.setResistance(1.0F);
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        int j1 = par1World.getBlockMetadata(par2, par3, par4) & 3;
        byte dir = 0;
        switch (BambooUtil.getPlayerDir(par5EntityLivingBase)) {
        case 0:
            dir = 3;
            break;
        case 1:
            dir = 2;
            break;
        case 2:
            dir = 0;
            break;
        case 3:
            dir = 1;
            break;
        default:
            break;
        }
        par1World.setBlockMetadataWithNotify(par2, par3, par4, j1 | (dir << getDirShiftBit()), 3);
    }

    @Override
    public int damageDropped(int meta) {
        return meta & getMetaMask();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side_, int meta) {
        return icons[(meta & getMetaMask()) % icons.length];
    }

    public int getMetaMask() {
        return 3;
    }

    public int getDirShiftBit() {
        return 2;
    }

    @Override
    public int getRotateMeta(int meta) {
        return meta >> getDirShiftBit();
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.quadRotatedPillarUID;
    }

}
