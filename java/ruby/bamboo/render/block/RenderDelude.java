package ruby.bamboo.render.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.RenderBlocks;
import ruby.bamboo.block.IDelude;

public class RenderDelude implements IRenderBlocks, IRenderInventory {
    public static final RenderDelude instance = new RenderDelude();

    @Override
    public void renderBlock(RenderBlocks renderblocks, Block par1Block, int par2, int par3, int par4) {
        boolean isGrass = false;

        if (renderblocks.getBlockIcon(par1Block, renderblocks.blockAccess, par2, par3, par4, 1).getIconName().equals("grass_top")) {
            ((IDelude) par1Block).setIconGrass(true);
            isGrass = true;
            RenderBlocks.fancyGrass = true;
        }
        switch (((IDelude) par1Block).getOriginalRenderType()) {
        case 0:
            renderblocks.renderStandardBlock(par1Block, par2, par3, par4);
            break;

        case 10:
            renderblocks.renderBlockStairs((BlockStairs) par1Block, par2, par3, par4);
            break;
        }

        if (isGrass) {
            ((IDelude) par1Block).setIconGrass(false);
            RenderBlocks.fancyGrass = false;
        }
    }

    @Override
    public void renderInventory(RenderBlocks renderBlocks, Block block, int metadata) {
        switch (((IDelude) block).getOriginalRenderType()) {
        case 0:
            RenderInventoryHelper.standardItemRender(block, renderBlocks, metadata);
            break;

        case 10:
            RenderInventoryHelper.renderItemStair(renderBlocks, block);
            break;
        }
    }

}
