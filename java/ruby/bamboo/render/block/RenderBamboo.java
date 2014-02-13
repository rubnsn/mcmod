package ruby.bamboo.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderBamboo implements IRenderBlocks {

    @Override
    public void render(RenderBlocks renderblocks, Block par1Block, int par2, int par3, int par4) {
        if (renderblocks.blockAccess.getBlockMetadata(par2, par3, par4) != 15) {
            renderblocks.renderBlockCrops(par1Block, par2, par3, par4);
        } else {
            renderblocks.renderCrossedSquares(par1Block, par2, par3, par4);
        }
    }

}
