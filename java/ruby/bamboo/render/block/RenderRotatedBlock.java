package ruby.bamboo.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import ruby.bamboo.block.IRotateBlock;

public class RenderRotatedBlock implements IRenderBlocks, IRenderInventory {
    public static final RenderRotatedBlock instance = new RenderRotatedBlock();

    @Override
    public void renderBlock(RenderBlocks renderblocks, Block par1Block, int par2, int par3, int par4) {
        int meta = renderblocks.blockAccess.getBlockMetadata(par2, par3, par4);
        renderblocks.uvRotateSouth = ((IRotateBlock) par1Block).getuvRotateSouth(meta);
        renderblocks.uvRotateEast = ((IRotateBlock) par1Block).getuvRotateEast(meta);
        renderblocks.uvRotateWest = ((IRotateBlock) par1Block).getuvRotateWest(meta);
        renderblocks.uvRotateNorth = ((IRotateBlock) par1Block).getuvRotateNorth(meta);
        renderblocks.uvRotateTop = ((IRotateBlock) par1Block).getuvRotateTop(meta);
        renderblocks.uvRotateBottom = ((IRotateBlock) par1Block).getuvRotateBottom(meta);
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
