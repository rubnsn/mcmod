package ruby.bamboo.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import ruby.bamboo.CustomRenderHandler;
import ruby.bamboo.gui.GuiHandler;
import ruby.bamboo.tileentity.TileEntityCampfire;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCampfire extends BlockContainer {
    private static boolean keepFurnaceInventory = false;

    public BlockCampfire() {
        super(Material.ground);
        setLightLevel(0.9F);
        setHardness(1.0F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {

        TileEntityCampfire tile = (TileEntityCampfire) world.getTileEntity(x, y, z);

        if (tile != null) {
            player.openGui(BambooCore.instance, GuiHandler.GUI_CAMPFIRE, world, x, y, z);
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityCampfire();
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.campfireUID;
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
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        float var7 = par2 + 0.5F;
        float var8 = par3 + 0.2F;
        float var9 = par4 + 0.5F;
        float var10 = par5Random.nextFloat() * 0.4F - 0.2F;
        float var11 = par5Random.nextFloat() * 0.4F - 0.2F;
        par1World.spawnParticle("smoke", var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
        par1World.spawnParticle("flame", var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        int var6 = MathHelper.floor_double(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, var6, 3);
    }

    public static void updateFurnaceBlockState(World par1World, int par2, int par3, int par4) {
        int var5 = par1World.getBlockMetadata(par2, par3, par4);
        TileEntity var6 = par1World.getTileEntity(par2, par3, par4);
        keepFurnaceInventory = true;
        par1World.setBlock(par2, par3, par4, BambooInit.campfire, 0, 3);
        keepFurnaceInventory = false;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, var5, 3);

        if (var6 != null) {
            var6.validate();
            par1World.setTileEntity(par2, par3, par4, var6);
        }
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
        if (!keepFurnaceInventory) {
            par1World.removeTileEntity(par2, par3, par4);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return AxisAlignedBB.getBoundingBox(par2, par3, par4, par2, par3, par4);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemFromBlock(BambooInit.campfire);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(BambooInit.campfire);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon("wool_colored_black");
    }
}
