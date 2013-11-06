package ruby.bamboo.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ruby.bamboo.BambooInit;
import ruby.bamboo.tileentity.ITileEntitySpa;
import ruby.bamboo.tileentity.TileEntitySpaChild;
import ruby.bamboo.tileentity.TileEntitySpaParent;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockSpaUnit extends BlockContainer implements ITileEntityProvider {

    public BlockSpaUnit(int par1) {
        super(par1, Material.ground);
        setTickRandomly(true);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (par5EntityPlayer.isSneaking()) {
            return false;
        }

        par5EntityPlayer.swingItem();
        int meta = par1World.getBlockMetadata(par2, par3, par4);
        meta = (meta & 8) == 8 ? 0 : 8;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, meta, 3);
        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon("wool_colored_white");
    }

    @Override
    public Icon getIcon(int side, int meta) {
        if (side == 1) {
            if ((meta & 8) > 0) {
                return BlockPistonBase.getPistonBaseIcon("piston_inner");
            } else {
                return BlockPistonBase.getPistonBaseIcon("piston_top_normal");
            }
        } else {
            return Block.pistonBase.getIcon(1, 0);
        }
    }

    @Override
    public void updateTick(World world, int posX, int posY, int posZ, Random random) {
        boolean isEnable = (world.getBlockMetadata(posX, posY, posZ) & 8) != 0;

        if (world.isBlockIndirectlyGettingPowered(posX, posY - 1, posZ) || isEnable) {
            if (world.isAirBlock(posX, posY + 1, posZ)) {
                this.setThisChildBlock(world, posX, posY, posZ, ForgeDirection.UP);
                ((TileEntitySpaParent) world.getBlockTileEntity(posX, posY, posZ)).setStay(true);
            } else if (world.getBlockId(posX, posY + 1, posZ) == BambooInit.spaBID && world.getBlockMetadata(posX, posY + 1, posZ) > 0) {
                world.setBlockMetadataWithNotify(posX, posY + 1, posZ, world.getBlockMetadata(posX, posY + 1, posZ) - 1, 3);
            }
        } else {
            if (world.getBlockTileEntity(posX, posY + 1, posZ) != null) {
                ((TileEntitySpaParent) world.getBlockTileEntity(posX, posY, posZ)).setStay(false);
                world.markBlockForUpdate(posX, posY, posZ);
            }
        }

        world.setBlockMetadataWithNotify(posX, posY, posZ, isEnable ? 8 : 0, 0);

        if (world.getBlockId(posX, posY + 1, posZ) == BambooInit.spaBID) {
            world.scheduleBlockUpdate(posX, posY, posZ, this.blockID, this.tickRate(world));
        }
    }

    private void setThisChildBlock(World world, int posX, int posY, int posZ, ForgeDirection dir) {
        world.setBlock(posX + dir.offsetX, posY + dir.offsetY, posZ + dir.offsetZ, BambooInit.spaBID, 7, 3);
        ((TileEntitySpaChild) world.getBlockTileEntity(posX + dir.offsetX, posY + dir.offsetY, posZ + dir.offsetZ)).setParentPosition(((ITileEntitySpa) world.getBlockTileEntity(posX, posY, posZ)).getParentPosition());
    }

    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
        return this.blockID;
    }

    @Override
    public int tickRate(World world) {
        return 30;
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        // TODO 自動生成されたメソッド・スタブ
        return new TileEntitySpaParent();
    }
}
