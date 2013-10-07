package ruby.bamboo.block;

import java.util.Random;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ruby.bamboo.entity.EntityWhiteSmokeFX;
import ruby.bamboo.tileentity.TileEntitySpa;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSpaWater extends BlockContainer
{
    private static int dyePattern[] = new int[16];
    static
    {
        dyePattern[1] = 0x000404;
        dyePattern[2] = 0x040004;
        dyePattern[3] = 0x040808;
        dyePattern[4] = 0x040400;
        dyePattern[5] = 0x000400;
        dyePattern[6] = 0x040202;
        dyePattern[7] = 0x010101;
        dyePattern[8] = 0x020202;
        dyePattern[9] = 0x020404;
        dyePattern[10] = 0x040204;
        dyePattern[11] = 0x000002;
        dyePattern[12] = 0x040402;
        dyePattern[13] = 0x040406;
        dyePattern[14] = 0x000004;
        dyePattern[15] = 0x000000;
    }
    public BlockSpaWater(int i, Material material)
    {
        super(i, material);
        float f = 0.0F;
        float f1 = 0.0F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        setTickRandomly(true);
        disableStats();
        setBlockUnbreakable();
        setResistance(6000000F);
        setLightOpacity(0);
    }
    @Override
    public int getBlockColor()
    {
        return 0xffffff;
    }
    private int getWaterColor(IBlockAccess iblockaccess, int i, int j, int k)
    {
        TileEntitySpa tsp = getParentEntity(iblockaccess, i, j, k);

        if (tsp != null)
        {
            return tsp.getColor();
        }

        return 0xffffff;
    }
    private TileEntitySpa getParentEntity(IBlockAccess iblockaccess, int i, int j, int k)
    {
        TileEntitySpa tesChild = (TileEntitySpa) iblockaccess.getBlockTileEntity(i, j, k);

        if (tesChild != null)
        {
            TileEntity parent = iblockaccess.getBlockTileEntity(tesChild.getX(), tesChild.getY(), tesChild.getZ());

            if (parent instanceof TileEntitySpa)
            {
                return (TileEntitySpa) parent;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        if (!world.isRemote)
        {
            if (entity instanceof EntityLivingBase)
            {
                ((TileEntitySpa)world.getBlockTileEntity(i, j, k)).onCollisionEntity((EntityLivingBase) entity);
            }
            else if (entity instanceof EntityItem)
            {
                if (((EntityItem) entity).getEntityItem().itemID == Item.dyePowder.itemID)
                {
                    TileEntitySpa parent = getParentEntity(world, i, j, k);

                    if (parent == null)
                    {
                        return;
                    }

                    while (((EntityItem) entity).getEntityItem().stackSize-- > 0)
                    {
                        parent.addColor(dyePattern[((EntityItem) entity).getEntityItem().getItemDamage()]);
                    }

                    if (world.getBlockMetadata(parent.xCoord, parent.yCoord, parent.zCoord) == 0)
                    {
                        world.setBlockMetadataWithNotify(parent.xCoord, parent.yCoord, parent.zCoord, world.getBlockMetadata(parent.xCoord, parent.yCoord, parent.zCoord) + 1, 3);
                        world.scheduleBlockUpdate(parent.xCoord, parent.yCoord, parent.zCoord, this.blockID, 0);
                    }
                }
            }
        }
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("wool_colored_white");
    }
    @Override
    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        return getWaterColor(iblockaccess, i, j, k);
    }

    private boolean canStay(World world, TileEntitySpa ts, int i, int j, int k)
    {
        if (ts.getOffset(i, j, k) > 16)
        {
            return false;
        }

        if (world.isAirBlock(i, j - 1, k))
        {
            return false;
        }

        return true;
    }
    private void spread(World world, int i, int j, int k, Random random)
    {
        TileEntitySpa parent = getParentEntity(world, i, j, k);

        if (world.getBlockId(i, j - 1, k) == blockID)
        {
            return;
        }

        if (parent == null || !parent.isStay())
        {
            addMeta(world, i, j, k, 1);
            return;
        }

        if (world.getBlockMetadata(i, j, k) < 7)
        {
            int pmeta = world.getBlockMetadata(i, j, k);
            int cx = i;
            int cy = j;
            int cz = k;

            if (world.isAirBlock(i, j - 1, k))
            {
                cy--;

                if (cy < 0)
                {
                    return;
                }

                addMeta(world, i, j, k, 1);
                world.setBlock(cx, cy, cz, blockID, 0, 3);
                setLocation(world, cx, cy, cz, parent.getX(), parent.getY(), parent.getZ());
                return;
            }

            switch (random.nextInt(4))
            {
                case 0:
                    cx++;
                    break;

                case 1:
                    cx--;
                    break;

                case 2:
                    cz++;
                    break;

                case 3:
                    cz--;
                    break;
            }

            if (world.getBlockId(cx, cy, cz) == blockID)
            {
                if (world.getBlockMetadata(i, j, k) < world.getBlockMetadata(cx, cy, cz))
                {
                    addMeta(world, cx, cy, cz, -1);
                }
            }
            else if (world.isAirBlock(cx, cy, cz) && !world.isAirBlock(cx, cy - 1, cz))
            {
                addMeta(world, i, j, k, 1);
                world.setBlock(cx, cy, cz, blockID, 7, 3);
                setLocation(world, cx, cy, cz, parent.getX(), parent.getY(), parent.getZ());
            }
            else if (world.isAirBlock(cx, cy, cz) && world.isAirBlock(cx, cy - 1, cz))
            {
                addMeta(world, i, j, k, 1);
                world.setBlock(cx, cy, cz, blockID, 7, 3);
                setLocation(world, cx, cy, cz, parent.getX(), parent.getY(), parent.getZ());
            }
        }
    }
    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
    }
    @Override
    protected void initializeBlock()
    {
    }
    private boolean addMeta(World world, int i, int j, int k, int l)
    {
        if (((world.getBlockMetadata(i, j, k) + l) >= 0) && ((world.getBlockMetadata(i, j, k) + l) < 8))
        {
            world.setBlockMetadataWithNotify(i, j, k, world.getBlockMetadata(i, j, k) + l, 3);
            return true;
        }
        else if (l > 0)
        {
            world.setBlock(i, j, k, 0, 0, 3);
            return true;
        }

        return false;
    }
    @Override
    public int tickRate(World par1World)
    {
        return 20;
    }
    @Override
    public int getRenderType()
    {
        return 4;
    }
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    @Override
    public Icon getIcon(int par1, int par2)
    {
        return Block.waterStill.getIcon(par1, par2);
    }
    @Override
    public int idDropped(int i, Random random, int j)
    {
        return 0;
    }
    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }
    @Override
    public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return false;
    }
    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        Material material = iblockaccess.getBlockMaterial(i, j, k);

        if (material == blockMaterial)
        {
            return false;
        }

        if (l == 1)
        {
            return true;
        }

        return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
    }
    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par5Random.nextInt(10) == 0 && par1World.isAirBlock(par2, par3 + 1, par4))
        {
        	FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityWhiteSmokeFX(par1World, par2 + 0.5, par3 + 1, par4 + 0.5, 0.0D, 0.0D, 0.0D, 1F));
            int l = par1World.getBlockMetadata(par2, par3, par4);

            if (l <= 0 || l >= 8)
            {
                par1World.spawnParticle("suspended", par2 + par5Random.nextFloat(), par3 + par5Random.nextFloat(), par4 + par5Random.nextFloat(), 0.0D, 0.0D, 0.0D);
            }
        }

        for (int i1 = 0; i1 < 0; i1++)
        {
            int k1 = par5Random.nextInt(4);
            int l1 = par2;
            int i2 = par4;

            if (k1 == 0)
            {
                l1--;
            }

            if (k1 == 1)
            {
                l1++;
            }

            if (k1 == 2)
            {
                i2--;
            }

            if (k1 == 3)
            {
                i2++;
            }

            if (par1World.getBlockMaterial(l1, par3, i2) != Material.air || !par1World.getBlockMaterial(l1, par3 - 1, i2).blocksMovement() && !par1World.getBlockMaterial(l1, par3 - 1, i2).isLiquid())
            {
                continue;
            }

            float f = 0.0625F;
            double d6 = par2 + par5Random.nextFloat();
            double d7 = par3 + par5Random.nextFloat();
            double d8 = par4 + par5Random.nextFloat();

            if (k1 == 0)
            {
                d6 = par2 - f;
            }

            if (k1 == 1)
            {
                d6 = par2 + 1 + f;
            }

            if (k1 == 2)
            {
                d8 = par4 - f;
            }

            if (k1 == 3)
            {
                d8 = par4 + 1 + f;
            }

            double d9 = 0.0D;
            double d10 = 0.0D;

            if (k1 == 0)
            {
                d9 = -f;
            }

            if (k1 == 1)
            {
                d9 = f;
            }

            if (k1 == 2)
            {
                d10 = -f;
            }

            if (k1 == 3)
            {
                d10 = f;
            }

            par1World.spawnParticle("splash", d6, d7, d8, d9, 0.0D, d10);
        }

        if (par5Random.nextInt(64) == 0)
        {
            int j1 = par1World.getBlockMetadata(par2, par3, par4);

            if (j1 > 0 && j1 < 8)
            {
                par1World.playSoundEffect(par2 + 0.5F, par3 + 0.5F, par4 + 0.5F, "liquid.water", par5Random.nextFloat() * 0.25F + 0.75F, par5Random.nextFloat() * 1.0F + 0.5F);
            }
        }

        if (par5Random.nextInt(10) == 0 && par1World.isBlockNormalCube(par2, par3 - 1, par4) && !par1World.getBlockMaterial(par2, par3 - 2, par4).isSolid())
        {
            double d1 = par2 + par5Random.nextFloat();
            double d3 = par3 - 1.05D;
            double d5 = par4 + par5Random.nextFloat();
            par1World.spawnParticle("dripWater", d1, d3, d5, 0.0D, 0.0D, 0.0D);
        }
    }
    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        spread(world, i, j, k, world.rand);

        if (world.getBlockMetadata(i, j, k) != 0)
        {
            world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
        }
        else
        {
            //world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate()+200);
        }
    }
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
    }
    public void setLocation(World world, int i, int j, int k, int i2, int j2, int k2)
    {
        ((TileEntitySpa)world.getBlockTileEntity(i, j, k)).setLocation(i2, j2, k2);
    }
    @Override
    public TileEntity createNewTileEntity(World var1)
    {
        return new TileEntitySpa();
    }
}
