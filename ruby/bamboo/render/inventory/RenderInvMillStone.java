package ruby.bamboo.render.inventory;

import ruby.bamboo.render.RenderMillStone;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderInvMillStone implements IRenderInventory {

    @Override
    public void render(RenderBlocks renderBlocks, Block block, int metadata) {
        RenderMillStone.instance.renderInv();
    }

}
