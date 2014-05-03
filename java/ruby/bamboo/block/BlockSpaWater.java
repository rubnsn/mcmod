package ruby.bamboo.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import ruby.bamboo.entity.EntityWhiteSmokeFX;
import ruby.bamboo.tileentity.spa.ITileEntitySpa;
import ruby.bamboo.tileentity.spa.TileEntitySpaChild;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSpaWater extends BlockLiquid implements ITileEntityProvider {
    private static final ForgeDirection[] directions;
    static {
        directions = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST };
    }

    public BlockSpaWater() {
        super(Material.water);
        float f = 0.0F;
        float f1 = 0.0F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        setTickRandomly(true);
        disableStats();
        setBlockUnbreakable();
        setResistance(6000000F);
        setLightOpacity(0);
        this.isBlockContainer = true;
    }

    @Override
    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
        p_149749_1_.removeTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
    }

    @Override
    public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_, int p_149696_5_, int p_149696_6_) {
        super.onBlockEventReceived(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_5_, p_149696_6_);
        TileEntity tileentity = p_149696_1_.getTileEntity(p_149696_2_, p_149696_3_, p_149696_4_);
        return tileentity != null ? tileentity.receiveClientEvent(p_149696_5_, p_149696_6_) : false;
    }

    @Override
    public int getBlockColor() {
        return 0xffffff;
    }

    private int getWaterColor(IBlockAccess iblockaccess, int posX, int posY, int posZ) {
        ITileEntitySpa tsp = (ITileEntitySpa) iblockaccess.getTileEntity(posX, posY, posZ);
        if (tsp != null) {
            return tsp.getColor();
        }
        return 0xffffff;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int posX, int posY, int posZ, Entity entity) {

        if (!world.isRemote) {
            if (entity instanceof EntityLivingBase) {
                ((ITileEntitySpa) world.getTileEntity(posX, posY, posZ)).onEntityLivingCollision((EntityLivingBase) entity);
            } else if (entity instanceof EntityItem) {
                ((ITileEntitySpa) world.getTileEntity(posX, posY, posZ)).onEntityItemCollision((EntityItem) entity);
                /*if (((EntityItem) entity).getEntityItem().itemID == Item.dyePowder.itemID) {
                    ITileEntitySpa tsp = (ITileEntitySpa) world.getBlockTileEntity(posX, posY, posZ);
                    if (!world.isRemote) {
                        if (tsp != null) {
                            while (((EntityItem) entity).getEntityItem().stackSize-- > 0) {
                                tsp.addColor(dyePattern[((EntityItem) entity).getEntityItem().getItemDamage()]);
                            }
                        }
                    }
                    tsp.colorUpdate();
                }*/
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon("wool_colored_white");
    }

    @Override
    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
        return getWaterColor(iblockaccess, i, j, k);
    }

    @Override
    public void updateTick(World world, int posX, int posY, int posZ, Random random) {
        if (!world.isRemote) {
            ITileEntitySpa tileSpa = (ITileEntitySpa) world.getTileEntity(posX, posY, posZ);
            if (tileSpa == null) {
                world.setBlockToAir(posX, posY, posZ);
            } else {
                int meta = world.getBlockMetadata(posX, posY, posZ);

                if (meta != tileSpa.getLastTickMeta()) {
                    this.setTickSchedule(world, posX, posY, posZ, tileSpa);
                    tileSpa.setLastTickMeta(meta);
                } else {
                    tileSpa.setTickSchedule(false);
                }

                if (tileSpa.isStay()) {
                    if (world.getBlock(posX, posY - 1, posZ) != this) {
                        this.spread(world, posX, posY, posZ, meta, world.rand);
                    }
                } else {
                    this.waterLevelDown(world, posX, posY, posZ);
                }
            }
        }

    }

    private void setTickSchedule(World world, int posX, int posY, int posZ, ITileEntitySpa tileSpa) {
        world.scheduleBlockUpdate(posX, posY, posZ, this, this.tickRate(world));
        tileSpa.setTickSchedule(true);
    }

    private void spread(World world, int posX, int posY, int posZ, int meta, Random random) {
        if (canSpread(world, posX, posY, posZ)) {
            if (this.isDirOffsettedIsAirBlock(world, posX, posY, posZ, ForgeDirection.DOWN)) {
                if (posY >= 0) {
                    this.setThisChildBlock(world, posX, posY, posZ, 0, ForgeDirection.DOWN);
                }
            } else {
                ForgeDirection dirTravel = this.getDirTravel(world, posX, posY, posZ);

                if (this.getDirOffsettedBlockID(world, posX, posY, posZ, dirTravel) == this) {
                    if (meta < this.getDirOffsettedMetadata(world, posX, posY, posZ, dirTravel)) {
                        this.waterLevelUP(world, posX, posY, posZ, dirTravel);
                    }
                } else if (this.isDirOffsettedIsAirBlock(world, posX, posY, posZ, dirTravel)) {
                    this.setThisChildBlock(world, posX, posY, posZ, dirTravel);
                }
            }
        }
    }

    private ForgeDirection getDirTravel(World world, int posX, int posY, int posZ) {
        ForgeDirection resultDir = ForgeDirection.NORTH;
        int meta = 0;
        int targetMeta;
        for (ForgeDirection dir : directions) {
            if (this.isDirOffsettedIsAirBlock(world, posX, posY, posZ, dir)) {
                resultDir = dir;
                break;
            } else {
                targetMeta = this.getDirOffsettedMetadata(world, posX, posY, posZ, dir);
                if (this.getDirOffsettedBlockID(world, posX, posY, posZ, dir) == this && meta < targetMeta) {
                    resultDir = dir;
                    meta = targetMeta;
                }
            }
        }
        return resultDir;
    }

    private boolean isDirOffsettedIsAirBlock(World world, int posX, int posY, int posZ, ForgeDirection dir) {
        return world.isAirBlock(posX + dir.offsetX, posY + dir.offsetY, posZ + dir.offsetZ);
    }

    private Block getDirOffsettedBlockID(World world, int posX, int posY, int posZ, ForgeDirection dir) {
        return world.getBlock(posX + dir.offsetX, posY + dir.offsetY, posZ + dir.offsetZ);
    }

    private int getDirOffsettedMetadata(World world, int posX, int posY, int posZ, ForgeDirection dir) {
        return world.getBlockMetadata(posX + dir.offsetX, posY + dir.offsetY, posZ + dir.offsetZ);
    }

    private void setThisChildBlock(World world, int posX, int posY, int posZ, int amount, ForgeDirection dir) {
        world.setBlock(posX + dir.offsetX, posY + dir.offsetY, posZ + dir.offsetZ, this, amount, 3);
        ((TileEntitySpaChild) world.getTileEntity(posX + dir.offsetX, posY + dir.offsetY, posZ + dir.offsetZ)).setParentPosition(((ITileEntitySpa) world.getTileEntity(posX, posY, posZ)).getParentPosition());
    }

    private void setThisChildBlock(World world, int posX, int posY, int posZ, ForgeDirection dir) {
        this.setThisChildBlock(world, posX, posY, posZ, 7, dir);
    }

    private boolean canSpread(World world, int posX, int posY, int posZ) {
        return world.getBlockMetadata(posX, posY, posZ) < 7;
    }

    private void waterLevelUP(World world, int posX, int posY, int posZ) {
        int meta = world.getBlockMetadata(posX, posY, posZ);
        if (meta > 0) {
            world.setBlockMetadataWithNotify(posX, posY, posZ, meta - 1, 3);
        }
    }

    private void waterLevelUP(World world, int posX, int posY, int posZ, ForgeDirection dir) {
        this.waterLevelUP(world, posX + dir.offsetX, posY + dir.offsetY, posZ + dir.offsetZ);
    }

    private void waterLevelDown(World world, int posX, int posY, int posZ) {
        int meta = world.getBlockMetadata(posX, posY, posZ);
        if (meta < 7) {
            world.setBlockMetadataWithNotify(posX, posY, posZ, meta + 1, 3);
        } else {
            world.setBlockToAir(posX, posY, posZ);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int posX, int posY, int posZ, Block par5) {
        if (!world.isRemote) {
            ITileEntitySpa tileSpa = (ITileEntitySpa) world.getTileEntity(posX, posY, posZ);
            if (tileSpa != null && !tileSpa.isTickScheduled()) {
                this.setTickSchedule(world, posX, posY, posZ, tileSpa);
            }
        }
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        par1World.scheduleBlockUpdate(par2, par3, par4, this, this.tickRate(par1World));
    }

    @Override
    public int tickRate(World par1World) {
        return 20;
    }

    /*
     * 水流部分は0～7(水位高～低)まで、垂直部分は8？
     */
    @Override
    public int getRenderType() {
        return 4;
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
    public IIcon getIcon(int par1, int par2) {
        return Blocks.water.getIcon(par1, par2);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        Material material = iblockaccess.getBlock(i, j, k).getMaterial();

        if (material == blockMaterial) {
            return false;
        }

        if (l == 1) {
            return true;
        }

        return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (par5Random.nextInt(10) == 0 && par1World.isAirBlock(par2, par3 + 1, par4)) {
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityWhiteSmokeFX(par1World, par2 + 0.5, par3 + 1, par4 + 0.5, 0.0D, 0.0D, 0.0D, 1F));
            int l = par1World.getBlockMetadata(par2, par3, par4);

            if (l <= 0 || l >= 8) {
                par1World.spawnParticle("suspended", par2 + par5Random.nextFloat(), par3 + par5Random.nextFloat(), par4 + par5Random.nextFloat(), 0.0D, 0.0D, 0.0D);
            }
        }

        for (int i1 = 0; i1 < 0; i1++) {
            int k1 = par5Random.nextInt(4);
            int l1 = par2;
            int i2 = par4;

            if (k1 == 0) {
                l1--;
            }

            if (k1 == 1) {
                l1++;
            }

            if (k1 == 2) {
                i2--;
            }

            if (k1 == 3) {
                i2++;
            }

            if (par1World.getBlock(l1, par3, i2).getMaterial() != Material.air || !par1World.getBlock(l1, par3 - 1, i2).getMaterial().blocksMovement() && !par1World.getBlock(l1, par3 - 1, i2).getMaterial().isLiquid()) {
                continue;
            }

            float f = 0.0625F;
            double d6 = par2 + par5Random.nextFloat();
            double d7 = par3 + par5Random.nextFloat();
            double d8 = par4 + par5Random.nextFloat();

            if (k1 == 0) {
                d6 = par2 - f;
            }

            if (k1 == 1) {
                d6 = par2 + 1 + f;
            }

            if (k1 == 2) {
                d8 = par4 - f;
            }

            if (k1 == 3) {
                d8 = par4 + 1 + f;
            }

            double d9 = 0.0D;
            double d10 = 0.0D;

            if (k1 == 0) {
                d9 = -f;
            }

            if (k1 == 1) {
                d9 = f;
            }

            if (k1 == 2) {
                d10 = -f;
            }

            if (k1 == 3) {
                d10 = f;
            }

            par1World.spawnParticle("splash", d6, d7, d8, d9, 0.0D, d10);
        }

        if (par5Random.nextInt(64) == 0) {
            int j1 = par1World.getBlockMetadata(par2, par3, par4);

            if (j1 > 0 && j1 < 8) {
                par1World.playSoundEffect(par2 + 0.5F, par3 + 0.5F, par4 + 0.5F, "liquid.water", par5Random.nextFloat() * 0.25F + 0.75F, par5Random.nextFloat() * 1.0F + 0.5F);
            }
        }

        if (par5Random.nextInt(10) == 0 && par1World.isBlockNormalCubeDefault(par2, par3 - 1, par4, false) && !par1World.getBlock(par2, par3 - 2, par4).getMaterial().isSolid()) {
            double d1 = par2 + par5Random.nextFloat();
            double d3 = par3 - 1.05D;
            double d5 = par4 + par5Random.nextFloat();
            par1World.spawnParticle("dripWater", d1, d3, d5, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        return null;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
        return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntitySpaChild();
    }
}
