package ruby.bamboo.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import ruby.bamboo.CustomRenderHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBamboo extends Block {
    // 最大成長値(0も含まれる)
    private static final int MAX_BAMBOO_LENGTH = 9;
    private IIcon parent;
    private IIcon child;
    private final String chiledName;

    public BlockBamboo(String chiledName) {
        super(MaterialBamboo.instance);
        this.chiledName = chiledName;
        setLightOpacity(0);
        setTickRandomly(true);
        setHardness(0F);
        setResistance(0F);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBonemealEvent(BonemealEvent event) {
        if (event.block == this) {
            if (event.entityPlayer.capabilities.isCreativeMode) {
                event.setCanceled(true);
            }
            if (!event.world.isRemote) {
                event.world.playAuxSFX(2005, event.x, event.y, event.z, 0);
            }
            int y = event.y + 1;
            for (; event.world.getBlock(event.x, y, event.z) == this; y++) {
            }
            tryBambooGrowth(event.world, event.x, y - 1, event.z, 0.75F);
            event.setResult(Result.ALLOW);
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (meta != 15) {
            return parent;
        }

        return child;
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random) {
        tryBambooGrowth(world, i, j, k, world.isRaining() ? 0.25F : 0.125F);
    }

    private void tryBambooGrowth(World world, int x, int y, int z, float probability) {
        if (!world.isRemote) {
            if (world.isAirBlock(x, y + 1, z)) {
                if (world.rand.nextFloat() < probability) {
                    int meta = world.getBlockMetadata(x, y, z);

                    if (meta != 15) {
                        if (meta < MAX_BAMBOO_LENGTH) {
                            world.setBlock(x, y + 1, z, this, meta + 1, 3);
                        } else {
                            if (world.isRaining() || world.rand.nextFloat() < probability) {
                                tryChildSpawn(world, x, y, z);
                            }
                        }
                    } else {
                        if (canChildGrow(world, x, y, z)) {
                            world.setBlockMetadataWithNotify(x, y, z, 0, 3);
                        }
                    }
                }
            }
        }
    }

    private void tryChildSpawn(World world, int i, int j, int k) {
        if (!world.isRemote) {
            j -= MAX_BAMBOO_LENGTH;

            for (int i1 = -1; i1 <= 1; i1++) {
                for (int j1 = -1; j1 <= 1; j1++) {
                    for (int k1 = -1; k1 <= 1; k1++) {
                        if (canChildSpawn(world, i + i1, j + j1, k + k1, world.rand)) {
                            world.setBlock(i + i1, j + j1 - 1, k + k1, Blocks.dirt, 0, 3);
                            world.setBlock(i + i1, j + j1, k + k1, this, 15, 3);
                        }
                    }
                }
            }
        }
    }

    public static boolean canChildGrow(World world, int i, int j, int k) {
        Chunk chunk = world.getChunkFromChunkCoords(i >> 4, k >> 4);
        i &= 0xf;
        k &= 0xf;
        return chunk.getBlockLightValue(i, j, k, 15) > 7;
    }

    private boolean canChildSpawn(World world, int i, int j, int k, Random random) {
        if (world.isAirBlock(i, j, k)) {
            // 天候・耕地確変
            if (random.nextFloat() < (world.isRaining() ? 0.4F : world.getBlock(i, j - 1, k) == Blocks.farmland ? 0.25F : 0.1F)) {
                if ((world.getBlock(i, j - 1, k) == Blocks.dirt || world.getBlock(i, j - 1, k) == Blocks.grass || world.getBlock(i, j - 1, k) == Blocks.farmland)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
        if (!world.isRemote) {
            if (l == 15) {
                if (entityplayer.getCurrentEquippedItem() != null && (entityplayer.getCurrentEquippedItem().getItem() instanceof ItemHoe)) {
                    entityplayer.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
                    entityplayer.getCurrentEquippedItem().damageItem(1, entityplayer);
                    dropBlockAsItem(world, i, j, k, new ItemStack(BambooInit.takenoko, 1, 0));
                }
            } else {
                super.harvestBlock(world, entityplayer, i, j, k, l);
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k) {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        checkBlockCoordValid(world, x, y, z);
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
        if (par1World.getBlock(par2, par3 - par6, par4) == this) {
            dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(BambooInit.itembamboo));
            par1World.setBlockToAir(par2, par3 - par6, par4);
        }
    }

    protected final void checkBlockCoordValid(World world, int i, int j, int k) {
        if (!canBlockStay(world, i, j, k)) {
            if (world.getBlockMetadata(i, j, k) != 15) {
                dropBlockAsItem(world, i, j, k, new ItemStack(BambooInit.itembamboo));
            }

            world.setBlockToAir(i, j, k);
        }
    }

    @Override
    public boolean canBlockStay(World world, int i, int j, int k) {
        Block block = world.getBlock(i, j - 1, k);

        if (block == this && world.getBlockMetadata(i, j - 1, k) == 15) {
            return false;
        } else if (block == this) {
            return true;
        }

        if (block != Blocks.grass && block != Blocks.dirt) {
            return false;
        }

        return true;
    }

    @Override
    public Item getItemDropped(int par1, Random rand, int par3) {
        return BambooInit.itembamboo;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        return null;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        if (par1IBlockAccess.getBlockMetadata(par2, par3, par4) == 15) {
            setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.5F, 0.7F);
        } else {
            setBlockBounds(0.5F - 0.375F, 0.0F, 0.5F - 0.375F, 0.5F + 0.375F, 1.0F, 0.5F + 0.375F);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.parent = par1IconRegister.registerIcon(BambooCore.resourceDomain + "bamboo");
        this.child = par1IconRegister.registerIcon(BambooCore.resourceDomain + this.chiledName);
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
    public int getRenderType() {
        return CustomRenderHandler.bambooUID;
    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }
}
