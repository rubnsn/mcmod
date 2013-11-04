package ruby.bamboo.render.inventory;

import ruby.bamboo.block.IDelude;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderInvDelude extends RenderInventory implements
        IRenderInventory {

    @Override
    public void render(RenderBlocks renderBlocks, Block block, int metadata) {
        switch (((IDelude) block).getOriginalRenderType()) {
        case 0:
            super.standardItemRender(block, renderBlocks, metadata);
            break;

        case 10:
            super.renderItemStair(renderBlocks, block);
            break;
        }
    }

}
