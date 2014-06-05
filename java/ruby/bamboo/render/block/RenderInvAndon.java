package ruby.bamboo.render.block;

import ruby.bamboo.render.tileentity.RenderAndon;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderInvAndon implements IRenderInventory {

    @Override
    public void renderInventory(RenderBlocks renderBlocks, Block block, int metadata) {
        RenderAndon.instance.renderInv();
    }

}
