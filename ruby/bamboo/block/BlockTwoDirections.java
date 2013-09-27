package ruby.bamboo.block;

import java.util.List;

import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockTwoDirections extends BlockDecorations
{
    public BlockTwoDirections(int par1, Material par2Material, boolean isHalf)
    {
        super(par1, par2Material, isHalf);
    }
    public BlockDecorations addTexName(String... name)
    {
        texNames = name;
        icons = new Icon[name.length * 2 & 7];
        return this;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (int i = 0; i < texNames.length; i++)
        {
            this.icons[i * 2] = par1IconRegister.registerIcon(texNames[i] + "_x");
            this.icons[i * 2 + 1] = par1IconRegister.registerIcon(texNames[i] + "_y");
        }
    }
    @Override
    public int damageDropped(int par1)
    {
        int meta = super.damageDropped(par1);
        return meta % 2 == 1 ? meta - 1 : meta;
    }
    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        int meta = (par1World.getBlockMetadata(par2, par3, par4)) | ((BambooUtil.getPlayerDir(par5EntityLivingBase) + 1) & 1);
        par1World.setBlockMetadataWithNotify(par2, par3, par4, meta, 3);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < icons.length; i += 2)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
}
