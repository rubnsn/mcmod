package ruby.bamboo.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

public class BlockKawaraStair extends BlockStairs {
    public BlockKawaraStair(Block par2Block, int par3) {
        super(par2Block, par3);
        useNeighborBrightness = true;
    }
}
