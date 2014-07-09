package ruby.bamboo.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import ruby.bamboo.BambooInit;

public class BlockBeanPlant extends BlockGrowableBase {
    @Override
    public Item getSeed() {
        return BambooInit.bean;
    }

    @Override
    public Item getProduct() {
        return BambooInit.bean;
    }

    @Override
    public float getGrowRate(World world, int x, int y, int z) {
        return super.getGrowRate(world, x, y, z) * 0.625F;
    }

    @Override
    public void tryBonemealGrow(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z) + MathHelper.getRandomIntegerInRange(world.rand, 1, 2);
        if (meta > this.getMaxGrowthStage() - 1) {
            meta = this.getMaxGrowthStage() - 1;
        }
        world.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(BambooInit.bean);
    }

    @Override
    public int getMaxGrowthStage() {
        return 5;
    }

    @Override
    public boolean canPlaceBlockOn(Block block) {
        return block == Blocks.farmland;
    }

    @Override
    public int getRenderType() {
        return 6;
    }

}
