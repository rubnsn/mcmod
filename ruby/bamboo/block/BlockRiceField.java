package ruby.bamboo.block;

import java.util.Random;

import ruby.bamboo.BambooInit;
import ruby.bamboo.CustomRenderHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class BlockRiceField extends BlockFluid {
    public final static Material riceField = new Material(MapColor.waterColor);

    public BlockRiceField(int par1, Material par2Material) {
        super(par1, riceField);
        setBlockBounds(0F, 0F, 0F, 1.0F, 0.5F, 1.0F);
        this.setLightOpacity(0);
        setHardness(0.5F);
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
    public boolean canCollideCheck(int par1, boolean par2) {
        return this.isCollidable();
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 3);
    }

    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        int meta = par1World.getBlockMetadata(par2, par3, par4);

        if (meta != 0) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, meta - 1, 3);
        }
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        /*
         * meta=meta<3?meta+1:0; System.out.println(meta);
         */
        if (par5EntityPlayer.getCurrentEquippedItem() != null && par5EntityPlayer.getCurrentEquippedItem().getItem() == Item.bucketWater) {
            if (!par1World.isRemote) {
                boolean flag = false;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int meta = par1World.getBlockMetadata(par2 + i, par3, par4 + j);

                        if ((par1World.getBlockId(par2, par3, par4) == this.blockID) && meta > 0) {
                            par1World.setBlockMetadataWithNotify(par2 + i, par3, par4 + j, meta - 1, 3);
                            flag = true;
                        }
                    }
                }

                if (flag) {
                    if (!par5EntityPlayer.capabilities.isCreativeMode) {
                        par5EntityPlayer.getCurrentEquippedItem().stackSize--;

                        if (!par5EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Item.bucketEmpty))) {
                            par5EntityPlayer.dropPlayerItem(new ItemStack(Item.bucketEmpty.itemID, 1, 0));
                        }
                    }
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
        return plant.getPlantID(world, x, y + 1, z) == BambooInit.ricePlantBID ? true : false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return AxisAlignedBB.getAABBPool().getAABB(par2, par3, par4, (double) par2 + 1, (double) par3 + 0.5F, (double) par4 + 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        boolean flg = super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
        return flg;
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.riceFieldUID;
    }

    public static int renderPass;

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderInPass(int pass) {
        renderPass = pass;
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        return Block.tilledField.getIcon(side, meta);
    }

    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
        return Block.dirt.blockID;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 1;
    }
}
