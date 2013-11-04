package ruby.bamboo.render.inventory;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

public class RenderInvBambooBlock implements IRenderInventory {

    @Override
    public void render(RenderBlocks renderBlocks, Block block, int metadata) {
        if (metadata == 0) {
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1F, 0.0F);
            renderBlocks.drawCrossedSquares(block, metadata, -0.5D, -0.5D, -0.5D, 1F);
            tessellator.draw();
        }
    }

}
