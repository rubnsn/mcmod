package ruby.bamboo.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSakuraCarpet extends BlockQuadRotateBlockBase {

    public BlockSakuraCarpet() {
        super(Material.carpet);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0325F, 1.0F);
        this.setLightOpacity(0);
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
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        String[] iconName = new String[] { "sakuracarpet", "sakuracarpet_red", "sakuracarpet_yellow" };
        icons = new IIcon[iconName.length];
        for (int i = 0; i < iconName.length; ++i) {
            this.icons[i] = iconRegister.registerIcon(BambooCore.resourceDomain + iconName[i]);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return !world.isAirBlock(x, y - 1, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!canBlockStay(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return side == 1 ? true : side == 0 || blockAccess.getBlock(x, y, z) != this ? super.shouldSideBeRendered(blockAccess, x, y, z, side) : false;
    }
}
