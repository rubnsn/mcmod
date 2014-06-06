package ruby.bamboo.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockMoss extends Block {

    public BlockMoss() {
        super(Material.ground);
        setTickRandomly(true);
        setStepSound(soundTypeGrass);
        setHardness(0.6F);
    }

    @Override
    public void updateTick(World world, int posX, int posY, int posZ, Random rand) {
        if (rand.nextFloat() < 0.5F) {
            if (world.getBlockLightValue(posX, posY + 1, posZ) >= 9) {
                int i1 = posX + rand.nextInt(3) - 1;
                int j1 = posY + rand.nextInt(5) - 3;
                int k1 = posZ + rand.nextInt(3) - 1;

                if (world.getBlockMetadata(i1, j1, k1) == 0 && world.getBlockLightValue(i1, j1 + 1, k1) >= 4 && world.getBlockLightOpacity(i1, j1 + 1, k1) <= 2) {
                    if (world.getBlock(i1, j1, k1) == Blocks.dirt) {
                        world.setBlock(i1, j1, k1, this);
                    } else if (rand.nextFloat() < 0.1F) {
                        Block block = world.getBlock(i1, j1, k1);
                        if (block == Blocks.cobblestone) {
                            world.setBlock(i1, j1, k1, Blocks.mossy_cobblestone);
                        } else if (block == Blocks.cobblestone_wall) {
                            world.setBlockMetadataWithNotify(i1, j1, k1, 1, 3);
                        } else if (block == Blocks.stonebrick) {
                            world.setBlockMetadataWithNotify(i1, j1, k1, 1, 3);
                        }
                    }
                }

            }
        }
    }
}
