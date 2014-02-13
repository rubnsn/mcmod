package ruby.bamboo.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

public class BlockKawaraStair extends BlockStairs {
    public BlockKawaraStair(int par1, Block par2Block, int par3) {
        super(par1, par2Block, par3);
        useNeighborBrightness[par1] = true;
    }
}
