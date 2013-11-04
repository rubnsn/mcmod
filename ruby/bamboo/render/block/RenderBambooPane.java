package ruby.bamboo.render.block;

import ruby.bamboo.block.BlockBambooPane;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;

public class RenderBambooPane implements IRenderBlocks {

    @Override
    public void render(RenderBlocks renderblocks, Block par1Block, int par2, int par3, int par4) {
        // バニラのPanelはメタデータによるIcon変更をサポートしないため追加
        // フチを描画しないオプションも追加
        renderBambooPane(renderblocks, (BlockBambooPane) par1Block, par2, par3, par4);
    }

    private void renderBambooPane(RenderBlocks renderblocks, BlockBambooPane bambooPane, int par2, int par3, int par4) {
        int l = renderblocks.blockAccess.getHeight();
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(bambooPane.getMixedBrightnessForBlock(renderblocks.blockAccess, par2, par3, par4));
        float f = 1.0F;
        int i1 = bambooPane.colorMultiplier(renderblocks.blockAccess, par2, par3, par4);
        float f1 = (i1 >> 16 & 255) / 255.0F;
        float f2 = (i1 >> 8 & 255) / 255.0F;
        float f3 = (i1 & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable) {
            float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        Icon icon;
        Icon icon1;
        //メタデータによるIcon変更
        int j1;
        j1 = renderblocks.blockAccess.getBlockMetadata(par2, par3, par4);
        icon = renderblocks.getBlockIconFromSideAndMetadata(bambooPane, 0, j1);
        icon1 = bambooPane.getIcon(0, j1);
        //フチ描画
        boolean isSideRender = bambooPane.isSideRender(renderblocks.blockAccess, par2, par3, par4);

        j1 = icon.getIconWidth();
        int k1 = icon.getIconHeight();
        double d0 = icon.getMinU();
        double d1 = icon.getInterpolatedU(8.0D);
        double d2 = icon.getMaxU();
        double d3 = icon.getMinV();
        double d4 = icon.getMaxV();
        int l1 = icon.getIconWidth();
        int i2 = icon.getIconHeight();
        double d5 = icon1.getInterpolatedU(7.0D);
        double d6 = icon1.getInterpolatedU(9.0D);
        double d7 = icon1.getMinV();
        double d8 = icon1.getInterpolatedV(8.0D);
        double d9 = icon1.getMaxV();
        double d10 = par2;
        double d11 = par2 + 0.5D;
        double d12 = par2 + 1;
        double d13 = par4;
        double d14 = par4 + 0.5D;
        double d15 = par4 + 1;
        double d16 = par2 + 0.5D - 0.0625D;
        double d17 = par2 + 0.5D + 0.0625D;
        double d18 = par4 + 0.5D - 0.0625D;
        double d19 = par4 + 0.5D + 0.0625D;
        boolean flag = bambooPane.canThisPaneConnectToThisBlockID(renderblocks.blockAccess.getBlockId(par2, par3, par4 - 1));
        boolean flag1 = bambooPane.canThisPaneConnectToThisBlockID(renderblocks.blockAccess.getBlockId(par2, par3, par4 + 1));
        boolean flag2 = bambooPane.canThisPaneConnectToThisBlockID(renderblocks.blockAccess.getBlockId(par2 - 1, par3, par4));
        boolean flag3 = bambooPane.canThisPaneConnectToThisBlockID(renderblocks.blockAccess.getBlockId(par2 + 1, par3, par4));
        boolean flag4 = bambooPane.shouldSideBeRendered(renderblocks.blockAccess, par2, par3 + 1, par4, 1);
        boolean flag5 = bambooPane.shouldSideBeRendered(renderblocks.blockAccess, par2, par3 - 1, par4, 0);

        if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1)) {
            if (flag2 && !flag3) {
                tessellator.addVertexWithUV(d10, par3 + 1, d14, d0, d3);
                tessellator.addVertexWithUV(d10, par3 + 0, d14, d0, d4);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d1, d4);
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d1, d3);
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d0, d3);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d0, d4);
                tessellator.addVertexWithUV(d10, par3 + 0, d14, d1, d4);
                tessellator.addVertexWithUV(d10, par3 + 1, d14, d1, d3);

                if (isSideRender) {
                    if (!flag1 && !flag) {
                        tessellator.addVertexWithUV(d11, par3 + 1, d19, d5, d7);
                        tessellator.addVertexWithUV(d11, par3 + 0, d19, d5, d9);
                        tessellator.addVertexWithUV(d11, par3 + 0, d18, d6, d9);
                        tessellator.addVertexWithUV(d11, par3 + 1, d18, d6, d7);
                        tessellator.addVertexWithUV(d11, par3 + 1, d18, d5, d7);
                        tessellator.addVertexWithUV(d11, par3 + 0, d18, d5, d9);
                        tessellator.addVertexWithUV(d11, par3 + 0, d19, d6, d9);
                        tessellator.addVertexWithUV(d11, par3 + 1, d19, d6, d7);
                    }

                    if (flag4 || par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2 - 1, par3 + 1, par4)) {
                        tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d18, d5, d8);
                    }

                    if (flag5 || par3 > 1 && renderblocks.blockAccess.isAirBlock(par2 - 1, par3 - 1, par4)) {
                        tessellator.addVertexWithUV(d10, par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d10, par3 - 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d10, par3 - 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d10, par3 - 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d8);
                    }
                }
            } else if (!flag2 && flag3) {
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d1, d3);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d1, d4);
                tessellator.addVertexWithUV(d12, par3 + 0, d14, d2, d4);
                tessellator.addVertexWithUV(d12, par3 + 1, d14, d2, d3);
                tessellator.addVertexWithUV(d12, par3 + 1, d14, d1, d3);
                tessellator.addVertexWithUV(d12, par3 + 0, d14, d1, d4);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d2, d4);
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d2, d3);

                if (isSideRender) {
                    if (!flag1 && !flag) {
                        tessellator.addVertexWithUV(d11, par3 + 1, d18, d5, d7);
                        tessellator.addVertexWithUV(d11, par3 + 0, d18, d5, d9);
                        tessellator.addVertexWithUV(d11, par3 + 0, d19, d6, d9);
                        tessellator.addVertexWithUV(d11, par3 + 1, d19, d6, d7);
                        tessellator.addVertexWithUV(d11, par3 + 1, d19, d5, d7);
                        tessellator.addVertexWithUV(d11, par3 + 0, d19, d5, d9);
                        tessellator.addVertexWithUV(d11, par3 + 0, d18, d6, d9);
                        tessellator.addVertexWithUV(d11, par3 + 1, d18, d6, d7);
                    }

                    if (flag4 || par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2 + 1, par3 + 1, par4)) {
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d18, d5, d7);
                        tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d18, d5, d7);
                    }

                    if (flag5 || par3 > 1 && renderblocks.blockAccess.isAirBlock(par2 + 1, par3 - 1, par4)) {
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d12, par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d12, par3 - 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d7);
                        tessellator.addVertexWithUV(d12, par3 - 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d12, par3 - 0.01D, d18, d5, d7);
                    }
                }
            }
        } else {
            tessellator.addVertexWithUV(d10, par3 + 1, d14, d0, d3);
            tessellator.addVertexWithUV(d10, par3 + 0, d14, d0, d4);
            tessellator.addVertexWithUV(d12, par3 + 0, d14, d2, d4);
            tessellator.addVertexWithUV(d12, par3 + 1, d14, d2, d3);
            tessellator.addVertexWithUV(d12, par3 + 1, d14, d0, d3);
            tessellator.addVertexWithUV(d12, par3 + 0, d14, d0, d4);
            tessellator.addVertexWithUV(d10, par3 + 0, d14, d2, d4);
            tessellator.addVertexWithUV(d10, par3 + 1, d14, d2, d3);

            if (isSideRender) {
                if (flag4) {
                    tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d18, d5, d9);
                } else {
                    if (par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2 - 1, par3 + 1, par4)) {
                        tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d18, d5, d8);
                    }

                    if (par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2 + 1, par3 + 1, par4)) {
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d18, d5, d7);
                        tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d18, d5, d7);
                    }
                }

                if (flag5) {
                    tessellator.addVertexWithUV(d10, par3 - 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d12, par3 - 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, par3 - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d10, par3 - 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d12, par3 - 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d10, par3 - 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d10, par3 - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d12, par3 - 0.01D, d18, d5, d9);
                } else {
                    if (par3 > 1 && renderblocks.blockAccess.isAirBlock(par2 - 1, par3 - 1, par4)) {
                        tessellator.addVertexWithUV(d10, par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d10, par3 - 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d10, par3 - 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d10, par3 - 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d8);
                    }

                    if (par3 > 1 && renderblocks.blockAccess.isAirBlock(par2 + 1, par3 - 1, par4)) {
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d12, par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d12, par3 - 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d7);
                        tessellator.addVertexWithUV(d12, par3 - 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d12, par3 - 0.01D, d18, d5, d7);
                    }
                }
            }
        }

        if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1)) {
            if (flag && !flag1) {
                tessellator.addVertexWithUV(d11, par3 + 1, d13, d0, d3);
                tessellator.addVertexWithUV(d11, par3 + 0, d13, d0, d4);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d1, d4);
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d1, d3);
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d0, d3);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d0, d4);
                tessellator.addVertexWithUV(d11, par3 + 0, d13, d1, d4);
                tessellator.addVertexWithUV(d11, par3 + 1, d13, d1, d3);

                if (isSideRender) {
                    if (!flag3 && !flag2) {
                        tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d7);
                        tessellator.addVertexWithUV(d16, par3 + 0, d14, d5, d9);
                        tessellator.addVertexWithUV(d17, par3 + 0, d14, d6, d9);
                        tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d7);
                        tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d7);
                        tessellator.addVertexWithUV(d17, par3 + 0, d14, d5, d9);
                        tessellator.addVertexWithUV(d16, par3 + 0, d14, d6, d9);
                        tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d7);
                    }

                    if (flag4 || par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2, par3 + 1, par4 - 1)) {
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d13, d6, d7);
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d13, d5, d7);
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d14, d6, d7);
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d13, d6, d8);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d13, d5, d8);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d14, d5, d7);
                    }

                    if (flag5 || par3 > 1 && renderblocks.blockAccess.isAirBlock(par2, par3 - 1, par4 - 1)) {
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d13, d6, d7);
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d13, d5, d7);
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d14, d6, d7);
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d13, d6, d8);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d13, d5, d8);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d14, d5, d7);
                    }
                }
            } else if (!flag && flag1) {
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d1, d3);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d1, d4);
                tessellator.addVertexWithUV(d11, par3 + 0, d15, d2, d4);
                tessellator.addVertexWithUV(d11, par3 + 1, d15, d2, d3);
                tessellator.addVertexWithUV(d11, par3 + 1, d15, d1, d3);
                tessellator.addVertexWithUV(d11, par3 + 0, d15, d1, d4);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d2, d4);
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d2, d3);

                if (isSideRender) {
                    if (!flag3 && !flag2) {
                        tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d7);
                        tessellator.addVertexWithUV(d17, par3 + 0, d14, d5, d9);
                        tessellator.addVertexWithUV(d16, par3 + 0, d14, d6, d9);
                        tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d7);
                        tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d7);
                        tessellator.addVertexWithUV(d16, par3 + 0, d14, d5, d9);
                        tessellator.addVertexWithUV(d17, par3 + 0, d14, d6, d9);
                        tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d7);
                    }

                    if (flag4 || par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2, par3 + 1, par4 + 1)) {
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d15, d5, d9);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d15, d6, d9);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d15, d5, d8);
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d14, d5, d9);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d14, d6, d9);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d15, d6, d8);
                    }

                    if (flag5 || par3 > 1 && renderblocks.blockAccess.isAirBlock(par2, par3 - 1, par4 + 1)) {
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d15, d5, d9);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d15, d6, d9);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d15, d5, d8);
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d14, d5, d9);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d14, d6, d9);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d15, d6, d8);
                    }
                }
            }
        } else {
            tessellator.addVertexWithUV(d11, par3 + 1, d15, d0, d3);
            tessellator.addVertexWithUV(d11, par3 + 0, d15, d0, d4);
            tessellator.addVertexWithUV(d11, par3 + 0, d13, d2, d4);
            tessellator.addVertexWithUV(d11, par3 + 1, d13, d2, d3);
            tessellator.addVertexWithUV(d11, par3 + 1, d13, d0, d3);
            tessellator.addVertexWithUV(d11, par3 + 0, d13, d0, d4);
            tessellator.addVertexWithUV(d11, par3 + 0, d15, d2, d4);
            tessellator.addVertexWithUV(d11, par3 + 1, d15, d2, d3);

            if (isSideRender) {
                if (flag4) {
                    tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d13, d6, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d15, d6, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d15, d5, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d13, d5, d9);
                } else {
                    if (par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2, par3 + 1, par4 - 1)) {
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d13, d6, d7);
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d13, d5, d7);
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d14, d6, d7);
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d13, d6, d8);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d13, d5, d8);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d14, d5, d7);
                    }

                    if (par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2, par3 + 1, par4 + 1)) {
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d15, d5, d9);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d15, d6, d9);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d15, d5, d8);
                        tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d14, d5, d9);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d14, d6, d9);
                        tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d15, d6, d8);
                    }
                }

                if (flag5) {
                    tessellator.addVertexWithUV(d17, par3 - 0.005D, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, par3 - 0.005D, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, par3 - 0.005D, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, par3 - 0.005D, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, par3 - 0.005D, d13, d6, d9);
                    tessellator.addVertexWithUV(d17, par3 - 0.005D, d15, d6, d7);
                    tessellator.addVertexWithUV(d16, par3 - 0.005D, d15, d5, d7);
                    tessellator.addVertexWithUV(d16, par3 - 0.005D, d13, d5, d9);
                } else {
                    if (par3 > 1 && renderblocks.blockAccess.isAirBlock(par2, par3 - 1, par4 - 1)) {
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d13, d6, d7);
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d13, d5, d7);
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d14, d6, d7);
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d13, d6, d8);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d13, d5, d8);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d14, d5, d7);
                    }

                    if (par3 > 1 && renderblocks.blockAccess.isAirBlock(par2, par3 - 1, par4 + 1)) {
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d15, d5, d9);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d15, d6, d9);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d15, d5, d8);
                        tessellator.addVertexWithUV(d16, par3 - 0.005D, d14, d5, d9);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d14, d6, d9);
                        tessellator.addVertexWithUV(d17, par3 - 0.005D, d15, d6, d8);
                    }
                }
            }
        }
    }

}
