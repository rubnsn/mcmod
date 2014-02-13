package ruby.bamboo.render.inventory;

import ruby.bamboo.render.RenderAndon;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderInvAndon implements IRenderInventory {

    @Override
    public void render(RenderBlocks renderBlocks, Block block, int metadata) {
        RenderAndon.instance.renderInv();
    }

}
