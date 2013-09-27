package ruby.bamboo.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ruby.bamboo.BambooInit;
import ruby.bamboo.Config;
import ruby.bamboo.BambooCore;
import ruby.bamboo.tileentity.TileEntitySpa;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockSpaUnit extends Block
{
    private int topTex;
    private int otherTex;
    public BlockSpaUnit(int par1)
    {
        super(par1, Material.ground);
        setTickRandomly(true);
    }
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par5EntityPlayer.isSneaking())
        {
            return false;
        }

        par5EntityPlayer.swingItem();
        int time = par1World.getBlockMetadata(par2, par3, par4);
        time = (time & 8) == 8 ? 0 : 15;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, time, 3);
        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
        return true;
    }
    public BlockSpaUnit setTopTex(int i)
    {
        topTex = i;
        return this;
    }
    public BlockSpaUnit setOtherTex(int i)
    {
        otherTex = i;
        return this;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("wool_colored_white");
    }
    @Override
    public Icon getIcon(int side, int meta)
    {
        if (side == 1)
        {
            if ((meta & 8) > 0)
            {
                return Block.pistonBase.getPistonBaseIcon("piston_inner");
            }
            else
            {
                return Block.pistonBase.getPistonBaseIcon("piston_top_normal");
            }
        }
        else
        {
            return Block.pistonBase.getIcon(1, 0);
        }
    }
    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        boolean flg = (world.getBlockMetadata(i, j, k) & 8) != 0;
        int x = i;
        int y = j;
        int z = k;

        if (world.isBlockIndirectlyGettingPowered(x, y - 1, z) || flg)
        {
            if (world.isAirBlock(x, y + 1, z))
            {
                world.setBlock(x, y + 1, z, BambooInit.spaBID, 6, 3);
                world.setBlockTileEntity(x, y + 1, z, new TileEntitySpa());
                ((TileEntitySpa)world.getBlockTileEntity(x, y + 1, z)).setLocation(x, y + 1, z);
                ((TileEntitySpa)world.getBlockTileEntity(x, y + 1, z)).setStay(true);
            }
            else if (world.getBlockId(x, y + 1, z) == BambooInit.spaBID && world.getBlockMetadata(x, y + 1, z) > 0)
            {
                world.setBlockMetadataWithNotify(x, y + 1, z, world.getBlockMetadata(x, y + 1, z) - 1, 3);
            }
        }
        else
        {
            if (world.getBlockTileEntity(x, y + 1, z) != null)
            {
                ((TileEntitySpa)world.getBlockTileEntity(x, y + 1, z)).setStay(false);
            }
        }

        world.setBlockMetadataWithNotify(i, j, k, flg ? 8 : 0, 0);

        if (world.getBlockId(i, j + 1, k) == BambooInit.spaBID)
        {
            world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
        }/*else{

        	world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world)+300);
        }*/
    }
    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return this.blockID;
    }
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        if (par1World.getBlockId(par2, par3 + 1, par4) == BambooInit.spaBID)
        {
            ((TileEntitySpa)par1World.getBlockTileEntity(par2, par3 + 1, par4)).setStay(false);
        }
    }
    @Override
    public int tickRate(World world)
    {
        return 60;
    }
}
