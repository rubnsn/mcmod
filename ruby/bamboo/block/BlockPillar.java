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

public class BlockPillar extends Block {
    private final float minSize;
    private final float maxSize;
    private final float height;
    private final Block iconBlock;
    private final int iconMeta;
    private ForgeDirection lastRenderDirection;
    private boolean isSubBoundsBoxRender = false;

    public BlockPillar(Block block, int meta, float minSize, float maxSize, float height) {
        super(Material.wood);
        this.setHardness(0.2F);
        this.setResistance(1.0F);
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.height = height;
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
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        Block block = par1IBlockAccess.getBlock(par2, par3, par4);

        if (block != null) {
            ForgeDirection fd = ForgeDirection.getOrientation(par5).getOpposite();
            int meta = par1IBlockAccess.getBlockMetadata(par2 + fd.offsetX, par3 + fd.offsetY, par4 + fd.offsetZ);

            if (meta == fd.ordinal() && block.isOpaqueCube() && block.renderAsNormalBlock()) {
                return false;
            }

            if (block instanceof BlockPillar) {
                if (par1IBlockAccess.getBlockMetadata(par2, par3, par4) == meta) {
                    if (((BlockPillar) par1IBlockAccess.getBlock(par2, par3, par4)).getSize() >= this.getSize()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void setSubBoundsBoxRender(boolean bool) {
        isSubBoundsBoxRender = bool;
    }

    // Y+
    public boolean setUpBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        BlockPillar block;
        block = (BlockPillar) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.UP, par2, par3, par4) : this);

        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case NORTH:
            this.setBlockBounds(block.getMinSize(), block.getMaxSize(), 1F - block.getHeight(), block.getMaxSize(), 1, 1.0F);
            break;

        case SOUTH:
            this.setBlockBounds(block.getMinSize(), block.getMaxSize(), 0, block.getMaxSize(), 1, block.getHeight());
            break;

        case WEST:
            this.setBlockBounds(1F - block.getHeight(), block.getMaxSize(), block.getMinSize(), 1.0F, 1, block.getMaxSize());
            break;

        case EAST:
            this.setBlockBounds(0, block.getMaxSize(), block.getMinSize(), block.getHeight(), 1, block.getMaxSize());
            break;

        default:
            break;
        }

        lastRenderDirection = ForgeDirection.UP;
        return true;
    }

    // Y-
    public boolean setDownBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        BlockPillar block;
        block = (BlockPillar) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.DOWN, par2, par3, par4) : this);

        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case NORTH:
            this.setBlockBounds(block.getMinSize(), 0, 1F - block.getHeight(), block.getMaxSize(), block.getMinSize(), 1.0F);
            break;

        case SOUTH:
            this.setBlockBounds(block.getMinSize(), 0, 0, block.getMaxSize(), block.getMinSize(), block.getHeight());
            break;

        case WEST:
            this.setBlockBounds(1F - block.getHeight(), 0, block.getMinSize(), 1.0F, block.getMinSize(), block.getMaxSize());
            break;

        case EAST:
            this.setBlockBounds(0, 0, block.getMinSize(), block.getHeight(), block.getMinSize(), block.getMaxSize());
            break;

        default:
            break;
        }

        lastRenderDirection = ForgeDirection.DOWN;
        return true;
    }

    // Z+
    public boolean setSouthBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        BlockPillar block;
        block = (BlockPillar) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.SOUTH, par2, par3, par4) : this);

        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case UP:
            this.setBlockBounds(block.getMinSize(), 0, block.getMaxSize(), block.getMaxSize(), block.getHeight(), 1);
            break;

        case DOWN:
            this.setBlockBounds(block.getMinSize(), 1 - block.getHeight(), block.getMaxSize(), block.getMaxSize(), 1, 1);
            break;

        case WEST:
            this.setBlockBounds(1 - block.getHeight(), block.getMinSize(), block.getMaxSize(), 1, block.getMaxSize(), 1);
            break;

        case EAST:
            this.setBlockBounds(0, block.getMinSize(), block.getMaxSize(), block.getHeight(), block.getMaxSize(), 1);
            break;

        default:
            break;
        }

        lastRenderDirection = ForgeDirection.SOUTH;
        return true;
    }

    // Z-
    public boolean setNorthBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        BlockPillar block;
        block = (BlockPillar) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.NORTH, par2, par3, par4) : this);

        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case UP:
            this.setBlockBounds(block.getMinSize(), 0, 0, block.getMaxSize(), block.getHeight(), block.getMinSize());
            break;

        case DOWN:
            this.setBlockBounds(block.getMinSize(), 1 - block.getHeight(), 0, block.getMaxSize(), 1, block.getMinSize());
            break;

        case WEST:
            this.setBlockBounds(1 - block.getHeight(), block.getMinSize(), 0, 1, block.getMaxSize(), block.getMinSize());
            break;

        case EAST:
            this.setBlockBounds(0, block.getMinSize(), 0, block.getHeight(), block.getMaxSize(), block.getMinSize());
            break;

        default:
            break;
        }

        lastRenderDirection = ForgeDirection.NORTH;
        return true;
    }

    // X+
    public boolean setEastBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        BlockPillar block;
        block = (BlockPillar) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.EAST, par2, par3, par4) : this);

        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case NORTH:
            this.setBlockBounds(block.getMaxSize(), block.getMinSize(), 1F - block.getHeight(), 1, block.getMaxSize(), 1.0F);
            break;

        case SOUTH:
            this.setBlockBounds(block.getMaxSize(), block.getMinSize(), 0, 1, block.getMaxSize(), block.getHeight());
            break;

        case UP:
            this.setBlockBounds(block.getMaxSize(), 0, block.getMinSize(), 1, block.getHeight(), block.getMaxSize());
            break;

        case DOWN:
            this.setBlockBounds(block.getMaxSize(), 1 - block.getHeight(), block.getMinSize(), 1, 1, block.getMaxSize());
            break;

        default:
            break;
        }

        lastRenderDirection = ForgeDirection.EAST;
        return true;
    }

    // X-
    public boolean setWestBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, boolean isSmallScale) {
        BlockPillar block;
        block = (BlockPillar) (isSmallScale ? getOffsettedBlock(par1IBlockAccess, ForgeDirection.WEST, par2, par3, par4) : this);

        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case NORTH:
            this.setBlockBounds(0, block.getMinSize(), 1F - block.getHeight(), block.getMinSize(), block.getMaxSize(), 1.0F);
            break;

        case SOUTH:
            this.setBlockBounds(0, block.getMinSize(), 0, block.getMinSize(), block.getMaxSize(), block.getHeight());
            break;

        case UP:
            this.setBlockBounds(0, 0, block.getMinSize(), block.getMinSize(), block.getHeight(), block.getMaxSize());
            break;

        case DOWN:
            this.setBlockBounds(0, 1 - block.getHeight(), block.getMinSize(), block.getMinSize(), 1, block.getMaxSize());
            break;

        default:
            break;
        }

        lastRenderDirection = ForgeDirection.WEST;
        return true;
    }

    public void setCoreBoundsBox(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        switch (ForgeDirection.getOrientation(par1IBlockAccess.getBlockMetadata(par2, par3, par4))) {
        case DOWN:
            this.setBlockBounds(minSize, 1 - height, minSize, maxSize, 1F, maxSize);
            break;

        case EAST:
            this.setBlockBounds(0, minSize, minSize, height, maxSize, maxSize);
            break;

        case NORTH:
            this.setBlockBounds(minSize, minSize, 1F - height, maxSize, maxSize, 1.0F);
            break;

        case SOUTH:
            this.setBlockBounds(minSize, minSize, 0, maxSize, maxSize, height);
            break;

        case UP:
            this.setBlockBounds(minSize, 0F, minSize, maxSize, height, maxSize);
            break;

        case WEST:
            this.setBlockBounds(1F - height, minSize, minSize, 1.0F, maxSize, maxSize);
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
            this.setBlockBounds(0F, 1 - height, 0F, 1F, 1F, 1F);
            break;

        case EAST:
            this.setBlockBounds(0, 0, 0, height, 1, 1F);
            break;

        case NORTH:
            this.setBlockBounds(0, 0, 1F - height, 1, 1, 1.0F);
            break;

        case SOUTH:
            this.setBlockBounds(0, 0, 0, 1, 1, height);
            break;

        case UP:
            this.setBlockBounds(0, 0, 0, 1, height, 1);
            break;

        case WEST:
            this.setBlockBounds(1F - height, 0, 0, 1, 1, 1);
            break;

        case UNKNOWN:
        default:
            break;
        }
    }

    public static boolean canBlockPlace(World world, int posX, int posY, int posZ, int side) {
        return canStay(world, posX, posY, posZ, side);
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
        if (!canStay(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4))) {
            par1World.func_147480_a(par2, par3, par4, true);
        }
    }

    private static boolean canStay(World world, int posX, int posY, int posZ, int side) {
        ForgeDirection fd = ForgeDirection.getOrientation(side).getOpposite();
        return !isAirBlockOffset(world, fd, posX, posY, posZ) && !(getOffsettedBlock(world, fd, posX, posY, posZ) instanceof BlockPillar);
    }

    private static boolean isAirBlockOffset(World world, ForgeDirection fd, int posX, int posY, int posZ) {
        return world.isAirBlock(posX + fd.offsetX, posY + fd.offsetY, posZ + fd.offsetZ);
    }

    private static Block getOffsettedBlock(IBlockAccess iBlockAccess, ForgeDirection fd, int posX, int posY, int posZ) {
        return iBlockAccess.getBlock(posX + fd.offsetX, posY + fd.offsetY, posZ + fd.offsetZ);
    }

    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.5F, minSize * 0.7F, minSize * 0.7F, 0.5F + height, maxSize * 1.3F, maxSize * 1.3F);
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
    }

    public float getSize() {
        return maxSize - minSize;
    }

    public float getMinSize() {
        return minSize;
    }

    public float getMaxSize() {
        return maxSize;
    }

    public float getHeight() {
        return height;
    }
}
