package ruby.bamboo.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ruby.bamboo.BambooInit;
import ruby.bamboo.CustomRenderHandler;
import ruby.bamboo.tileentity.TileEntityCampfire;
import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCampfire extends BlockFurnace
{
    private static boolean keepFurnaceInventory = false;
    public BlockCampfire(int par1)
    {
        super(par1, true);
        setLightValue(1F);
        setHardness(1.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityCampfire();
    }
    @Override
    public int getRenderType()
    {
        return CustomRenderHandler.campfireUID;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        float var7 = par2 + 0.5F;
        float var8 = par3 + 0.2F;
        float var9 = par4 + 0.5F;
        float var10 = par5Random.nextFloat() * 0.4F - 0.2F;
        float var11 = par5Random.nextFloat() * 0.4F - 0.2F;
        par1World.spawnParticle("smoke", var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
        par1World.spawnParticle("flame", var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
    }
    @Override

    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        int var6 = MathHelper.floor_double(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, var6, 3);
    }
    public static void updateFurnaceBlockState(World par1World, int par2, int par3, int par4)
    {
        int var5 = par1World.getBlockMetadata(par2, par3, par4);
        TileEntity var6 = par1World.getBlockTileEntity(par2, par3, par4);
        keepFurnaceInventory = true;
        par1World.setBlock(par2, par3, par4, BambooInit.campfireBID, 0, 3);
        keepFurnaceInventory = false;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, var5, 3);

        if (var6 != null)
        {
            var6.validate();
            par1World.setBlockTileEntity(par2, par3, par4, var6);
        }
    }
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        if (!keepFurnaceInventory)
        {
            super.breakBlock(par1World, par2, par3, par4, par5, par6);
        }
    }
    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return this.blockID;
    }

    @Override
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return this.blockID;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return AxisAlignedBB.getAABBPool().getAABB(par2, par3, par4, par2, par3, par4);
    }
}
