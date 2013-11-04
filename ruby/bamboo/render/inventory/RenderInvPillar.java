package ruby.bamboo.render.inventory;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderInvPillar extends RenderInventory implements
        IRenderInventory {

    @Override
    public void render(RenderBlocks renderBlocks, Block block, int metadata) {
        super.standardItemRender(block, renderBlocks, metadata);
    }

}
