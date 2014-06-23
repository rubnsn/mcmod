package ruby.bamboo.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import ruby.bamboo.BambooInit;

public class BlockSeaweedPlant extends BlockGrowableBase {

    @Override
    public Item getSeed() {
        return BambooInit.seaweedSeed;
    }

    @Override
    public Item getProduct() {
        return BambooInit.itemSeaweed;
    }

    @Override
    public int getMaxGrowthStage() {
        return 3;
    }

    @Override
    public boolean canPlaceBlockOn(Block block) {
        return block == Blocks.sand;
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        boolean hasWater = world.getBlock(x + 1, y - 1, z).getMaterial() == Material.water || world.getBlock(x - 1, y - 1, z).getMaterial() == Material.water || world.getBlock(x, y - 1, z + 1).getMaterial() == Material.water || world.getBlock(x, y - 1, z - 1).getMaterial() == Material.water;
        return world.getBlock(x, y - 1, z) == Blocks.sand && hasWater;
    }

    @Override
    public void tryBonemealGrow(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z) + MathHelper.getRandomIntegerInRange(world.rand, 1, 1);
        if (meta > this.getMaxGrowthStage() - 1) {
            meta = this.getMaxGrowthStage() - 1;
        }
        world.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }

    @Override
    public float getGrowRate(World world, int x, int y, int z) {
        return super.getGrowRate(world, x, y, z) * 0.4F;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(BambooInit.seaweedSeed);
    }
}
