package ruby.bamboo.block;

import java.util.List;

import ruby.bamboo.BambooCore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDecorations extends Block
{
    private boolean isHalf;
    String[] texNames;
    Icon[] icons;
    private Block upper;
    public BlockDecorations(int par1, Material par2Material, boolean isHalf)
    {
        super(par1, par2Material);
        this.isHalf = isHalf;
        setHardness(0.2F);
        setResistance(1.0F);

        if (isHalf)
        {
            this.setLightOpacity(0);
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
        else
        {
            this.setLightOpacity(255);
        }
    }
    //最上位は半ブロックの上下判定で使用する,バニラの半ブロと同じ仕様
    public BlockDecorations addTexName(String... name)
    {
        texNames = name;
        icons = new Icon[name.length & 7];
        return this;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return !isHalf;
    }
    @Override
    public boolean renderAsNormalBlock()
    {
        return !isHalf;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (int i = 0; i < icons.length; i++)
        {
            this.icons[i] = par1IconRegister.registerIcon(texNames[i]);
        }
    }
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side, int meta)
    {
        return icons[meta & 7];
    }
    @Override
    public int damageDropped(int par1)
    {
        return par1 & 7;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < icons.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
    @Override
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        return par5 != 0 && (par5 == 1 || (double)par7 <= 0.5D) ? par9 : par9 | 8;
    }
    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
    }
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        if (!this.isHalf)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            boolean flag = (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) != 0;

            if (flag)
            {
                this.setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
            else
            {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
            }
        }
    }
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (!this.isHalf)
        {
            return super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
        }
        else if (par5 != 1 && par5 != 0 && !super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5))
        {
            return false;
        }
        else
        {
            int i1 = par2 + Facing.offsetsXForSide[Facing.oppositeSide[par5]];
            int j1 = par3 + Facing.offsetsYForSide[Facing.oppositeSide[par5]];
            int k1 = par4 + Facing.offsetsZForSide[Facing.oppositeSide[par5]];
            boolean flag = (par1IBlockAccess.getBlockMetadata(i1, j1, k1) & 8) != 0;
            return flag ? (par5 == 0 ? true : (par5 == 1 && super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5) ? true : !isBlockSingleSlab(par1IBlockAccess.getBlockId(par2, par3, par4)) || (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) == 0)) : (par5 == 1 ? true : (par5 == 0 && super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5) ? true : !isBlockSingleSlab(par1IBlockAccess.getBlockId(par2, par3, par4)) || (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) != 0));
        }
    }
    private boolean isBlockSingleSlab(int par0)
    {
        return par0 == Block.stoneSingleSlab.blockID || par0 == Block.woodSingleSlab.blockID || par0 == blockID;
    }
    @Override
    public void setBlockBoundsForItemRender()
    {
        if (this.isHalf)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
