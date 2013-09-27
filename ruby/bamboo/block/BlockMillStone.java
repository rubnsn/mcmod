package ruby.bamboo.block;

import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import ruby.bamboo.CustomRenderHandler;
import ruby.bamboo.GuiHandler;
import ruby.bamboo.tileentity.TileEntityMillStone;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

public class BlockMillStone extends BlockContainer
{
    private static boolean keepFurnaceInventory = false;

    public BlockMillStone(int par1)
    {
        super(par1, Material.rock);
        setHardness(1.0F);
    }
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        par5EntityPlayer.openGui(BambooCore.getInstance(), GuiHandler.GUI_MILLSTONE, par1World, par2, par3, par4);
        return true;
        /*
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
        	par1World.setBlockMetadataWithNotify(par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4)==0?1:0, 3);
        	return true;
        }*/
    }
    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityMillStone();
    }
    @Override
    public int getRenderType()
    {
        return CustomRenderHandler.millStoneUID;
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
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        if (!keepFurnaceInventory)
        {
            TileEntityMillStone tileentity = (TileEntityMillStone)par1World.getBlockTileEntity(par2, par3, par4);

            if (tileentity != null)
            {
                for (int j1 = 0; j1 < tileentity.getSizeInventory(); ++j1)
                {
                    ItemStack itemstack = tileentity.getStackInSlot(j1);

                    if (itemstack != null)
                    {
                        float f = par1World.rand.nextFloat() * 0.8F + 0.1F;
                        float f1 = par1World.rand.nextFloat() * 0.8F + 0.1F;
                        float f2 = par1World.rand.nextFloat() * 0.8F + 0.1F;

                        while (itemstack.stackSize > 0)
                        {
                            int k1 = par1World.rand.nextInt(21) + 10;

                            if (k1 > itemstack.stackSize)
                            {
                                k1 = itemstack.stackSize;
                            }

                            itemstack.stackSize -= k1;
                            EntityItem entityitem = new EntityItem(par1World, (double)((float)par2 + f), (double)((float)par3 + f1), (double)((float)par4 + f2), new ItemStack(itemstack.itemID, k1, itemstack.getItemDamage()));

                            if (itemstack.hasTagCompound())
                            {
                                entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                            }

                            float f3 = 0.05F;
                            entityitem.motionX = (double)((float)par1World.rand.nextGaussian() * f3);
                            entityitem.motionY = (double)((float)par1World.rand.nextGaussian() * f3 + 0.2F);
                            entityitem.motionZ = (double)((float)par1World.rand.nextGaussian() * f3);
                            par1World.spawnEntityInWorld(entityitem);
                        }
                    }
                }

                par1World.func_96440_m(par2, par3, par4, par5);
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
    public static void updateBlockState(boolean grind, World worldObj, int xCoord, int yCoord, int zCoord)
    {
        TileEntity tileentity = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
        keepFurnaceInventory = true;

        if (grind)
        {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);
        }
        else
        {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);
        }

        keepFurnaceInventory = false;

        if (tileentity != null)
        {
            tileentity.validate();
            worldObj.setBlockTileEntity(xCoord, yCoord, zCoord, tileentity);
        }
    }
    @Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        return Container.calcRedstoneFromInventory((IInventory)par1World.getBlockTileEntity(par2, par3, par4));
    }
}
