package ruby.bamboo.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import ruby.bamboo.BambooInit;

public class BlockRicePlant extends BlockGrowableBase {

    public BlockRicePlant() {
        super();
        float f = 0.5F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
    }

    @Override
    public Item getSeed() {
        return BambooInit.riceSeed;
    }

    @Override
    public Item getProduct() {
        return BambooInit.straw;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);
        if (metadata >= this.getMaxGrowthStage() - 1) {
            ret.add(new ItemStack(this.getSeed(), 1, 0));
        }
        return ret;
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
        return new ItemStack(BambooInit.riceSeed);
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
