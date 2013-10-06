package ruby.bamboo.block;

import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockKayabukiRoof extends BlockStairs
{
    /** The block that is used as model for the stair. */

    public BlockKayabukiRoof(int par1)
    {
        super(par1, BambooInit.getBlockInstance(BambooInit.DECO_SQUARE), 4);
        this.setStepSound(Block.soundSandFootstep);
        setHardness(1.0F);
        //setLightValue(0.01F);
        this.setLightOpacity(1);
    }
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        return false;
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
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("kaya_y");
    }

    /*

    @Override
    public void addCollidingBlockToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        int var8 = par1World.getBlockMetadata(par2, par3, par4);
        int var9 = var8 & 3;
        float var10 = 0.0F;
        float var11 = 0.5F;
        float var12 = 0.5F;
        float var13 = 1.0F;

        if ((var8 & 4) != 0)
        {
            var10 = 0.5F;
            var11 = 1.0F;
            var12 = 0.0F;
            var13 = 0.5F;
        }

        this.setBlockBounds(0.0F, var10, 0.0F, 1.0F, var11, 1.0F);
        super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);

        if (var9 == 0)
        {
            this.setBlockBounds(0.5F, var12, 0.0F, 1.0F, var13, 1.0F);
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }
        else if (var9 == 1)
        {
            this.setBlockBounds(0.0F, var12, 0.0F, 0.5F, var13, 1.0F);
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }
        else if (var9 == 2)
        {
            this.setBlockBounds(0.0F, var12, 0.5F, 1.0F, var13, 1.0F);
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }
        else if (var9 == 3)
        {
            this.setBlockBounds(0.0F, var12, 0.0F, 1.0F, var13, 0.5F);
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
    {
        int var6 = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int var7 = par1World.getBlockMetadata(par2, par3, par4) & 12;

        if (var6 == 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2 | var7);
        }

        if (var6 == 1)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1 | var7);
        }

        if (var6 == 2)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3 | var7);
        }

        if (var6 == 3)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 0 | var7);
        }
    }
    @Override
    public void updateBlockMetadata(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8)
    {
        if (par5 == 0 || par5 != 1 && (double)par7 > 0.5D)
        {
            int var9 = par1World.getBlockMetadata(par2, par3, par4);
            par1World.setBlockMetadataWithNotify(par2, par3, par4, var9 | 4);
        }
    }

    @Override
    public int damageDropped(int i)
    {
        if (i < 8)
        {
            return 0;
        }
        else
        {
            return 8;
        }
    }
    @Override
    protected ItemStack createStackedBlock(int i)
    {
        ItemStack itemStack = null;

        if (i < 8)
        {
            itemStack = new ItemStack(Item.itemsList[blockID], 1, 0);
        }

        return itemStack;
    }
     */
}
