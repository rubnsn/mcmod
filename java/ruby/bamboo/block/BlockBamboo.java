package ruby.bamboo.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import ruby.bamboo.BambooInit;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BlockBamboo extends Block {
    // 最大成長値(0も含まれる)
    private final int MAX_BAMBOO_LENGTH;
    private final int renderType;

    public BlockBamboo(int maxLength, int renderType) {
        super(MaterialBamboo.instance);
        this.MAX_BAMBOO_LENGTH = maxLength;
        this.renderType = renderType;
        setLightOpacity(0);
        setTickRandomly(true);
        setHardness(0F);
        setResistance(0F);
        setBlockBounds(0.5F - 0.375F, 0.0F, 0.5F - 0.375F, 0.5F + 0.375F, 1.0F, 0.5F + 0.375F);
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
                            growBamboo(world, x, y, z, meta);
                        } else {
                            if (world.isRaining() || world.rand.nextFloat() < probability) {
                                tryChildSpawn(world, x, y, z);
                            }
                        }
                    }
                }
            }
        }
    }

    void growBamboo(World world, int x, int y, int z, int meta) {
        world.setBlock(x, y + 1, z, this, meta + 1, 3);
    }

    void tryChildSpawn(World world, int i, int j, int k) {
        if (!world.isRemote) {
            j -= MAX_BAMBOO_LENGTH;

            for (int i1 = -1; i1 <= 1; i1++) {
                for (int j1 = -1; j1 <= 1; j1++) {
                    for (int k1 = -1; k1 <= 1; k1++) {
                        if (canChildSpawn(world, i + i1, j + j1, k + k1, world.rand)) {
                            world.setBlock(i + i1, j + j1 - 1, k + k1, Blocks.dirt, 0, 3);
                            world.setBlock(i + i1, j + j1, k + k1, BambooInit.bambooShoot, 15, 3);
                        }
                    }
                }
            }
        }
    }

    boolean canChildSpawn(World world, int i, int j, int k, Random random) {
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
            dropBlockAsItem(world, i, j, k, new ItemStack(BambooInit.itembamboo));
            world.setBlockToAir(i, j, k);
        }
    }

    @Override
    public boolean canBlockStay(World world, int i, int j, int k) {
        Block block = world.getBlock(i, j - 1, k);

        if (block == this) {
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
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return renderType;
    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }
}
