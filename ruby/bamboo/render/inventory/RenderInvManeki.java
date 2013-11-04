package ruby.bamboo.render.inventory;

import ruby.bamboo.render.RenderManeki;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderInvManeki implements IRenderInventory {

    @Override
    public void render(RenderBlocks renderBlocks, Block block, int metadata) {
        RenderManeki.instance.renderInv();
    }

}
