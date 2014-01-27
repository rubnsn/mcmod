package ruby.bamboo.block;

import ruby.bamboo.BambooInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

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
}
