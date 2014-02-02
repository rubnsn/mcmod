package ruby.bamboo.block;

import java.util.ArrayList;

import ruby.bamboo.BambooInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockRicePlant extends BlockCrops {
    public BlockRicePlant(int par1) {
        super(par1);
    }

    @Override
    protected boolean canThisPlantGrowOnThisBlockID(int par1) {
        return par1 == Block.tilledField.blockID;
    }

    @Override
    protected int getSeedItem() {
        return BambooInit.seedRiceIID;
    }

    @Override
    protected int getCropItem() {
        return BambooInit.strawIID;
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = super.getBlockDropped(world, x, y, z, metadata, fortune);
        if (metadata >= 7) {
            ret.add(new ItemStack(this.getSeedItem(), 1, 0));
        }
        return ret;
    }
}
