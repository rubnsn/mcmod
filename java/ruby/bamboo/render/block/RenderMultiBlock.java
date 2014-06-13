package ruby.bamboo.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import ruby.bamboo.block.BlockMultiBlock;
import ruby.bamboo.tileentity.TileEntityMultiBlock;

public class RenderMultiBlock implements IRenderBlocks {
    private float[] offset;

    @Override
    public void renderBlock(RenderBlocks renderblocks, Block par1Block, int par2, int par3, int par4) {
        TileEntity tile = renderblocks.blockAccess.getTileEntity(par2, par3, par4);
        if (tile instanceof TileEntityMultiBlock) {
            TileEntityMultiBlock tileMulti = (TileEntityMultiBlock) tile;
            byte[][][] visibleBit = tileMulti.getVisibleFlg();
            Block innerBlock;
            int meta;
            byte[][][] visibleFlgs = tileMulti.getVisibleFlg();
            offset = tileMulti.getRenderOffset();
            for (byte innerX = 0; innerX < tileMulti.getFieldSize(); innerX++) {
                for (byte innerY = 0; innerY < tileMulti.getFieldSize(); innerY++) {
                    for (byte innerZ = 0; innerZ < tileMulti.getFieldSize(); innerZ++) {
                        innerBlock = tileMulti.getInnerBlock(innerX, innerY, innerZ);
                        meta = tileMulti.getInnerMeta(innerX, innerY, innerZ);
                        if (innerBlock != Blocks.air) {
                            par1Block.setBlockBoundsBasedOnState(renderblocks.blockAccess, par2, par3, par4);
                            renderMultiBlock(renderblocks, (BlockMultiBlock) par1Block, meta, visibleFlgs[innerX][innerY][innerZ], tileMulti.getFieldSize(), par2, par3, par4, innerX, innerY, innerZ);
                        }
                    }
                }
            }
        }
    }

    private void renderMultiBlock(RenderBlocks renderblocks, BlockMultiBlock par1Block, int meta, byte visibleFlg, byte fieldSize, int par2, int par3, int par4, byte innerX, byte innerY, byte innerZ) {
        Tessellator tessellator = Tessellator.instance;
        float size = (1 / (float) fieldSize);
        renderblocks.setRenderBounds(offset[innerX], offset[innerY], offset[innerZ], offset[innerX] + size, offset[innerY] + size, offset[innerZ] + size);
        par1Block.setRenderSide(visibleFlg);
        par1Block.setInnerPos(innerX, innerY, innerZ);
        renderblocks.renderStandardBlock(par1Block, par2, par3, par4);
        par1Block.isTopRender = true;
        renderTop(renderblocks, par1Block, par2, par3, par4, innerX, innerZ);
        par1Block.isTopRender = false;
    }

