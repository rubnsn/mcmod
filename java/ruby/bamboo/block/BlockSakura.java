package ruby.bamboo.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import ruby.bamboo.BambooCore;
import ruby.bamboo.worldgen.WorldGenBigSakura;
import ruby.bamboo.worldgen.WorldGenSakura;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSakura extends BlockSapling {
    public BlockSakura() {
        super();
        float f = 0.4F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (!par1World.isRemote) {
            ItemStack is = par5EntityPlayer.getCurrentEquippedItem();

            if (is != null && is.getItem() == Items.dye && is.getItemDamage() != 0x0F) {
                if (!par5EntityPlayer.capabilities.isCreativeMode) {
                    is.stackSize--;
                }

                growTree(par1World, par2, par3, par4, par1World.rand, is.getItemDamage());
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public void onBonemealEvent(BonemealEvent event) {
        if (event.block == this) {
            if (event.entityPlayer.capabilities.isCreativeMode) {
                event.setCanceled(true);
            }
            growTree(event.world, event.x, event.y, event.z, event.world.rand, 0x0F);
            event.setResult(Result.ALLOW);
        }
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random) {
        if (!world.isRemote) {
            if (world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(30) == 0) {
                growTree(world, i, j, k, random, 0x0f);
            }
        }
    }

    public void growTree(World world, int i, int j, int k, Random random, int dmg) {
        int l = world.getBlockMetadata(i, j, k) & 3;
        world.setBlockToAir(i, j, k);
        Object obj = null;

        if (random.nextInt(10) == 0) {
            obj = new WorldGenBigSakura(true, dmg);
        } else {
            obj = new WorldGenSakura(dmg);
        }

        if (!((WorldGenerator) (obj)).generate(world, random, i, j, k)) {
            world.setBlock(i, j, k, this, dmg, 0);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(BambooCore.resourceDomain + "sakura");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return this.blockIcon;
    }

}
