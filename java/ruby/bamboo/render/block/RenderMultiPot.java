package ruby.bamboo.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import ruby.bamboo.BambooInit;
import ruby.bamboo.CustomRenderHandler;
import ruby.bamboo.block.BlockMultiPot;
import ruby.bamboo.tileentity.TileEntityMultiPot;

public class RenderMultiPot implements IRenderBlocks {
    private float[] offset = new float[] { -0.333F, 0, 0.333F };
    private static final int[] PATTERN_X;
    private static final int[] PATTERN_Z;
    private static RenderMultiPot instance;
    static {
        int[] i1 = new int[TileEntityMultiPot.MAX_LENGTH];
        int[] i2 = new int[TileEntityMultiPot.MAX_LENGTH];
        for (int i = 0; i < TileEntityMultiPot.MAX_LENGTH; i++) {
            i1[i] = i % TileEntityMultiPot.SQ;
            i2[i] = (int) (i / (float) TileEntityMultiPot.SQ);
        }
        PATTERN_X = i1;
        PATTERN_Z = i2;
    }

    public RenderMultiPot() {
        instance = this;
    }

    @Override
    public void renderBlock(RenderBlocks renderblocks, Block par1Block, int par2, int par3, int par4) {
        TileEntity tileentity = renderblocks.blockAccess.getTileEntity(par2, par3, par4);

        if (tileentity != null && tileentity instanceof TileEntityMultiPot) {
            for (int i = 0; i < TileEntityMultiPot.MAX_LENGTH; i++) {
                if (((TileEntityMultiPot) tileentity).isEnable(i)) {
                    renderBlockMultiPot(renderblocks, (BlockMultiPot) par1Block, tileentity, par2, par3, par4, i);
                }
            }
        }
    }

    private void renderBlockMultiPot(RenderBlocks render, BlockMultiPot blockPot, TileEntity tileentity, int x, int y, int z, int num) {
        float offsetX = offset[PATTERN_X[num]];
        float offsetZ = offset[PATTERN_Z[num]];
        Tessellator tessellator = Tessellator.instance;
        tessellator.addTranslation(offsetX, 0, offsetZ);
        float f = 0.375F;
        float f1 = f / 2.25F;
        render.setRenderBounds(0.5F - f1, 0.0F, 0.5F - f1, 0.5F + f1, f, 0.5F + f1);
        render.renderStandardBlock(blockPot, x, y, z);
        tessellator.setBrightness(blockPot.getMixedBrightnessForBlock(render.blockAccess, x, y, z));
        int l = blockPot.colorMultiplier(render.blockAccess, x, y, z);
        IIcon iicon = render.getBlockIcon(Blocks.flower_pot);
        float red = (float) (l >> 16 & 255) / 255.0F;
        float green = (float) (l >> 8 & 255) / 255.0F;
        float blue = (float) (l & 255) / 255.0F;
        float f3;

        if (EntityRenderer.anaglyphEnable) {
            f3 = (red * 30.0F + green * 59.0F + blue * 11.0F) / 100.0F;
            float f4 = (red * 30.0F + green * 70.0F) / 100.0F;
            float f5 = (red * 30.0F + blue * 70.0F) / 100.0F;
            red = f3;
            green = f4;
            blue = f5;
        }

        tessellator.setColorOpaque_F(red, green, blue);
        f3 = 0.2F;
        render.renderFaceXPos(blockPot, (double) ((float) x - 0.5F + f3), (double) y, (double) z, iicon);
        render.renderFaceXNeg(blockPot, (double) ((float) x + 0.5F - f3), (double) y, (double) z, iicon);
        render.renderFaceZPos(blockPot, (double) x, (double) y, (double) ((float) z - 0.5F + f3), iicon);
        render.renderFaceZNeg(blockPot, (double) x, (double) y, (double) ((float) z + 0.5F - f3), iicon);
        render.renderFaceYPos(blockPot, (double) x, (double) ((float) y - 0.5F + f3 + 0.1875F), (double) z, render.getBlockIcon(Blocks.dirt));

        if (tileentity != null && tileentity instanceof TileEntityMultiPot) {
            Item item = ((TileEntityMultiPot) tileentity).getFlowerPotItem(num);
            int i1 = ((TileEntityMultiPot) tileentity).getFlowerPotData(num);

            if (item instanceof ItemBlock) {
                Block block = Block.getBlockFromItem(item);
                int j1 = block.getRenderType();
                float f6 = 0.0F;
                float f7 = 4.0F;
                float f8 = 0.0F;
                tessellator.addTranslation(f6 / 16.0F, f7 / 16.0F, f8 / 16.0F);
                l = block.colorMultiplier(render.blockAccess, x, y, z);

                if (l != 16777215) {
                    red = (float) (l >> 16 & 255) / 255.0F;
                    green = (float) (l >> 8 & 255) / 255.0F;
                    blue = (float) (l & 255) / 255.0F;
                    tessellator.setColorOpaque_F(red, green, blue);
                }

                if (j1 == 1 || j1 == 40 || j1 == CustomRenderHandler.coordinateCrossUID) {
                    render.drawCrossedSquares(render.getBlockIconFromSideAndMetadata(block, 0, i1), (double) x, (double) y, (double) z, 0.75F);
                } else if (j1 == 13) {
                    render.renderAllFaces = true;
                    float f9 = 0.125F;
                    render.setRenderBounds((double) (0.5F - f9), 0.0D, (double) (0.5F - f9), (double) (0.5F + f9), 0.25D, (double) (0.5F + f9));
                    render.renderStandardBlock(block, x, y, z);
                    render.setRenderBounds((double) (0.5F - f9), 0.25D, (double) (0.5F - f9), (double) (0.5F + f9), 0.5D, (double) (0.5F + f9));
                    render.renderStandardBlock(block, x, y, z);
                    render.setRenderBounds((double) (0.5F - f9), 0.5D, (double) (0.5F - f9), (double) (0.5F + f9), 0.75D, (double) (0.5F + f9));
                    render.renderStandardBlock(block, x, y, z);
                    render.renderAllFaces = false;
                    render.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
                }

                tessellator.addTranslation(-f6 / 16.0F, -f7 / 16.0F, -f8 / 16.0F);
            }
        }

        tessellator.addTranslation(-offsetX, 0, -offsetZ);
    }

    public static RenderMultiPot getInstance() {
        return instance;
    }

    public void renderInv(RenderBlocks render) {
        renderBlockMultiPot(render, (BlockMultiPot) BambooInit.multiPot, null, 0, 0, 0, 4);
    }
}
