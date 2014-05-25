package ruby.bamboo.block;

import java.util.ArrayList;

import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import ruby.bamboo.BambooInit;

public class BlockRicePlant extends BlockCrops {

    @Override
    protected Item func_149866_i() {
        return BambooInit.riceSeed;
    }

    @Override
    protected Item func_149865_P() {
        return BambooInit.straw;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);
        if (metadata >= 7) {
            ret.add(new ItemStack(this.func_149866_i(), 1, 0));
        }
        return ret;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(BambooInit.riceSeed);
    }
}
