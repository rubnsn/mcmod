package ruby.bamboo.render.block;

import ruby.bamboo.block.BlockKitunebi;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderKitunebi implements IRenderBlocks {

    @Override
    public void render(RenderBlocks renderblocks, Block par1Block, int par2, int par3, int par4) {
        if (((BlockKitunebi) par1Block).isVisible()) {
            renderblocks.renderCrossedSquares(par1Block, par2, par3, par4);
        }
    }

}