    private void renderTop(RenderBlocks render, Block block, int x, int y, int z, byte innerX, byte innerZ) {
        render.enableAO = true;
        int color = block.colorMultiplier(render.blockAccess, x, y, z);
        float redLight = (float) (color >> 16 & 255) / 255.0F;
        float greenLight = (float) (color >> 8 & 255) / 255.0F;
        float blueLight = (float) (color & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable) {
            float f3 = (redLight * 30.0F + greenLight * 59.0F + blueLight * 11.0F) / 100.0F;
            float f4 = (redLight * 30.0F + greenLight * 70.0F) / 100.0F;
            float f5 = (redLight * 30.0F + blueLight * 70.0F) / 100.0F;
            redLight = f3;
            greenLight = f4;
            blueLight = f5;
        }

        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        int j1;
        int k1;
        int l1;
        int i2;
        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i1;
        int l = block.getMixedBrightnessForBlock(render.blockAccess, x, y, z);
        if (render.renderAllFaces || block.shouldSideBeRendered(render.blockAccess, x, y + 1, z, 1)) {
            if (render.renderMaxY >= 1.0D) {
                ++y;
            }

            render.aoBrightnessXYNP = block.getMixedBrightnessForBlock(render.blockAccess, x - 1, y, z);
            render.aoBrightnessXYPP = block.getMixedBrightnessForBlock(render.blockAccess, x + 1, y, z);
            render.aoBrightnessYZPN = block.getMixedBrightnessForBlock(render.blockAccess, x, y, z - 1);
            render.aoBrightnessYZPP = block.getMixedBrightnessForBlock(render.blockAccess, x, y, z + 1);
            render.aoLightValueScratchXYNP = render.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            render.aoLightValueScratchXYPP = render.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            render.aoLightValueScratchYZPN = render.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            render.aoLightValueScratchYZPP = render.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            flag2 = render.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
            flag3 = render.blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
            flag4 = render.blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
            flag5 = render.blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();

            if (!flag5 && !flag3) {
                render.aoLightValueScratchXYZNPN = render.aoLightValueScratchXYNP;
                render.aoBrightnessXYZNPN = render.aoBrightnessXYNP;
            } else {
                render.aoLightValueScratchXYZNPN = render.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
                render.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(render.blockAccess, x - 1, y, z - 1);
            }

            if (!flag5 && !flag2) {
                render.aoLightValueScratchXYZPPN = render.aoLightValueScratchXYPP;
                render.aoBrightnessXYZPPN = render.aoBrightnessXYPP;
            } else {
                render.aoLightValueScratchXYZPPN = render.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
                render.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(render.blockAccess, x + 1, y, z - 1);
            }

            if (!flag4 && !flag3) {
                render.aoLightValueScratchXYZNPP = render.aoLightValueScratchXYNP;
                render.aoBrightnessXYZNPP = render.aoBrightnessXYNP;
            } else {
                render.aoLightValueScratchXYZNPP = render.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
                render.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(render.blockAccess, x - 1, y, z + 1);
            }

            if (!flag4 && !flag2) {
                render.aoLightValueScratchXYZPPP = render.aoLightValueScratchXYPP;
                render.aoBrightnessXYZPPP = render.aoBrightnessXYPP;
            } else {
                render.aoLightValueScratchXYZPPP = render.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
                render.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(render.blockAccess, x + 1, y, z + 1);
            }

            if (render.renderMaxY >= 1.0D) {
                --y;
            }

            i1 = l;

            if (render.renderMaxY >= 1.0D || !render.blockAccess.getBlock(x, y + 1, z).isOpaqueCube()) {
                i1 = block.getMixedBrightnessForBlock(render.blockAccess, x, y + 1, z);
            }

            f7 = render.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            f11 = (render.aoLightValueScratchXYNP + render.aoLightValueScratchXYZNPN + f7 + render.aoLightValueScratchYZPN) / 4.0F;
            f10 = (f7 + render.aoLightValueScratchYZPN + render.aoLightValueScratchXYPP + render.aoLightValueScratchXYZPPN) / 4.0F;
            f9 = (render.aoLightValueScratchYZPP + f7 + render.aoLightValueScratchXYZPPP + render.aoLightValueScratchXYPP) / 4.0F;
            f8 = (render.aoLightValueScratchXYZNPP + render.aoLightValueScratchXYNP + render.aoLightValueScratchYZPP + f7) / 4.0F;

            f6 = (float) ((double) f8 * render.renderMaxZ * (1.0D - render.renderMinX) + (double) f9 * render.renderMaxZ * render.renderMinX + (double) f10 * (1.0D - render.renderMaxZ) * render.renderMinX + (double) f11 * (1.0D - render.renderMaxZ) * (1.0D - render.renderMinX));
            f3 = (float) ((double) f8 * render.renderMaxZ * (1.0D - render.renderMaxX) + (double) f9 * render.renderMaxZ * render.renderMaxX + (double) f10 * (1.0D - render.renderMaxZ) * render.renderMaxX + (double) f11 * (1.0D - render.renderMaxZ) * (1.0D - render.renderMaxX));
            f4 = (float) ((double) f8 * render.renderMinZ * (1.0D - render.renderMaxX) + (double) f9 * render.renderMinZ * render.renderMaxX + (double) f10 * (1.0D - render.renderMinZ) * render.renderMaxX + (double) f11 * (1.0D - render.renderMinZ) * (1.0D - render.renderMaxX));
            f5 = (float) ((double) f8 * render.renderMinZ * (1.0D - render.renderMinX) + (double) f9 * render.renderMinZ * render.renderMinX + (double) f10 * (1.0D - render.renderMinZ) * render.renderMinX + (double) f11 * (1.0D - render.renderMinZ) * (1.0D - render.renderMinX));

            j1 = render.getAoBrightness(render.aoBrightnessXYNP, render.aoBrightnessXYZNPN, render.aoBrightnessYZPN, i1);
            k1 = render.getAoBrightness(render.aoBrightnessYZPN, render.aoBrightnessXYPP, render.aoBrightnessXYZPPN, i1);
            l1 = render.getAoBrightness(render.aoBrightnessYZPP, render.aoBrightnessXYZPPP, render.aoBrightnessXYPP, i1);
            i2 = render.getAoBrightness(render.aoBrightnessXYZNPP, render.aoBrightnessXYNP, render.aoBrightnessYZPP, i1);

            render.brightnessTopLeft = render.mixAoBrightness(j1, k1, l1, i2, render.renderMaxZ * (1.0D - render.renderMinX), render.renderMaxZ * render.renderMinX, (1.0D - render.renderMaxZ) * render.renderMinX, (1.0D - render.renderMaxZ) * (1.0D - render.renderMinX));
            render.brightnessBottomLeft = render.mixAoBrightness(j1, k1, l1, i2, render.renderMaxZ * (1.0D - render.renderMaxX), render.renderMaxZ * render.renderMaxX, (1.0D - render.renderMaxZ) * render.renderMaxX, (1.0D - render.renderMaxZ) * (1.0D - render.renderMaxX));
            render.brightnessBottomRight = render.mixAoBrightness(j1, k1, l1, i2, render.renderMinZ * (1.0D - render.renderMaxX), render.renderMinZ * render.renderMaxX, (1.0D - render.renderMinZ) * render.renderMaxX, (1.0D - render.renderMinZ) * (1.0D - render.renderMaxX));
            render.brightnessTopRight = render.mixAoBrightness(j1, k1, l1, i2, render.renderMinZ * (1.0D - render.renderMinX), render.renderMinZ * render.renderMinX, (1.0D - render.renderMinZ) * render.renderMinX, (1.0D - render.renderMinZ) * (1.0D - render.renderMinX));

            render.colorRedTopLeft = render.colorRedBottomLeft = render.colorRedBottomRight = render.colorRedTopRight = redLight;
            render.colorGreenTopLeft = render.colorGreenBottomLeft = render.colorGreenBottomRight = render.colorGreenTopRight = greenLight;
            render.colorBlueTopLeft = render.colorBlueBottomLeft = render.colorBlueBottomRight = render.colorBlueTopRight = blueLight;
            render.colorRedTopLeft *= f3;
            render.colorGreenTopLeft *= f3;
            render.colorBlueTopLeft *= f3;
            render.colorRedBottomLeft *= f4;
            render.colorGreenBottomLeft *= f4;
            render.colorBlueBottomLeft *= f4;
            render.colorRedBottomRight *= f5;
            render.colorGreenBottomRight *= f5;
            render.colorBlueBottomRight *= f5;
            render.colorRedTopRight *= f6;
            render.colorGreenTopRight *= f6;
            render.colorBlueTopRight *= f6;
            render.renderFaceYPos(block, (double) x, (double) y, (double) z, render.getBlockIcon(block, render.blockAccess, x, y, z, 1));
        }
    }
}
