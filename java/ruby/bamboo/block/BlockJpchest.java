package ruby.bamboo.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooUtil;
import ruby.bamboo.tileentity.TileEntityJPChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockJpchest extends BlockContainer {
    private IIcon front;
    private IIcon other;

    public BlockJpchest() {
        super(Material.wood);
        random = new Random();
        setStepSound(Block.soundTypeWood);
        setHardness(2.0F);
        setResistance(1.0F);
        this.setBlockBounds(0, 0, 0, 1, 1, 1);
    }

    @Override
    public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        int m = iblockaccess.getBlockMetadata(i, j, k);

        if (m == 0 && l == 2) {
            return front;
        } else if (m == 1 && l == 5) {
            return front;
        } else if (m == 2 && l == 3) {
            return front;
        } else if (m == 3 && l == 4) {
            return front;
        }

        return other;
    }

    @Override
    public IIcon getIcon(int i, int j) {
        if (i == 3) {
            return front;
        }

        return other;
    }

    @Override
    public int getRenderType() {
        return 0;
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
        TileEntityJPChest tileentitychest = (TileEntityJPChest) par1World.getTileEntity(par2, par3, par4);

        if (tileentitychest != null) {
            for (int l = 0; l < tileentitychest.getSizeInventory(); l++) {
                ItemStack itemstack = tileentitychest.getStackInSlot(l);

                if (itemstack == null) {
                    continue;
                }

                float f = random.nextFloat() * 0.8F + 0.1F;
                float f1 = random.nextFloat() * 0.8F + 0.1F;
                float f2 = random.nextFloat() * 0.8F + 0.1F;

                while (itemstack.stackSize > 0) {
                    int i1 = random.nextInt(21) + 10;

                    if (i1 > itemstack.stackSize) {
                        i1 = itemstack.stackSize;
                    }

                    itemstack.stackSize -= i1;
                    EntityItem entityitem = new EntityItem(par1World, par2 + f, par3 + f1, par4 + f2, new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage()));
                    float f3 = 0.05F;
                    entityitem.motionX = (float) random.nextGaussian() * f3;
                    entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
                    entityitem.motionZ = (float) random.nextGaussian() * f3;

                    if (itemstack.hasTagCompound()) {
                        entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                    }

                    par1World.spawnEntityInWorld(entityitem);
                }
            }
        }

        par1World.removeTileEntity(par2, par3, par4);
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block l) {
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (par5EntityPlayer.isSneaking()) {
            return false;
        }

        Object obj = par1World.getTileEntity(par2, par3, par4);

        if (obj == null) {
            return true;
        }

        par5EntityPlayer.displayGUIChest(((IInventory) (obj)));
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        return true;
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        par1World.setBlockMetadataWithNotify(par2, par3, par4, BambooUtil.getPlayerDir(par5EntityLivingBase), 3);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
        return Container.calcRedstoneFromInventory((IInventory) par1World.getTileEntity(par2, par3, par4));
    }

    private final Random random;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IIconRegister) {
        this.front = par1IIconRegister.registerIcon(BambooCore.resourceDomain + "jpchest_f");
        this.other = par1IIconRegister.registerIcon(BambooCore.resourceDomain + "jpchest_o");
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityJPChest();
    }
}
