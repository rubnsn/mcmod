package ruby.bamboo.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ruby.bamboo.BambooCore;
import ruby.bamboo.WorldGenBigSakura;
import ruby.bamboo.WorldGenSakura;

import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockSakura extends BlockFlower {
    public BlockSakura(int i) {
        super(i);
        float f = 0.4F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (!par1World.isRemote) {
            ItemStack is = par5EntityPlayer.getCurrentEquippedItem();

            if (is != null && is.itemID == Item.dyePowder.itemID) {
                if (!par5EntityPlayer.capabilities.isCreativeMode) {
                    is.stackSize--;
                }

                growTree(par1World, par2, par3, par4, par1World.rand, is.getItemDamage());
                return true;
            }
        }

        return false;
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random) {
        if (!world.isRemote) {
            super.updateTick(world, i, j, k, random);

            if (world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(30) == 0) {
                growTree(world, i, j, k, random, 0x0f);
            }
        }
    }

    public void growTree(World world, int i, int j, int k, Random random, int dmg) {
        int l = world.getBlockMetadata(i, j, k) & 3;
        world.setBlock(i, j, k, 0, 0, 3);
        Object obj = null;

        if (random.nextInt(10) == 0) {
            obj = new WorldGenBigSakura(true, dmg);
        } else {
            obj = new WorldGenSakura(dmg);
        }

        if (!((WorldGenerator) (obj)).generate(world, random, i, j, k)) {
            world.setBlock(i, j, k, blockID, dmg, 0);
        }
    }

    @Override
    public int damageDropped(int i) {
        return this.blockID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(BambooCore.resorceDmain + "sakura");
    }
}
