package ruby.bamboo.block;

import java.util.List;

import ruby.bamboo.CustomRenderHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBambooPane extends BlockPane
{
    private Icon[] blockIcons = new Icon[6];

    public BlockBambooPane(int i, Material material)
    {
        super(i, "bamboopane", "bamboopane", material, true);
        setHardness(0.2F);
    }
    @Override
    public int getRenderType()
    {
        return CustomRenderHandler.bambooPaneUID;
    }
    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        if (par5Entity instanceof EntityItem)
        {
            par5Entity.motionX += 0.01;
            par5Entity.motionZ += 0.01;
        }
    }
    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        if (!isNoren(par1World, par2, par3, par4))
        {
            super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }
    }
    public boolean isSideRender(IBlockAccess iBlockAccess, int x, int y , int z)
    {
        if (isNoren(iBlockAccess, x, y, z))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    private boolean isNoren(IBlockAccess iBlockAccess, int x, int y , int z)
    {
        if (iBlockAccess.getBlockMetadata(x, y, z) != 4 && iBlockAccess.getBlockMetadata(x, y, z) != 5)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < blockIcons.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int meta)
    {
        return blockIcons[meta];
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcons[0] = par1IconRegister.registerIcon("bamboopane");
        this.blockIcons[1] = par1IconRegister.registerIcon("ranma");
        this.blockIcons[2] = par1IconRegister.registerIcon("bamboopane2");
        this.blockIcons[3] = par1IconRegister.registerIcon("bamboopane3");
        this.blockIcons[4] = par1IconRegister.registerIcon("norenblue");
        this.blockIcons[5] = par1IconRegister.registerIcon("norenpurple");
    }
    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }
}
