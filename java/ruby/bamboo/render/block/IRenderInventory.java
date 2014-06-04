package ruby.bamboo.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public interface IRenderInventory {
    public void renderInventory(RenderBlocks renderblocks, Block block, int metadata);
}
