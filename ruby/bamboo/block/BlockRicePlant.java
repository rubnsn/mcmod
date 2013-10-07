package ruby.bamboo.block;

import ruby.bamboo.BambooInit;
import net.minecraft.block.BlockCrops;

public class BlockRicePlant extends BlockCrops
{
    public BlockRicePlant(int par1)
    {
        super(par1);
    }
    @Override
    protected boolean canThisPlantGrowOnThisBlockID(int par1)
    {
        return par1 == BambooInit.riceFieldBID;
    }
    @Override
    protected int getSeedItem()
    {
        return BambooInit.seedRiceIID;
    }

    @Override
    protected int getCropItem()
    {
        return BambooInit.strawIID;
    }
}
