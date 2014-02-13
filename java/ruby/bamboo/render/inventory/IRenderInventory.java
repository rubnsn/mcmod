package ruby.bamboo.render.inventory;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

public interface IRenderInventory {
    public void render(RenderBlocks renderBlocks, Block block, int metadata);
}
