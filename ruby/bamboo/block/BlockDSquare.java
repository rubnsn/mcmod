package ruby.bamboo.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ruby.bamboo.BambooInit;
import ruby.bamboo.BambooUtil;
import ruby.bamboo.Config;
import ruby.bamboo.BambooCore;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockDSquare extends Block
{
    private boolean isHalf;
    private String[] texNameList;
    private Icon[] icons;
    public BlockDSquare(int itemID , boolean isHalf)
    {
        super(itemID, Material.ground);
        //texList=new ArrayList<Integer>();
        this.isHalf = isHalf;
        icons = new Icon[16];

        if (isHalf)
        {
            setBlockBounds(0, 0, 0, 1, 0.5F, 1);
        }

        setStepSound(Block.soundSandFootstep);
        setHardness(0.2F);
        setResistance(0.0F);
        setTickRandomly(true);
        this.setLightOpacity(1);
    }
    /***
     * テクスチャー追加、4方向個1セットとする、最大4種
     * @param tex
     */
    public BlockDSquare addTexName(String... texArray)
    {
        if (texArray.length % 4 != 0)
        {
            FMLCommonHandler.instance().raiseException(new IllegalArgumentException(),"BlockDSquare.addTex() is multiples of 4 only", true);
        }

        if (texNameList != null)
        {
            String[] temp = new String[texNameList.length + texArray.length];
            System.arraycopy(texNameList, 0, temp, 0, texNameList.length);
            System.arraycopy(texArray, 0, temp, texNameList.length, texArray.length);
            texNameList = temp;
        }
        else
        {
            texNameList = texArray;
        }

        return this;
    }
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        return false;
    }
    @Override
    public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f, int i1)
    {
        if (world.isRemote)
        {
            return;
        }

        ItemStack is;

        switch (l)
        {
            case 10:
            case 11:
                is = new ItemStack(isHalf ? BambooInit.dHalfSquareBID : BambooInit.dSquareBID, 1, 10);
                break;

            default:
                is = new ItemStack(isHalf ? BambooInit.dHalfSquareBID : BambooInit.dSquareBID, 1, l & 12);
        }

        dropBlockAsItem_do(world, i, j, k, is);
    }
    @Override
    public Icon getIcon(int side, int meta)
    {
        meta &= 0xf;
        return meta < icons.length ? icons[meta] : icons[0];
    }
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        int meta = (par1World.getBlockMetadata(par2, par3, par4)) | ((BambooUtil.getPlayerDir(par5EntityLivingBase) + 1) & 3);

        switch (meta)
        {
            case 10:
            case 11:
                if (par6ItemStack.getItemDamage() == 8)
                {
                    meta -= 2;
                }

                break;
        }

        par1World.setBlockMetadataWithNotify(par2, par3, par4, meta, 3);
    }
    @Override
    public boolean renderAsNormalBlock()
    {
        return !isHalf;
    }
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 4));
        par3List.add(new ItemStack(par1, 1, 8));
        par3List.add(new ItemStack(par1, 1, 10));
        par3List.add(new ItemStack(par1, 1, 12));
    }
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par1World.canBlockSeeTheSky(par2, par3, par4) && par5Random.nextInt(10) == 0)
        {
            switch (par1World.getBlockMetadata(par2, par3, par4))
            {
                case 0:
                case 1:
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4) + 8, 3);
                    break;

                case 2:
                case 3:
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4) + 6, 3);
                    break;

                case 12:
                case 13:
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4) - 2, 3);
                    break;

                case 14:
                case 15:
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4) - 4, 3);
                    break;
            }
        }
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (int i = 0; i < texNameList.length; i++)
        {
            icons[i] = par1IconRegister.registerIcon(texNameList[i]);
        }
    }
    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }
}
