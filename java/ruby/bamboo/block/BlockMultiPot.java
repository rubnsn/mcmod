package ruby.bamboo.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.CustomRenderHandler;
import ruby.bamboo.tileentity.TileEntityMultiPot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMultiPot extends BlockContainer {

    public BlockMultiPot() {
        super(Material.circuits);
        setBlockBounds(0, 0, 0, 1, 0.375F, 1);
        setHardness(1F);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float f = 0.375F;
        float f1 = f / 2.0F;
        this.setBlockBounds(0.5F - f1, 0.0F, 0.5F - f1, 0.5F + f1, f, 0.5F + f1);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        ItemStack currentItem = par5EntityPlayer.getCurrentEquippedItem();
        TileEntityMultiPot tile = (TileEntityMultiPot) par1World.getTileEntity(par2, par3, par4);
        int hitNum = tile.getSlotPositionNumber(par7, par9);
        if (currentItem == null) {
            if (tile.getFlowerPotItem(hitNum) != null) {
                if (!par5EntityPlayer.capabilities.isCreativeMode) {
                    this.dropBlockAsItem(par1World, par2, par3, par4, tile.removeItemOnSlotNumber(hitNum));
                }
            } else {
                this.removeSlot(par1World, tile, par2, par3, par4, hitNum, par5EntityPlayer);
            }
        } else {
            if (currentItem.getItem() == Item.getItemFromBlock(this)) {
                if (!tile.isEnable(hitNum)) {
                    tile.addSlot(hitNum);
                    if (!par5EntityPlayer.capabilities.isCreativeMode) {
                        currentItem.stackSize--;
                    }
                } else {
                    this.removeSlot(par1World, tile, par2, par3, par4, hitNum, par5EntityPlayer);
                }
            } else {
                Block block = Block.getBlockFromItem(currentItem.getItem());
                if (tile.isEnable(hitNum)) {
                    if (block.getRenderType() == 1 || block.getRenderType() == 13 || block.getRenderType() == 40 || block.getRenderType() == CustomRenderHandler.coordinateCrossUID) {
                        if (tile.getFlowerPotItem(hitNum) == null) {
                            tile.setItemOnSlotNumber(hitNum, currentItem.getItem(), currentItem.getItemDamage());
                            if (!par5EntityPlayer.capabilities.isCreativeMode) {
                                currentItem.stackSize--;
                            }
                        } else {
                            ItemStack is = tile.removeItemOnSlotNumber(hitNum);
                            if (!par5EntityPlayer.capabilities.isCreativeMode) {
                                this.dropBlockAsItem(par1World, par2, par3, par4, is);
                            }
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        par1World.markBlockForUpdate(par2, par3, par4);
        return true;
    }

    private void removeSlot(World par1World, TileEntityMultiPot tile, int par2, int par3, int par4, int hitNum, EntityPlayer player) {
        if (tile.isEnable(hitNum)) {
            ItemStack res = tile.removeSlot(hitNum);
            if (!par1World.isRemote) {
                if (!player.capabilities.isCreativeMode) {
                    if (res != null) {
                        this.dropBlockAsItem(par1World, par2, par3, par4, res);
                    }
                    this.dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(Item.getItemFromBlock(this)));
                }
                if (tile.isEmpty()) {
                    par1World.func_147480_a(par2, par3, par4, true);
                }
            }
        }
    }

    @Override
    public void breakBlock(World world, int posX, int posY, int posZ, Block block, int meta) {
        ((TileEntityMultiPot) world.getTileEntity(posX, posY, posZ)).dropAllItems(world);
        super.breakBlock(world, posX, posY, posZ, block, meta);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return null;
    }

    @Override
    public void dropBlockAsItem(World world, int posX, int posY, int posZ, ItemStack itemStack) {
        super.dropBlockAsItem(world, posX, posY, posZ, itemStack);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityMultiPot();
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.multiPotUID;
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
    @SideOnly(Side.CLIENT)
    public String getItemIconName() {
        return BambooCore.resourceDomain + "flower_pot";
    }
}
