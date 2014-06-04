package ruby.bamboo.render.block;

import ruby.bamboo.render.RenderMillStone;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderInvMillStone implements IRenderInventory {

    @Override
    public void renderInventory(RenderBlocks renderBlocks, Block block, int metadata) {
        RenderMillStone.instance.renderInv();
    }

}
