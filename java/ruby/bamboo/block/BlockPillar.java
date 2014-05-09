package ruby.bamboo.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import ruby.bamboo.CustomRenderHandler;

public class BlockPillar extends Block implements IPillarRender {
    private final float minWidth;
    private final float maxWidth;
    private final float minHeight;
    private final float maxHeight;
    private final Block iconBlock;
    private final int iconMeta;
    private int renderSide;

    public BlockPillar(Block block, int meta, float minWidth, float maxWidth, float height) {
        super(Material.wood);
        this.setHardness(0.2F);
        this.setResistance(1.0F);
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.minHeight = height;
        this.maxHeight = 1 - height;
        this.iconBlock = block;
        this.iconMeta = meta;
    }

    @Override
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int side, float hitX, float hitY, float hitZ, int metadata) {
        return side;
    }

    @Override
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return iconBlock.getIcon(2, iconMeta);
    }

    @Override
    public IIcon getIcon(int par1, int par2) {
        return iconBlock.getIcon(2, iconMeta);
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
            ForgeDirection fd = ForgeDirection.getOrientation(par5).getOpposite();
            int meta = par1IBlockAccess.getBlockMetadata(par2 + fd.offsetX, par3 + fd.offsetY, par4 + fd.offsetZ);

            if (meta == fd.ordinal() && block.isOpaqueCube() && block.renderAsNormalBlock()) {
                return false;
            }
            if (block instanceof BlockPillar) {
                if (par1IBlockAccess.getBlockMetadata(par2, par3, par4) == meta) {
                    if (((BlockPillar) par1IBlockAccess.getBlock(par2, par3, par4)).getSize() > this.getSize()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    // Y+
    public boolean setUpBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        IPillarRender block = (IPillarRender) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.UP, par2, par3, par4) : this);

        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case NORTH:
            this.setBlockBounds(block.getMinWidth(), block.getMaxWidth(), block.getMaxHeight(), block.getMaxWidth(), 1, 1.0F);
            break;

        case SOUTH:
            this.setBlockBounds(block.getMinWidth(), block.getMaxWidth(), 0, block.getMaxWidth(), 1, block.getMinHeight());
            break;

        case WEST:
            this.setBlockBounds(block.getMaxHeight(), block.getMaxWidth(), block.getMinWidth(), 1.0F, 1, block.getMaxWidth());
            break;

        case EAST:
            this.setBlockBounds(0, block.getMaxWidth(), block.getMinWidth(), block.getMinHeight(), 1, block.getMaxWidth());
            break;

        default:
            break;
        }

        return true;
    }

    @Override
    // Y-
    public boolean setDownBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        IPillarRender block = (IPillarRender) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.DOWN, par2, par3, par4) : this);

        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case NORTH:
            this.setBlockBounds(block.getMinWidth(), 0, block.getMaxHeight(), block.getMaxWidth(), block.getMinWidth(), 1.0F);
            break;

        case SOUTH:
            this.setBlockBounds(block.getMinWidth(), 0, 0, block.getMaxWidth(), block.getMinWidth(), block.getMinHeight());
            break;

        case WEST:
            this.setBlockBounds(block.getMaxHeight(), 0, block.getMinWidth(), 1.0F, block.getMinWidth(), block.getMaxWidth());
            break;

        case EAST:
            this.setBlockBounds(0, 0, block.getMinWidth(), block.getMinHeight(), block.getMinWidth(), block.getMaxWidth());
            break;

        default:
            break;
        }

        return true;
    }

    @Override
    // Z+
    public boolean setSouthBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        IPillarRender block = (IPillarRender) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.SOUTH, par2, par3, par4) : this);

        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case UP:
            this.setBlockBounds(block.getMinWidth(), 0, block.getMaxWidth(), block.getMaxWidth(), block.getMinHeight(), 1);
            break;

        case DOWN:
            this.setBlockBounds(block.getMinWidth(), block.getMaxHeight(), block.getMaxWidth(), block.getMaxWidth(), 1, 1);
            break;

        case WEST:
            this.setBlockBounds(block.getMaxHeight(), block.getMinWidth(), block.getMaxWidth(), 1, block.getMaxWidth(), 1);
            break;

        case EAST:
            this.setBlockBounds(0, block.getMinWidth(), block.getMaxWidth(), block.getMinHeight(), block.getMaxWidth(), 1);
            break;

        default:
            break;
        }

        return true;
    }

    @Override
    // Z-
    public boolean setNorthBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        IPillarRender block = (IPillarRender) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.NORTH, par2, par3, par4) : this);

        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case UP:
            this.setBlockBounds(block.getMinWidth(), 0, 0, block.getMaxWidth(), block.getMinHeight(), block.getMinWidth());
            break;

        case DOWN:
            this.setBlockBounds(block.getMinWidth(), block.getMaxHeight(), 0, block.getMaxWidth(), 1, block.getMinWidth());
            break;

        case WEST:
            this.setBlockBounds(block.getMaxHeight(), block.getMinWidth(), 0, 1, block.getMaxWidth(), block.getMinWidth());
            break;

        case EAST:
            this.setBlockBounds(0, block.getMinWidth(), 0, block.getMinHeight(), block.getMaxWidth(), block.getMinWidth());
            break;

        default:
            break;
        }

        return true;
    }

    @Override
    // X+
    public boolean setEastBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        IPillarRender block = (IPillarRender) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.EAST, par2, par3, par4) : this);

        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case NORTH:
            this.setBlockBounds(block.getMaxWidth(), block.getMinWidth(), block.getMaxHeight(), 1, block.getMaxWidth(), 1.0F);
            break;

        case SOUTH:
            this.setBlockBounds(block.getMaxWidth(), block.getMinWidth(), 0, 1, block.getMaxWidth(), block.getMinHeight());
            break;

        case UP:
            this.setBlockBounds(block.getMaxWidth(), 0, block.getMinWidth(), 1, block.getMinHeight(), block.getMaxWidth());
            break;

        case DOWN:
            this.setBlockBounds(block.getMaxWidth(), block.getMaxHeight(), block.getMinWidth(), 1, 1, block.getMaxWidth());
            break;

        default:
            break;
        }

        return true;
    }

    @Override
    // X-
    public boolean setWestBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        IPillarRender block = (IPillarRender) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.WEST, par2, par3, par4) : this);

        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case NORTH:
            this.setBlockBounds(0, block.getMinWidth(), block.getMaxHeight(), block.getMinWidth(), block.getMaxWidth(), 1.0F);
            break;

        case SOUTH:
            this.setBlockBounds(0, block.getMinWidth(), 0, block.getMinWidth(), block.getMaxWidth(), block.getMinHeight());
            break;

        case UP:
            this.setBlockBounds(0, 0, block.getMinWidth(), block.getMinWidth(), block.getMinHeight(), block.getMaxWidth());
            break;

        case DOWN:
            this.setBlockBounds(0, block.getMaxHeight(), block.getMinWidth(), block.getMinWidth(), 1, block.getMaxWidth());
            break;

        default:
            break;
        }

        return true;
    }

    @Override
    public void setCoreBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case DOWN:
            this.setBlockBounds(minWidth, 1 - minHeight, minWidth, maxWidth, 1F, maxWidth);
            break;

        case EAST:
            this.setBlockBounds(0, minWidth, minWidth, minHeight, maxWidth, maxWidth);
            break;

        case NORTH:
            this.setBlockBounds(minWidth, minWidth, 1F - minHeight, maxWidth, maxWidth, 1.0F);
            break;

        case SOUTH:
            this.setBlockBounds(minWidth, minWidth, 0, maxWidth, maxWidth, minHeight);
            break;

        case UP:
            this.setBlockBounds(minWidth, 0F, minWidth, maxWidth, minHeight, maxWidth);
            break;

        case WEST:
            this.setBlockBounds(1F - minHeight, minWidth, minWidth, 1.0F, maxWidth, maxWidth);
            break;

        case UNKNOWN:
        default:
            break;
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case DOWN:
            this.setBlockBounds(0F, 1 - minHeight, 0F, 1F, 1F, 1F);
            break;

        case EAST:
            this.setBlockBounds(0, 0, 0, minHeight, 1, 1F);
            break;

        case NORTH:
            this.setBlockBounds(0, 0, 1F - minHeight, 1, 1, 1.0F);
            break;

        case SOUTH:
            this.setBlockBounds(0, 0, 0, 1, 1, minHeight);
            break;

        case UP:
            this.setBlockBounds(0, 0, 0, 1, minHeight, 1);
            break;

        case WEST:
            this.setBlockBounds(1F - minHeight, 0, 0, 1, 1, 1);
            break;

        case UNKNOWN:
        default:
            break;
        }
    }

    public boolean canBlockPlace(World world, int posX, int posY, int posZ, int side) {
        return canStay(world, posX, posY, posZ, side);
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
        if (!canStay(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4))) {
            par1World.func_147480_a(par2, par3, par4, true);
        }
    }

    private boolean canStay(World world, int posX, int posY, int posZ, int side) {
        ForgeDirection fd = ForgeDirection.getOrientation(side).getOpposite();
        return !isAirBlockOffset(world, fd, posX, posY, posZ) && !(getOffsettedBlock(world, fd, posX, posY, posZ) instanceof BlockPillar);
    }

    private boolean isAirBlockOffset(World world, ForgeDirection fd, int posX, int posY, int posZ) {
        return world.isAirBlock(posX + fd.offsetX, posY + fd.offsetY, posZ + fd.offsetZ);
    }

    private Block getOffsettedBlock(IBlockAccess iBlockAccess, ForgeDirection fd, int posX, int posY, int posZ) {
        return iBlockAccess.getBlock(posX + fd.offsetX, posY + fd.offsetY, posZ + fd.offsetZ);
    }

    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.5F, minWidth * 0.7F, minWidth * 0.7F, 0.5F + minHeight, maxWidth * 1.3F, maxWidth * 1.3F);
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
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
        return true;
    }

    @Override
    public boolean canDifferentMetaLink(int meta1, int meta2) {
        return meta1 != meta2;
    }

}
