package ruby.bamboo.render.inventory;

import ruby.bamboo.render.RenderCampfire;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderInvCampfire implements IRenderInventory {

    @Override
    public void render(RenderBlocks renderBlocks, Block block, int metadata) {
        RenderCampfire.instance.renderInv();
    }

}
