package ruby.bamboo.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderQuadRotatedPillar implements IRenderBlocks, IRenderInventory {
    public static final RenderQuadRotatedPillar instance = new RenderQuadRotatedPillar();

    @Override
    public void renderBlock(RenderBlocks renderblocks, Block par1Block, int par2, int par3, int par4) {
        byte rotateMeta = (byte) (renderblocks.blockAccess.getBlockMetadata(par2, par3, par4) >> 2);
        renderblocks.uvRotateSouth = rotateMeta;
        renderblocks.uvRotateEast = rotateMeta;
        renderblocks.uvRotateWest = rotateMeta;
        renderblocks.uvRotateNorth = rotateMeta;
        renderblocks.uvRotateTop = rotateMeta;
        renderblocks.uvRotateBottom = rotateMeta;
        renderblocks.renderStandardBlock(par1Block, par2, par3, par4);
        renderblocks.uvRotateSouth = 0;
        renderblocks.uvRotateEast = 0;
        renderblocks.uvRotateWest = 0;
        renderblocks.uvRotateNorth = 0;
        renderblocks.uvRotateTop = 0;
        renderblocks.uvRotateBottom = 0;
    }

    @Override
    public void renderInventory(RenderBlocks renderBlocks, Block block, int metadata) {
        RenderInventoryHelper.standardItemRender(block, renderBlocks, metadata);
    }
}
