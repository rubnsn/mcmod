package ruby.bamboo.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ruby.bamboo.BambooInit;
import ruby.bamboo.entity.EntitySakuraPetal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSakuraLeaves extends BlockLeavesBase implements ICustomPetal
{
    private Icon sakurapetal;
    private Icon broadleaf;

    private static int[] color = new int[] { 0x191919, 0xCC4C4C, 0x667F33, 0x7F664C, 0x3366CC, 0xB266E5, 0x4C99B2, 0x999999, 0x4C4C4C, 0xF2B2CC, 0x7FCC19, 0xE5E533, 0x99B2F2, 0xE57FD8, 0xF2B233, 0xfbedf0 };
    private static String[] path = new String[] {"petal", "petal_0", "petal", "petal", "petal", "petal", "petal", "petal", "petal", "petal", "petal", "petal_1", "petal", "petal", "petal_1", "petal"};
    /*
     * カラーデバッグ用
     *
     * @Override public boolean onBlockActivated(World par1World, int par2, int
     * par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7,
     * float par8, float par9) { int meta=par1World.getBlockMetadata(par2, par3,
     * par4); meta=meta!=0x0f?meta+1:0;
     * par1World.setBlockMetadataWithNotify(par2, par3, par4, meta);
     * System.out.println(meta); return true; }
     */
    public BlockSakuraLeaves(int id)
    {
        super(id, Material.leaves, true);
        //setTickRandomly(true);
        setHardness(0.0F);
        setLightOpacity(1);
        setLightValue(0.7F);
        setStepSound(Block.soundGrassFootstep);
    }

    @Override
    public String getCustomPetal(int meta)
    {
        return path[meta];
    }
    @Override
    public Icon getIcon(int i, int j)
    {
        if (j == 1 || j == 11 || j == 14)
        {
            return broadleaf;
        }

        return sakurapetal;
    }

    @Override
    public int getRenderColor(int i)
    {
        return color[i];
    }

    @Override
    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        return color[iblockaccess.getBlockMetadata(i, j, k)];
    }

    @Override
    public int idDropped(int i, Random random, int j)
    {
        return this.blockID;
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        // par1World.setBlockMetadata(par2, par3, par4, 0x0f);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par5Random.nextInt(100) != 0)
        {
            return;
        }

        if (par1World.getBlockId(par2, par3 - 1, par4) == 0)
        {
            par1World.spawnEntityInWorld(new EntitySakuraPetal(par1World, par2 + par5Random.nextFloat(), par3, par4 + par5Random.nextFloat(), 0, 0, 0, color[par1World.getBlockMetadata(par2, par3, par4)]).setCustomPetal(getCustomPetal(par1World.getBlockMetadata(par2, par3, par4))));
        }
    }

    private void removeLeaves(World world, int i, int j, int k)
    {
        int var8 = quantityDropped(world.rand);

        if (var8 > 0)
        {
            this.dropBlockAsItem_do(world, i, j, k, new ItemStack(BambooInit.sakuraBID, var8, 0));
        }

        world.setBlock(i, j, k, 0, 0, 3);
    }

    @Override
    public void onEntityWalking(World world, int i, int j, int k, Entity entity)
    {
        super.onEntityWalking(world, i, j, k, entity);
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return par1Random.nextInt(20) == 0 ? 1 : 0;
    }
    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        int var8 = quantityDropped(par1World.rand);

        if (var8 > 0)
        {
            this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(BambooInit.sakuraBID, var8, 0));
        }
    }
    @Override
    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
    {
        if (!world.isRemote && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == Item.shears.itemID)
        {
            entityplayer.addStat(StatList.mineBlockStatArray[blockID], 1);
            entityplayer.getCurrentEquippedItem().damageItem(1, entityplayer);
            dropBlockAsItem_do(world, i, j, k, new ItemStack(this, 1, l));
        }
        else
        {
            int var8 = quantityDropped(world.rand);

            if (var8 > 0)
            {
                this.dropBlockAsItem_do(world, i, j, k, new ItemStack(BambooInit.sakuraBID, var8, 0));
            }
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int posX, int posY, int posZ, int side)
    {
        /*
         * int i1 = iblockaccess.getBlockId(i, j, k); if(!graphicsLevel && i1 ==
         * blockID) { return false; } else { return
         * super.shouldSideBeRendered(iblockaccess, i, j, k, l); }
         */
        return true;
        /*
        if(iblockaccess.getBlockMaterial(posX, posY, posZ)==this.blockMaterial){
        	return false;
        }else{
        	return true;
        }*/
        //return side == 0 && this.minY > 0.0D ? true : (side == 1 && this.maxY < 1.0D ? true : (side == 2 && this.minZ > 0.0D ? true : (side == 3 && this.maxZ < 1.0D ? true : (side == 4 && this.minX > 0.0D ? true : (side == 5 && this.maxX < 1.0D ? true : !iblockaccess.isBlockOpaqueCube(posX, posY, posZ))))));
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.sakurapetal = par1IconRegister.registerIcon("sakurapetal");
        this.broadleaf = par1IconRegister.registerIcon("broadleaf");
    }
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < color.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }
}
