package ruby.bamboo.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import ruby.bamboo.BambooCore;
import ruby.bamboo.Config;
import ruby.bamboo.CustomRenderHandler;
import ruby.bamboo.tileentity.TileEntityMultiBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMultiBlock extends BlockContainer {

    private byte renderSide = 0x3F;
    private byte innerX;
    private byte innerY;
    private byte innerZ;

    public BlockMultiBlock() {
        super(Material.ground);
        this.setHardness(1.0F);
        this.setResistance(1.0F);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityMultiBlock) {
            for (ItemStack is : ((TileEntityMultiBlock) tile).getInnnerBlocks()) {
                this.dropBlockAsItem(world, x, y, z, is);
            }
            this.dropBlockAsItem(world, x, y, z, new ItemStack(block, 1, ((TileEntityMultiBlock) tile).getFieldSize()));
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.multiBlockUID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess iblockaccess, int x, int y, int z) {
        TileEntity tile = iblockaccess.getTileEntity(x, y, z);
        if (tile instanceof TileEntityMultiBlock) {
            return ((TileEntityMultiBlock) tile).getInnerBlock(innerX, innerY, innerZ).colorMultiplier((IBlockAccess) tile, innerX, innerY, innerZ);
        }
        return super.colorMultiplier(iblockaccess, x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && this.canUse(par5EntityPlayer)) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileEntityMultiBlock) {
                ItemStack is = par5EntityPlayer.getCurrentEquippedItem();
                if (is != null && canPlaceBlock(Block.getBlockFromItem(is.getItem()))) {
                    Block block = Block.getBlockFromItem(is.getItem());
                    int innerMeta = block.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, is.getItemDamage());
                    ItemStack copy = is.copy();
                    copy.setItemDamage(innerMeta);
                    if (((TileEntityMultiBlock) tile).setInnerBlock(world, hitX, hitY, hitZ, side, copy)) {
                        if (!par5EntityPlayer.capabilities.isCreativeMode) {
                            is.stackSize--;
                        }
                        world.markBlockForUpdate(x, y, z);
                    }
                } else {
                    ItemStack res = ((TileEntityMultiBlock) tile).removeInnerBlock(hitX, hitY, hitZ, side);
                    if (res != null) {
                        if (!par5EntityPlayer.capabilities.isCreativeMode) {
                            this.dropBlockAsItem(world, x, y, z, res);
                        }
                        world.markBlockForUpdate(x, y, z);
                    }
                }
            }
        }
        return true;
    }

    public boolean canUse(EntityPlayer player) {
        if (Config.multiBlockRestraint == 0) {
            return true;
        } else if (Config.multiBlockRestraint == 1) {
            return player.capabilities.isCreativeMode;
        } else if (Config.multiBlockRestraint == 2) {
            return player.capabilities.isCreativeMode || player.canCommandSenderUseCommand(MinecraftServer.getServer().getOpPermissionLevel(), "stop");
        } else if (Config.multiBlockRestraint == 3) {
            return player.canCommandSenderUseCommand(MinecraftServer.getServer().getOpPermissionLevel(), "stop");
        }
        return false;
    }

    public boolean canPlaceBlock(Block block) {
        return (block.getRenderType() == 0 && isCube(block)) || block.isNormalCube();
    }

    public boolean isCube(Block block) {
        return block.getBlockBoundsMinX() == 0 && block.getBlockBoundsMinY() == 0 && block.getBlockBoundsMinZ() == 0 && block.getBlockBoundsMaxX() == 1 && block.getBlockBoundsMaxY() == 1 && block.getBlockBoundsMaxZ() == 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
        par3List.add(new ItemStack(par1, 1, 4));
        par3List.add(new ItemStack(par1, 1, 5));
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB bb, List list, Entity entiyt) {

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityMultiBlock) {
            int meta = world.getBlockMetadata(x, y, z);
            if (((TileEntityMultiBlock) tile).isEmpty()) {
                this.setBoundsForMeta(meta);
                super.addCollisionBoxesToList(world, x, y, z, bb, list, entiyt);
            } else {
                float f = 1 / (float) ((TileEntityMultiBlock) tile).getFieldSize();
                for (int innerX = 0; innerX < ((TileEntityMultiBlock) tile).getFieldSize(); innerX++) {
                    for (int innerY = 0; innerY < ((TileEntityMultiBlock) tile).getFieldSize(); innerY++) {
                        for (int innerZ = 0; innerZ < ((TileEntityMultiBlock) tile).getFieldSize(); innerZ++) {
                            if (((TileEntityMultiBlock) tile).isExist(innerX, innerY, innerZ)) {
                                this.setBlockBounds(f * innerX, f * innerY, f * innerZ, f * innerX + f, f * innerY + f, f * innerZ + f);
                                super.addCollisionBoxesToList(world, x, y, z, bb, list, entiyt);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 vec3Start, Vec3 vec3End) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityMultiBlock) {
            MovingObjectPosition[] mop = new MovingObjectPosition[(int) Math.pow(((TileEntityMultiBlock) tile).getFieldSize(), 3) + 1];
            int count = 0;
            if (((TileEntityMultiBlock) tile).isEmpty()) {
                this.setBoundsForMeta(tile.getBlockMetadata());
                mop[count++] = super.collisionRayTrace(world, x, y, z, vec3Start, vec3End);
            } else {
                float f = 1 / (float) ((TileEntityMultiBlock) tile).getFieldSize();
                for (int innerX = 0; innerX < ((TileEntityMultiBlock) tile).getFieldSize(); innerX++) {
                    for (int innerY = 0; innerY < ((TileEntityMultiBlock) tile).getFieldSize(); innerY++) {
                        for (int innerZ = 0; innerZ < ((TileEntityMultiBlock) tile).getFieldSize(); innerZ++) {
                            if (((TileEntityMultiBlock) tile).isExist(innerX, innerY, innerZ)) {
                                this.setBlockBounds(f * innerX, f * innerY, f * innerZ, f * innerX + f, f * innerY + f, f * innerZ + f);
                                mop[count++] = super.collisionRayTrace(world, x, y, z, vec3Start, vec3End);
                            }
                        }
                    }
                }
            }
            double d1 = 0.0D;
            int i2 = count;
            MovingObjectPosition movingobjectposition1 = null;
            for (int j2 = 0; j2 < i2; ++j2) {
                MovingObjectPosition movingobjectposition = mop[j2];

                if (movingobjectposition != null) {
                    double d0 = movingobjectposition.hitVec.squareDistanceTo(vec3End);

                    if (d0 > d1) {
                        movingobjectposition1 = movingobjectposition;
                        d1 = d0;
                    }
                }
            }
            return movingobjectposition1;
        }
        return super.collisionRayTrace(world, x, y, z, vec3Start, vec3End);
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBoundsForMeta(world.getBlockMetadata(x, y, z));
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    public void setBoundsForMeta(int meta) {
        float f = 0.001F;
        switch (ForgeDirection.VALID_DIRECTIONS[meta]) {
        case DOWN:
            this.setBlockBounds(0, 0, 0, 1, f, 1);
            break;
        case EAST:
            this.setBlockBounds(1 - f, 0, 0, 1, 1, 1);
            break;
        case NORTH:
            this.setBlockBounds(0, 0, 0, 1, 1, f);
            break;
        case SOUTH:
            this.setBlockBounds(0, 0, 1 - f, 1, 1, 1);
            break;
        case UP:
            this.setBlockBounds(0, 1 - f, 0, 1, 1F, 1);
            break;
        case WEST:
            this.setBlockBounds(0, 0, 0, f, 1, 1);
            break;
        default:
            this.setBlockBounds(0, 0, 0, 1, 1, 1);
            break;
        }
    }

    @Override
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
        return ForgeDirection.OPPOSITES[par5];
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void setInnerPos(byte x, byte y, byte z, byte renderSide) {
        this.innerX = x;
        this.innerY = y;
        this.innerZ = z;
        this.renderSide = renderSide;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
        TileEntity tile = iBlockAccess.getTileEntity(x, y, z);
        if (tile instanceof TileEntityMultiBlock) {
            TileEntityMultiBlock tileMulti = (TileEntityMultiBlock) tile;
            if (!((TileEntityMultiBlock) tile).isEmpty()) {
                return tileMulti.getInnerBlock(innerX, innerY, innerZ).getIcon(side, tileMulti.getInnerMeta(innerX, innerY, innerZ));
            }
        }
        return this.blockIcon;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return (par5 != 1) && (((renderSide >> par5) & 1) == 1);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityMultiBlock();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.blockIcon = p_149651_1_.registerIcon(BambooCore.resourceDomain + "multiblock");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getItemIconName() {
        return BambooCore.resourceDomain + "multiblock";
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        ItemStack is = super.getPickBlock(target, world, x, y, z);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityMultiBlock) {
            is.setItemDamage(((TileEntityMultiBlock) tile).getFieldSize());
            is.stackTagCompound = new NBTTagCompound();
            ((TileEntityMultiBlock) tile).writeToSlotNBT(is.stackTagCompound);
        } else {
            is.setItemDamage(3);
        }
        return is;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float dropRate, int fortune) {
    }
}
