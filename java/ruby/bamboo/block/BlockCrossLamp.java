package ruby.bamboo.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCrossLamp extends Block {
    private IIcon[] icons;

    public BlockCrossLamp() {
        super(Material.glass);
        setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.75F, 0.8F);
        setLightLevel(1F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        String[] texs = new String[] { "kagaribi", "bo" };
        icons = new IIcon[texs.length];
        byte b = 0;
        for (String tex : texs) {
            icons[b++] = par1IconRegister.registerIcon(BambooCore.resourceDomain + tex);
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return icons[meta % icons.length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < icons.length; i++) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int posX, int posY, int posZ, Random r) {
        int l = world.getBlockMetadata(posX, posY, posZ);
        double d0 = (double) ((float) posX + 0.5F);
        double d1 = (double) ((float) posY + 0.75F);
        double d2 = (double) ((float) posZ + 0.5F);

        if (l == 0) {
            world.spawnParticle("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        } else if (l == 1) {
            world.spawnParticle("smoke", d0, d1 + 0.3, d2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", d0, d1 + 0.3, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        int meta = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        if (meta == 0) {
            setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.75F, 0.8F);
        } else if (meta == 1) {
            setBlockBounds(0.35F, 0.0F, 0.35F, 0.65F, 0.95F, 0.65F);
        }
    }

    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
    }

    @Override
    public int getRenderType() {
        return 1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }
}
