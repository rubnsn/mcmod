package ruby.bamboo.render.block;

import ruby.bamboo.render.tileentity.RenderCampfire;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderInvCampfire implements IRenderInventory {

    @Override
    public void renderInventory(RenderBlocks renderBlocks, Block block, int metadata) {
        RenderCampfire.instance.renderInv();
    }

}
