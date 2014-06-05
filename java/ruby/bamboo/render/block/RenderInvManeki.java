package ruby.bamboo.render.block;

import ruby.bamboo.render.tileentity.RenderManeki;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderInvManeki implements IRenderInventory {

    @Override
    public void renderInventory(RenderBlocks renderBlocks, Block block, int metadata) {
        RenderManeki.instance.renderInv();
    }

}
