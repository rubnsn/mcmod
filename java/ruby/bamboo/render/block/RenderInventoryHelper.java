package ruby.bamboo.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

public class RenderInventoryHelper {
    public static void standardItemRender(Block par1Block, RenderBlocks renderer, int par2) {
        Tessellator tessellator = Tessellator.instance;
        par1Block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(par1Block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 0, par2));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 1, par2));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 2, par2));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 3, par2));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 4, par2));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 5, par2));
        tessellator.draw();
        GL11.glTranslatef(1F, 1F, 1F);
    }

    public static void renderItemStair(RenderBlocks renderer, Block par1Block) {
        Tessellator tessellator = Tessellator.instance;
        renderer.setRenderBoundsFromBlock(par1Block);

        for (int k = 0; k < 2; ++k) {
            if (k == 0) {
                renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
            }

            if (k == 1) {
                renderer.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
            }

            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(par1Block, 0));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(par1Block, 1));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(par1Block, 2));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(par1Block, 3));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(par1Block, 4));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(par1Block, 5));
            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
    }

    public static void renderItemBoard() {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1F, 0.0F);
        tessellator.draw();
    }
}
