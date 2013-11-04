package ruby.bamboo.render.block;

import ruby.bamboo.block.BlockRiceField;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

public class RenderRiceField implements IRenderBlocks {

    @Override
    public void render(RenderBlocks renderblocks, Block par1Block, int par2, int par3, int par4) {
        if (BlockRiceField.renderPass == 0) {
            this.renderField(renderblocks, par1Block, par2, par3, par4);
        } else {
            this.renderBlockFluids(renderblocks, par1Block, par2, par3, par4);
        }
    }

    private void renderField(RenderBlocks renderer, Block par1Block, int par2, int par3, int par4) {
        Tessellator tessellator = Tessellator.instance;
        boolean flag1 = par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3 - 1, par4, 0);
        boolean[] aboolean = new boolean[] { par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 - 1, 2), par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 + 1, 3), par1Block.shouldSideBeRendered(renderer.blockAccess, par2 - 1, par3, par4, 4), par1Block.shouldSideBeRendered(renderer.blockAccess, par2 + 1, par3, par4, 5) };
        Icon top = renderer.getBlockIconFromSide(par1Block, 1);
        Icon other = renderer.getBlockIconFromSide(par1Block, 0);
        tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4));
        renderer.renderFaceYPos(par1Block, par2, par3, par4, top);

        if (flag1) {
            renderer.renderFaceYNeg(par1Block, par2, par3, par4, other);
        }

        for (int i = 0; i < 4; i++) {
            if (aboolean[i]) {
                switch (i) {
                case 0:
                    renderer.renderFaceZNeg(par1Block, par2, par3, par4, other);
                    break;

                case 1:
                    renderer.renderFaceZPos(par1Block, par2, par3, par4, other);
                    break;

                case 2:
                    renderer.renderFaceXNeg(par1Block, par2, par3, par4, other);
                    break;

                case 3:
                    renderer.renderFaceXPos(par1Block, par2, par3, par4, other);
                    break;
                }
            }
        }
    }

    // 水レンダー
    private boolean renderBlockFluids(RenderBlocks renderer, Block par1Block, int par2, int par3, int par4) {
        Tessellator tessellator = Tessellator.instance;
        int l = par1Block.colorMultiplier(renderer.blockAccess, par2, par3, par4);
        float f = (l >> 16 & 255) / 255.0F;
        float f1 = (l >> 8 & 255) / 255.0F;
        float f2 = (l & 255) / 255.0F;
        boolean flag = par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3 + 1, par4, 1);
        boolean flag1 = par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3 - 1, par4, 0);
        boolean[] aboolean = new boolean[] { par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 - 1, 2), par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 + 1, 3), par1Block.shouldSideBeRendered(renderer.blockAccess, par2 - 1, par3, par4, 4), par1Block.shouldSideBeRendered(renderer.blockAccess, par2 + 1, par3, par4, 5) };

        if (!flag && !flag1 && !aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3]) {
            return false;
        } else {
            boolean flag2 = false;
            float f3 = 0.5F;
            float f4 = 1.0F;
            float f5 = 0.8F;
            float f6 = 0.6F;
            double d0 = 0.0D;
            double d1 = 1.0D;
            Material material = par1Block.blockMaterial;
            int i1 = renderer.blockAccess.getBlockMetadata(par2, par3, par4);
            double d2 = this.getFluidHeight(renderer, par2, par3, par4, material);
            double d3 = this.getFluidHeight(renderer, par2, par3, par4 + 1, material);
            double d4 = this.getFluidHeight(renderer, par2 + 1, par3, par4 + 1, material);
            double d5 = this.getFluidHeight(renderer, par2 + 1, par3, par4, material);
            double d6 = 0.0010000000474974513D;
            float f7;
            float f8;
            float f9;

            if (d2 == 0) {
                return false;
            }

            if (renderer.renderAllFaces || flag) {
                flag2 = true;
                // 水のIconを使用
                Icon icon = renderer.getBlockIconFromSideAndMetadata(Block.waterMoving, 1, i1);
                float f10 = (float) BlockFluid.getFlowDirection(renderer.blockAccess, par2, par3, par4, Material.water);

                if (f10 > -999.0F) {
                    icon = renderer.getBlockIconFromSideAndMetadata(Block.waterMoving, 2, i1);
                }

                d2 -= d6;
                d3 -= d6;
                d4 -= d6;
                d5 -= d6;
                double d7;
                double d8;
                double d9;
                double d10;
                double d11;
                double d12;
                double d13;
                double d14;

                if (f10 < -999.0F) {
                    d8 = icon.getInterpolatedU(0.0D);
                    d12 = icon.getInterpolatedV(0.0D);
                    d7 = d8;
                    d11 = icon.getInterpolatedV(16.0D);
                    d10 = icon.getInterpolatedU(16.0D);
                    d14 = d11;
                    d9 = d10;
                    d13 = d12;
                } else {
                    f9 = MathHelper.sin(f10) * 0.25F;
                    f8 = MathHelper.cos(f10) * 0.25F;
                    f7 = 8.0F;
                    d8 = icon.getInterpolatedU(8.0F + (-f8 - f9) * 16.0F);
                    d12 = icon.getInterpolatedV(8.0F + (-f8 + f9) * 16.0F);
                    d7 = icon.getInterpolatedU(8.0F + (-f8 + f9) * 16.0F);
                    d11 = icon.getInterpolatedV(8.0F + (f8 + f9) * 16.0F);
                    d10 = icon.getInterpolatedU(8.0F + (f8 + f9) * 16.0F);
                    d14 = icon.getInterpolatedV(8.0F + (f8 - f9) * 16.0F);
                    d9 = icon.getInterpolatedU(8.0F + (f8 - f9) * 16.0F);
                    d13 = icon.getInterpolatedV(8.0F + (-f8 - f9) * 16.0F);
                }

                tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4));
                f9 = 1.0F;
                tessellator.setColorOpaque_F(f4 * f9 * f, f4 * f9 * f1, f4 * f9 * f2);
                tessellator.addVertexWithUV(par2 + 0, par3 + d2 + 0.5F, par4 + 0, d8, d12);
                tessellator.addVertexWithUV(par2 + 0, par3 + d3 + 0.5F, par4 + 1, d7, d11);
                tessellator.addVertexWithUV(par2 + 1, par3 + d4 + 0.5F, par4 + 1, d10, d14);
                tessellator.addVertexWithUV(par2 + 1, par3 + d5 + 0.5F, par4 + 0, d9, d13);
            }

            if (renderer.renderAllFaces || flag1) {
                tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4));
                float f11 = 1.0F;
                tessellator.setColorOpaque_F(f3 * f11, f3 * f11, f3 * f11);
                // renderer.renderFaceYNeg(Block.waterMoving, (double)par2,
                // (double)par3 + d6, (double)par4,
                // renderer.getBlockIconFromSide(Block.waterMoving, 0));
                flag2 = true;
            }

            for (int j1 = 0; j1 < 4; ++j1) {
                int k1 = par2;
                int l1 = par4;

                if (j1 == 0) {
                    l1 = par4 - 1;
                }

                if (j1 == 1) {
                    ++l1;
                }

                if (j1 == 2) {
                    k1 = par2 - 1;
                }

                if (j1 == 3) {
                    ++k1;
                }

                // 水のIconを使用するように
                Icon icon1 = renderer.getBlockIconFromSideAndMetadata(Block.waterMoving, j1 + 2, i1);

                if (renderer.renderAllFaces || aboolean[j1]) {
                    double d15;
                    double d16;
                    double d17;
                    double d18;
                    double d19;
                    double d20;

                    if (j1 == 0) {
                        d15 = d2;
                        d17 = d5;
                        d16 = par2;
                        d18 = par2 + 1;
                        d19 = par4 + d6;
                        d20 = par4 + d6;
                    } else if (j1 == 1) {
                        d15 = d4;
                        d17 = d3;
                        d16 = par2 + 1;
                        d18 = par2;
                        d19 = par4 + 1 - d6;
                        d20 = par4 + 1 - d6;
                    } else if (j1 == 2) {
                        d15 = d3;
                        d17 = d2;
                        d16 = par2 + d6;
                        d18 = par2 + d6;
                        d19 = par4 + 1;
                        d20 = par4;
                    } else {
                        d15 = d5;
                        d17 = d4;
                        d16 = par2 + 1 - d6;
                        d18 = par2 + 1 - d6;
                        d19 = par4;
                        d20 = par4 + 1;
                    }

                    flag2 = true;
                    float f12 = icon1.getInterpolatedU(0.0D);
                    f9 = icon1.getInterpolatedU(8.0D);
                    f8 = icon1.getInterpolatedV((1.0D - d15) * 16.0D * 0.5D);
                    f7 = icon1.getInterpolatedV((1.0D - d17) * 16.0D * 0.5D);
                    float f13 = icon1.getInterpolatedV(8.0D);
                    tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(renderer.blockAccess, k1, par3, l1));
                    float f14 = 1.0F;

                    if (j1 < 2) {
                        f14 *= f5;
                    } else {
                        f14 *= f6;
                    }

                    tessellator.setColorOpaque_F(f4 * f14 * f, f4 * f14 * f1, f4 * f14 * f2);
                    tessellator.addVertexWithUV(d16, par3 + d15 + 0.5F, d19, f12, f8);
                    tessellator.addVertexWithUV(d18, par3 + d17 + 0.5F, d20, f9, f7);
                    tessellator.addVertexWithUV(d18, (double) (par3 + 0) + 0.5F, d20, f9, f13);
                    tessellator.addVertexWithUV(d16, (double) (par3 + 0) + 0.5F, d19, f12, f13);
                }
            }

            renderer.renderMinY = d0;
            renderer.renderMaxY = d1;
            return flag2;
        }
    }

    // バニラと違うメタデータによる水の高さ表現のため
    private float getFluidHeight(RenderBlocks renderer, int par1, int par2, int par3, Material par4Material) {
        int l = 0;
        float f = 0.0F;

        for (int i1 = 0; i1 < 4; ++i1) {
            int j1 = par1 - (i1 & 1);
            int k1 = par3 - (i1 >> 1 & 1);

            if (renderer.blockAccess.getBlockMaterial(j1, par2 + 1, k1) == par4Material) {
                return 1.0F;
            }

            Material material1 = renderer.blockAccess.getBlockMaterial(j1, par2, k1);

            if (material1 == par4Material) {
                int l1 = renderer.blockAccess.getBlockMetadata(j1, par2, k1) & 3;
                l1 += 4;
                f += getFluidHeightPercent(l1);
                ++l;
            } else if (!material1.isSolid()) {
                ++f;
                ++l;
            }
        }

        return 1.0F - f / l;
    }

    private float getFluidHeightPercent(float par0) {
        return (par0 + 1) / (par0 != 7 ? 9.0F : 8.0F);
    }

}
