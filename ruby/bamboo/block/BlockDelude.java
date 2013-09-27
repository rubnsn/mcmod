package ruby.bamboo.block;

import java.util.List;
import java.util.Random;

import ruby.bamboo.BambooInit;
import ruby.bamboo.Config;
import ruby.bamboo.BambooCore;
import ruby.bamboo.CustomRenderHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDelude extends Block implements IDelude
{
    //縦半か
    private boolean isHeightSlab;
    private boolean isIconGrass = false;
    public BlockDelude(int par1, boolean par2)
    {
        super(par1, Material.ground);
        isHeightSlab = par2;
        this.setLightOpacity(0);

        if (par2)
        {
            this.setBlockBounds(0.25F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }

        setHardness(0.5F);
    }
    @Override
    public int getRenderType()
    {
        return CustomRenderHandler.deludeUID;
    }
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        if (this.isHeightSlab)
        {
            switch (par1IBlockAccess.getBlockMetadata(par2, par3, par4))
            {
                case 5:
                    this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
                    break;

                case 2:
                    this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
                    break;

                case 3:
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
                    break;

                case 0:
                    this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;

                case 1:
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
                    break;

                default :
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;
            }
        }
        else
        {
            boolean var5 = (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) != 0;

            if (var5)
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
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
    }
    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (this.isHeightSlab)
        {
            return super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
        }
        else if (par5 != 1 && par5 != 0 && !super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5))
        {
            return false;
        }
        else
        {
            int var6 = par2 + Facing.offsetsXForSide[Facing.oppositeSide[par5]];
            int var7 = par3 + Facing.offsetsYForSide[Facing.oppositeSide[par5]];
            int var8 = par4 + Facing.offsetsZForSide[Facing.oppositeSide[par5]];
            boolean var9 = (par1IBlockAccess.getBlockMetadata(var6, var7, var8) & 8) != 0;
            return var9 ? (par5 == 0 ? true : (par5 == 1 && super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5) ? true : !isBlockSingleSlab(par1IBlockAccess.getBlockId(par2, par3, par4)) || (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) == 0)) : (par5 == 1 ? true : (par5 == 0 && super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5) ? true : !isBlockSingleSlab(par1IBlockAccess.getBlockId(par2, par3, par4)) || (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) != 0));
        }
    }
    private boolean isBlockSingleSlab(int par0)
    {
        return par0 == Block.stoneSingleSlab.blockID || par0 == Block.woodSingleSlab.blockID;
    }
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        if (this.isHeightSlab)
        {
            this.setBlockBounds(0.25F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("delude");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return !isIconGrass ? getDefaultIcon() : Block.grass.getIcon(par1, par2);
    }
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    @Override
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        int var10 = 0;

        if (!isHeightSlab)
        {
            var10 = (par5 != 0 && (par5 == 1 || (double)par7 <= 0.5D) ? par9 : par9 | 8);
        }

        int meta = par5;

        switch (par5)
        {
            case 0:
                meta = 4;
                break;

            case 1:
                meta = 5;
                break;

            case 4:
                meta = 0;
                break;

            case 5:
                meta = 1;
                break;
        }

        return var10 | meta;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        return onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9, 0);
    }
    private boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9, int par10)
    {
        switch (getMetadata(par1World, par2, par3, par4))
        {
            case 0:
                par2++;
                break;

            case 1:
                par2--;
                break;

            case 2:
                par4++;
                break;

            case 3:
                par4--;
                break;

            case 4:
                par3++;
                break;

            case 5:
                par3--;
                break;
        }

        if (isDeludeBlock(par1World, par2, par3, par4))
        {
            return par10 < BambooCore.getConf().deludeMaxReference ? onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9, par10 + 1) : false;
        }
        else
        {
            return Block.blocksList[par1World.getBlockId(par2, par3, par4)] != null ? Block.blocksList[par1World.getBlockId(par2, par3, par4)].onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9) : false;
        }
    }
    @Override
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        Icon tex = getBlockTexture(par1IBlockAccess, par2, par3, par4, par5, 0);
        return tex != null ? tex : getDefaultIcon();
    }
    private Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5 , int par6)
    {
        switch (getMetadata(par1IBlockAccess, par2, par3, par4))
        {
            case 0:
                par2++;
                break;

            case 1:
                par2--;
                break;

            case 2:
                par4++;
                break;

            case 3:
                par4--;
                break;

            case 4:
                par3++;
                break;

            case 5:
                par3--;
                break;
        }

        if (isDeludeBlock(par1IBlockAccess, par2, par3, par4))
        {
            return par6 < BambooCore.getConf().deludeTexMaxReference ? getBlockTexture(par1IBlockAccess, par2, par3, par4, par5, par6 + 1) : getDefaultIcon();
        }
        else
        {
            return Block.blocksList[par1IBlockAccess.getBlockId(par2, par3, par4)] != null && par1IBlockAccess.getBlockMaterial(par2, par3, par4) != Material.water ? Block.blocksList[par1IBlockAccess.getBlockId(par2, par3, par4)].getBlockTexture(par1IBlockAccess, par2, par3, par4, par5) : getDefaultIcon();
        }
    }
    private Icon getDefaultIcon()
    {
        return blockIcon;
    }
    @Override
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return colorMultiplier(par1IBlockAccess, par2, par3, par4, 0);
    }
    private int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        switch (getMetadata(par1IBlockAccess, par2, par3, par4))
        {
            case 0:
                par2++;
                break;

            case 1:
                par2--;
                break;

            case 2:
                par4++;
                break;

            case 3:
                par4--;
                break;

            case 4:
                par3++;
                break;

            case 5:
                par3--;
                break;
        }

        if (isDeludeBlock(par1IBlockAccess, par2, par3, par4))
        {
            return par5 < BambooCore.getConf().deludeTexMaxReference ? colorMultiplier(par1IBlockAccess, par2, par3, par4, par5 + 1) : 0xFFFFFF;
        }
        else
        {
            return Block.blocksList[par1IBlockAccess.getBlockId(par2, par3, par4)] != null && par1IBlockAccess.getBlockMaterial(par2, par3, par4) != Material.water ? Block.blocksList[par1IBlockAccess.getBlockId(par2, par3, par4)].colorMultiplier(par1IBlockAccess, par2, par3, par4) : 0xFFFFFF;
        }
    }
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
    }
    private int getMetadata(IBlockAccess ib, int x, int y, int z)
    {
        if (ib.getBlockId(x, y, z) == BambooInit.delude_stairBID)
        {
            int meta = ib.getBlockMetadata(x, y, z);
            return (meta & 8) != 8 ? meta & 3 : (meta & 12) == 8 ? 5 : 4;
        }
        else
        {
            return ib.getBlockMetadata(x, y, z) & 7;
        }
    }
    private boolean isDeludeBlock(IBlockAccess iba, int x, int y, int z)
    {
        return Block.blocksList[iba.getBlockId(x, y, z)] instanceof IDelude;
    }
    @Override
    public int getRenderBlockPass()
    {
        return 0;
    }
    @Override
    public void setIconGrass(boolean bool)
    {
        isIconGrass = bool;
    }
    @Override
    public int getOriginalRenderType()
    {
        return 0;
    }
}
