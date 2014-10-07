package ruby.bamboo.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import ruby.bamboo.render.tileentity.RenderVillagerBlock;

public class RenderInvVillagerBlock implements IRenderInventory {

    @Override
    public void renderInventory(RenderBlocks renderblocks, Block block, int metadata) {
        RenderVillagerBlock.instance.renderInv();
    }

}
