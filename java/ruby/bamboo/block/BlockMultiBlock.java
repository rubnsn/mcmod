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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import ruby.bamboo.BambooCore;
import ruby.bamboo.CustomRenderHandler;
import ruby.bamboo.tileentity.TileEntityMultiBlock;
import ruby.bamboo.world.DummyWorld;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMultiBlock extends BlockContainer {

    private byte renderSide = 0x3F;
    private byte innerX;
    private byte innerY;
    private byte innerZ;
    public boolean isTopRender = false;
    private IIcon icon;

    public BlockMultiBlock() {
        super(Material.ground);
        this.setLightOpacity(0);
        this.setHardness(1.0F);
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.multiBlockUID;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        ItemStack is = par5EntityPlayer.getCurrentEquippedItem();
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityMultiBlock) {
            if (is != null && (Block.getBlockFromItem(is.getItem()).getRenderType() == 0 || Block.getBlockFromItem(is.getItem()).isNormalCube())) {
                Block block = Block.getBlockFromItem(is.getItem());
                DummyWorld.lastSetMeta = 0;
                block.onBlockPlacedBy(DummyWorld.dummyInstance, 0, 0, 0, par5EntityPlayer, is);
                int meta = block.onBlockPlaced(DummyWorld.dummyInstance, 0, 0, 0, par6, par7, par8, par9, is.getItemDamage());
                if (meta == 0) {
                    meta = DummyWorld.lastSetMeta;
                }
                ItemStack copy = is.copy();
                copy.setItemDamage(meta);
                if (((TileEntityMultiBlock) tile).setInnerBlock(par7, par8, par9, par6, copy)) {
                    if (!par5EntityPlayer.capabilities.isCreativeMode) {
                        is.stackSize--;
                    }
                    world.markBlockForUpdate(x, y, z);
                    return true;
                }
            } else {
                ItemStack res = ((TileEntityMultiBlock) tile).removeInnerBlock(par7, par8, par9, par6);
                if (res != null) {
                    if (!par5EntityPlayer.capabilities.isCreativeMode) {
                        this.dropBlockAsItem(world, x, y, z, res);
                    }
                    world.markBlockForUpdate(x, y, z);
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
        par3List.add(new ItemStack(par1, 1, 4));
        par3List.add(new ItemStack(par1, 1, 5));
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 0;
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
                for (int innerX = 0; innerX < ((TileEntityMultiBlock) tile).getFieldSize(); innerX++) {
                    for (int innerY = 0; innerY < ((TileEntityMultiBlock) tile).getFieldSize(); innerY++) {
                        for (int innerZ = 0; innerZ < ((TileEntityMultiBlock) tile).getFieldSize(); innerZ++) {
                            if (((TileEntityMultiBlock) tile).isExist(innerX, innerY, innerZ)) {
                                float f = 1 / (float) ((TileEntityMultiBlock) tile).getFieldSize();
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
                for (int innerX = 0; innerX < ((TileEntityMultiBlock) tile).getFieldSize(); innerX++) {
                    for (int innerY = 0; innerY < ((TileEntityMultiBlock) tile).getFieldSize(); innerY++) {
                        for (int innerZ = 0; innerZ < ((TileEntityMultiBlock) tile).getFieldSize(); innerZ++) {
                            if (((TileEntityMultiBlock) tile).isExist(innerX, innerY, innerZ)) {
                                float f = 1 / (float) ((TileEntityMultiBlock) tile).getFieldSize();
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
        return icon;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return (((renderSide >> par5) & 1) == 1) && (isTopRender || par5 != 1);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityMultiBlock();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.icon = p_149651_1_.registerIcon(BambooCore.resourceDomain + "multiblock");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getItemIconName() {
        return BambooCore.resourceDomain + "multiblock";
    }
}
