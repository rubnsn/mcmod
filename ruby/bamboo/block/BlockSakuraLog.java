package ruby.bamboo.block;

import java.util.List;
import java.util.Random;

import ruby.bamboo.BambooCore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class BlockSakuraLog extends BlockLog
{
    private static Icon top;
    private static Icon side;
    public BlockSakuraLog(int par1)
    {
        super(par1);
        setHardness(2.0F);
        setStepSound(Block.soundWoodFootstep);
    }
    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return this.blockID;
    }
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }
    @Override
    public Icon getIcon(int par1, int par2)
    {
        if (par2 == 0)
        {
            if (par1 == 0 || par1 == 1)
            {
                return top;
            }
            else
            {
                return side;
            }
        }
        else if (par2 == 4)
        {
            if (par1 == 4 || par1 == 5)
            {
                return top;
            }
            else
            {
                return side;
            }
        }
        else   //if(par2==8){
        {
            if (par1 == 2 || par1 == 3)
            {
                return top;
            }
            else
            {
                return side;
            }
        }
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.top = par1IconRegister.registerIcon("sakuralog_t");
        this.side = par1IconRegister.registerIcon("sakuralog_s");
    }
}
