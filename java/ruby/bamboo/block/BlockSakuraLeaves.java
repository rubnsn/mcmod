package ruby.bamboo.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import ruby.bamboo.entity.EntitySakuraPetal;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSakuraLeaves extends BlockLeavesBase implements ICustomPetal {
    private IIcon sakurapetal;
    private IIcon broadleaf;

    public static final int[] COLOR = new int[] { 0x5C5C5C, 0xE60012, 0x3F9E55, 0x98744B, 0x5F89DC, 0xB087CC, 0x87DBF6, 0xD3D3D3, 0x8D8D8D, 0xFF929E, 0xBCF472, 0xFEF66A, 0xB8EFFF, 0xE6B2E4, 0xFFC600, 0xffc5cc };
    private static String[] path = new String[] { "petal", "petal_0", "petal", "petal", "petal", "petal", "petal", "petal", "petal", "petal", "petal", "petal_1", "petal", "petal", "petal_1", "petal" };

    public BlockSakuraLeaves() {
        super(Material.leaves, true);
        // setTickRandomly(true);
        setHardness(0.0F);
        setLightOpacity(1);
        setLightLevel(0.7F);
        setStepSound(Block.soundTypeGrass);
    }

    @Override
    public String getCustomPetal(int meta) {
        return path[meta];
    }

    @Override
    public IIcon getIcon(int i, int j) {
        if (j == 1 || j == 11 || j == 14) {
            return broadleaf;
        }

        return sakurapetal;
    }

    @Override
    public int getRenderColor(int i) {
        return COLOR[i];
    }

    @Override
    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
        return COLOR[iblockaccess.getBlockMetadata(i, j, k)];
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        // par1World.setBlockMetadata(par2, par3, par4, 0x0f);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (par5Random.nextInt(100) != 0) {
            return;
        }

        if (par1World.isAirBlock(par2, par3 - 1, par4)) {
            par1World.spawnEntityInWorld(new EntitySakuraPetal(par1World, par2 + par5Random.nextFloat(), par3, par4 + par5Random.nextFloat(), 0, 0, 0, COLOR[par1World.getBlockMetadata(par2, par3, par4)]).setCustomPetal(getCustomPetal(par1World.getBlockMetadata(par2, par3, par4))));
        }
    }

    private void removeLeaves(World world, int i, int j, int k) {
        int var8 = quantityDropped(world.rand);

        if (var8 > 0) {
            this.dropBlockAsItem(world, i, j, k, new ItemStack(BambooInit.sakura, var8, 0));
        }

        world.setBlockToAir(i, j, k);
    }

    @Override
    public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
        super.onEntityWalking(world, i, j, k, entity);
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return par1Random.nextInt(20) == 0 ? 1 : 0;
    }

    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
        int var8 = quantityDropped(par1World.rand);

        if (var8 > 0) {
            this.dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(BambooInit.sakura, var8, 0));
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
        if (!world.isRemote && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() == Items.shears) {
            entityplayer.getCurrentEquippedItem().damageItem(1, entityplayer);
            dropBlockAsItem(world, i, j, k, new ItemStack(this, 1, l));
        } else {
            int var8 = quantityDropped(world.rand);

            if (var8 > 0) {
                this.dropBlockAsItem(world, i, j, k, new ItemStack(BambooInit.sakura, var8, 0));
            }
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int posX, int posY, int posZ, int side) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.sakurapetal = par1IconRegister.registerIcon(BambooCore.resourceDomain + "sakurapetal");
        this.broadleaf = par1IconRegister.registerIcon(BambooCore.resourceDomain + "broadleaf");
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < COLOR.length; i++) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public int damageDropped(int par1) {
        return par1;
    }
}
