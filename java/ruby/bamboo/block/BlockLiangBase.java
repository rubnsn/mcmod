package ruby.bamboo.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import ruby.bamboo.CustomRenderHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockLiangBase extends Block implements IPillarRender {
    @SideOnly(Side.CLIENT)
    IIcon[] icons;

    private final float minWidth;
    private final float maxWidth;
    private final float minHeight;
    private final float maxHeight;

    private int renderSide;

    public BlockLiangBase(float minWidth, float maxWidth, float minHeight, float maxHeight) {
        super(Material.wood);
        this.setHardness(0.2F);
        this.setResistance(1.0F);
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        setBlockBounds(0, 0, 0, 1, 1, 1);
    }

    @Override
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return getIcon(par5, par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }

    @Override
    public IIcon getIcon(int par1, int par2) {
        return icons[par2 % icons.length];
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.pillarUID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1Item, CreativeTabs par2Tab, List par3List) {
        for (int i = 0; i < icons.length; i++) {
            par3List.add(new ItemStack(par1Item, 1, i));
        }
    }

    // Y+
    @Override
    public boolean setUpBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        IPillarRender block = (IPillarRender) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.UP, par2, par3, par4) : this);
        this.setBlockBounds(block.getMinWidth(), block.getMaxHeight(), block.getMinWidth(), block.getMaxWidth(), 1, block.getMaxWidth());

        return true;
    }

    // Y-
    @Override
    public boolean setDownBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        IPillarRender block = (IPillarRender) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.DOWN, par2, par3, par4) : this);
        this.setBlockBounds(block.getMinWidth(), 0, block.getMinWidth(), block.getMaxWidth(), block.getMinHeight(), block.getMaxWidth());

        return true;
    }

    // Z+
    @Override
    public boolean setSouthBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        IPillarRender block = (IPillarRender) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.SOUTH, par2, par3, par4) : this);
        this.setBlockBounds(block.getMinWidth(), block.getMinHeight(), block.getMaxWidth(), block.getMaxWidth(), block.getMaxHeight(), 1);

        return true;
    }

    // Z-
    @Override
    public boolean setNorthBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        IPillarRender block = (IPillarRender) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.NORTH, par2, par3, par4) : this);
        this.setBlockBounds(block.getMinWidth(), block.getMinHeight(), 0, block.getMaxWidth(), block.getMaxHeight(), block.getMinWidth());

        return true;
    }

    // X+
    @Override
    public boolean setEastBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        IPillarRender block = (IPillarRender) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.EAST, par2, par3, par4) : this);
        this.setBlockBounds(block.getMaxWidth(), block.getMinHeight(), block.getMinWidth(), 1, block.getMaxHeight(), block.getMaxWidth());

        return true;
    }

    // X-
    @Override
    public boolean setWestBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        IPillarRender block = (IPillarRender) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.WEST, par2, par3, par4) : this);
        this.setBlockBounds(0, block.getMinHeight(), block.getMinWidth(), block.getMinWidth(), block.getMaxHeight(), block.getMaxWidth());

        return true;
    }

    @Override
    public boolean isLink(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, ForgeDirection fd) {
        Block offsetBlock = par1IBlockAccess.getBlock(par2 + fd.offsetX, par3 + fd.offsetY, par4 + fd.offsetZ);
        return offsetBlock.isNormalCube() || !(offsetBlock instanceof BlockPillar) && (offsetBlock.getMaterial() == Material.wood || offsetBlock.getMaterial() == Material.ground);
    }

    @Override
    public void setRenderSide(int side) {
        this.renderSide = side;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {

        Block block = par1IBlockAccess.getBlock(par2, par3, par4);
        if (renderSide == par5) {
            return true;
        }
        if (block != null) {
            if (block == this) {
                return false;
            }
        }
        return true;
    }

    public void setCoreBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        this.setBlockBounds(minWidth, minHeight, minWidth, maxWidth, maxHeight, maxWidth);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        this.setBlockBounds(0, 0, 0, 1, 1, 1);
    }

    @Override
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int side, float hitX, float hitY, float hitZ, int metadata) {
        return metadata;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public abstract void registerBlockIcons(IIconRegister p_149651_1_);

    private Block getOffsettedBlock(IBlockAccess iBlockAccess, ForgeDirection fd, int posX, int posY, int posZ) {
        return iBlockAccess.getBlock(posX + fd.offsetX, posY + fd.offsetY, posZ + fd.offsetZ);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(minWidth, minHeight, minWidth, maxWidth, maxHeight, maxWidth);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int posX, int posY, int posZ) {
        return AxisAlignedBB.getAABBPool().getAABB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1);
    }

    @Override
    public float getSize() {
        return maxWidth - minWidth;
    }

    @Override
    public float getMinWidth() {
        return minWidth;
    }

    @Override
    public float getMaxWidth() {
        return maxWidth;
    }

    @Override
    public float getMinHeight() {
        return minHeight;
    }

    @Override
    public float getMaxHeight() {
        return maxHeight;
    }

    @Override
    public boolean isLinkSkipp() {
        return false;
    }

    @Override
    public boolean canDifferentMetaLink(int meta1, int meta2) {
        return false;
    }

    @Override
    public int damageDropped(int par1) {
        return par1;
    }
}
