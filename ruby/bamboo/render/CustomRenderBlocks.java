package ruby.bamboo.render;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.block.BlockBambooPane;
import ruby.bamboo.block.BlockPillar;
import ruby.bamboo.block.BlockRiceField;
import ruby.bamboo.block.IDelude;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public class CustomRenderBlocks
{
    public void renderDelude(RenderBlocks renderer, Block par1Block, int par2, int par3, int par4)
    {
        boolean isGrass = false;

        if (renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 1).getIconName().equals("grass_top"))
        {
            ((IDelude)par1Block).setIconGrass(true);
            isGrass = true;
            renderer.fancyGrass = true;
        }

        switch (((IDelude)par1Block).getOriginalRenderType())
        {
            case 0:
                renderer.renderStandardBlock(par1Block, par2, par3, par4);
                break;

            case 10:
                renderer.renderBlockStairs((BlockStairs)par1Block, par2, par3, par4);
                break;
        }

        if (isGrass)
        {
            ((IDelude)par1Block).setIconGrass(false);
            renderer.fancyGrass = false;
        }
    }
    public void renderBlockPillar(RenderBlocks renderer, BlockPillar par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);
        Icon icon = renderer.getBlockIcon(par1Block);
        int meta = renderer.blockAccess.getBlockMetadata(par2, par3, par4);
        boolean isNotUpAndDown = (meta != 0 && meta != 1);
        boolean isNotNothAndSouth = (meta != 2 && meta != 3);
        boolean isNotEastAndWest = (meta != 4 && meta != 5);
        boolean[] isSideRender = new boolean[] {par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3 - 1, par4, 0), par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3 + 1, par4, 1), par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 - 1, 2), par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 + 1, 3), par1Block.shouldSideBeRendered(renderer.blockAccess, par2 - 1, par3, par4, 4), par1Block.shouldSideBeRendered(renderer.blockAccess, par2 + 1, par3, par4, 5)};
        boolean[] isRender = new boolean[6];
        boolean isUpRender;
        boolean isDownRender;
        boolean isEastRender;
        boolean isWestRender;
        boolean isNorthRender;
        boolean isSouthRender;
        boolean isSmallScale;
        renderer.enableAO = true;

        for (ForgeDirection fd: ForgeDirection.VALID_DIRECTIONS)
        {
            if (fd == ForgeDirection.getOrientation(meta) ||
                    fd == ForgeDirection.getOrientation(meta).getOpposite())
            {
                continue;//同一方向のリンクはなし
            }

            Arrays.fill(isRender, false);
            isSmallScale = false;

            if (Block.blocksList[renderer.blockAccess.getBlockId(par2 + fd.offsetX, par3 + fd.offsetY, par4 + fd.offsetZ)] instanceof BlockPillar)
            {
                if (renderer.blockAccess.getBlockMetadata(par2, par3, par4) != renderer.blockAccess.getBlockMetadata(par2 + fd.offsetX, par3 + fd.offsetY, par4 + fd.offsetZ))
                {
                    continue;
                }

                if (par1Block.getSize() > ((BlockPillar) Block.blocksList[renderer.blockAccess.getBlockId(par2 + fd.offsetX, par3 + fd.offsetY, par4 + fd.offsetZ)]).getSize())
                {
                    isSmallScale = true;
                }
            }

            if (renderer.blockAccess.getBlockMaterial(par2 + fd.offsetX, par3 + fd.offsetY, par4 + fd.offsetZ) == Material.wood)
            {
                switch (fd)
                {
                    case DOWN:
                        isRender[fd.ordinal()] = par1Block.setDownBoundsBox(renderer.blockAccess, par2, par3, par4, isSmallScale);
                        break;

                    case UP:
                        isRender[fd.ordinal()] = par1Block.setUpBoundsBox(renderer.blockAccess, par2, par3, par4, isSmallScale);
                        break;

                    case EAST:
                        isRender[fd.ordinal()] = par1Block.setEastBoundsBox(renderer.blockAccess, par2, par3, par4, isSmallScale);
                        break;

                    case WEST:
                        isRender[fd.ordinal()] = par1Block.setWestBoundsBox(renderer.blockAccess, par2, par3, par4, isSmallScale);
                        break;

                    case NORTH:
                        isRender[fd.ordinal()] = par1Block.setNorthBoundsBox(renderer.blockAccess, par2, par3, par4, isSmallScale);
                        break;

                    case SOUTH:
                        isRender[fd.ordinal()] = par1Block.setSouthBoundsBox(renderer.blockAccess, par2, par3, par4, isSmallScale);
                        break;

                    default:
                        continue;
                }

                renderer.setRenderBoundsFromBlock(par1Block);

                if (!(isRender[0] || isRender[1]) || isRender[fd.ordinal()])
                {
                    if (isSideRender[0] || isNotUpAndDown)
                    {
                        renderBlockWithAmbientOcclusion(renderer, par1Block, par2, par3, par4, 1, 1, 1, 0);
                    }

                    if (isSideRender[1] || isNotUpAndDown)
                    {
                        renderBlockWithAmbientOcclusion(renderer, par1Block, par2, par3, par4, 1, 1, 1, 1);
                    }
                }

                if (!(isRender[2] || isRender[3]) || isRender[fd.ordinal()])
                {
                    if (isSideRender[2] || isNotNothAndSouth)
                    {
                        renderBlockWithAmbientOcclusion(renderer, par1Block, par2, par3, par4, 1, 1, 1, 2);
                    }

                    if (isSideRender[3] || isNotNothAndSouth)
                    {
                        renderBlockWithAmbientOcclusion(renderer, par1Block, par2, par3, par4, 1, 1, 1, 3);
                    }
                }

                if (!(isRender[4] || isRender[5]) || isRender[fd.ordinal()])
                {
                    if (isSideRender[4] || isNotEastAndWest)
                    {
                        renderBlockWithAmbientOcclusion(renderer, par1Block, par2, par3, par4, 1, 1, 1, 4);
                    }

                    if (isSideRender[5] || isNotEastAndWest)
                    {
                        renderBlockWithAmbientOcclusion(renderer, par1Block, par2, par3, par4, 1, 1, 1, 5);
                    }
                }
            }
        }

        renderer.enableAO = false;
        par1Block.setCoreBoundsBox(renderer.blockAccess, par2, par3, par4);
        renderer.setRenderBoundsFromBlock(par1Block);
        renderer.renderStandardBlockWithAmbientOcclusion(par1Block, par2, par3, par4, 1F, 1F, 1F);
    }
    private void renderBlockWithAmbientOcclusion(RenderBlocks renderer, Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7, int side)
    {
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag1 = true;
        int l = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        if (renderer.getBlockIcon(par1Block).getIconName().equals("grass_top"))
        {
            flag1 = false;
        }
        else if (renderer.hasOverrideBlockTexture())
        {
            flag1 = false;
        }

        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        float f7;
        int i1;

        if (side == ForgeDirection.DOWN.ordinal())
        {
            if (renderer.renderMinY <= 0.0D)
            {
                --par3;
            }

            renderer.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4);
            renderer.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4);
            flag3 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
            flag2 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
            flag5 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
            flag4 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

            if (!flag4 && !flag2)
            {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4 - 1);
                renderer.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4 - 1);
            }

            if (!flag5 && !flag2)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4 + 1);
                renderer.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4 + 1);
            }

            if (!flag4 && !flag3)
            {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4 - 1);
                renderer.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4 - 1);
            }

            if (!flag5 && !flag3)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4 + 1);
                renderer.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4 + 1);
            }

            if (renderer.renderMinY <= 0.0D)
            {
                ++par3;
            }

            i1 = l;

            if (renderer.renderMinY <= 0.0D || !renderer.blockAccess.isBlockOpaqueCube(par2, par3 - 1, par4))
            {
                i1 = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4);
            f3 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
            f6 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
            f5 = (f7 + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
            f4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + f7 + renderer.aoLightValueScratchYZNN) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, i1);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, i1);

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = par5 * 0.5F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = par6 * 0.5F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = par7 * 0.5F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.5F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.5F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.5F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            renderer.renderFaceYNeg(par1Block, (double)par2, (double)par3, (double)par4, renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 0));
            flag = true;
        }

        if (side == ForgeDirection.UP.ordinal())
        {
            if (renderer.renderMaxY >= 1.0D)
            {
                ++par3;
            }

            renderer.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4);
            renderer.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4);
            renderer.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 + 1);
            flag3 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
            flag2 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
            flag5 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
            flag4 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];

            if (!flag4 && !flag2)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4 - 1);
                renderer.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4 - 1);
            }

            if (!flag4 && !flag3)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4 - 1);
                renderer.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4 - 1);
            }

            if (!flag5 && !flag2)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4 + 1);
                renderer.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4 + 1);
            }

            if (!flag5 && !flag3)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4 + 1);
                renderer.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4 + 1);
            }

            if (renderer.renderMaxY >= 1.0D)
            {
                --par3;
            }

            i1 = l;

            if (renderer.renderMaxY >= 1.0D || !renderer.blockAccess.isBlockOpaqueCube(par2, par3 + 1, par4))
            {
                i1 = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4);
            f6 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + f7) / 4.0F;
            f3 = (renderer.aoLightValueScratchYZPP + f7 + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
            f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, i1);
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = par5;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = par6;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = par7;
            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            renderer.renderFaceYPos(par1Block, (double)par2, (double)par3, (double)par4, renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 1));
            flag = true;
        }

        float f8;
        float f9;
        float f10;
        float f11;
        int j1;
        int k1;
        int l1;
        int i2;
        Icon icon;

        if (side == ForgeDirection.NORTH.ordinal())
        {
            if (renderer.renderMinZ <= 0.0D)
            {
                --par4;
            }

            renderer.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4);
            renderer.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4);
            renderer.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4);
            renderer.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4);
            flag3 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
            flag2 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
            flag5 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
            flag4 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

            if (!flag2 && !flag4)
            {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3 - 1, par4);
                renderer.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3 - 1, par4);
            }

            if (!flag2 && !flag5)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3 + 1, par4);
                renderer.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3 + 1, par4);
            }

            if (!flag3 && !flag4)
            {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3 - 1, par4);
                renderer.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3 - 1, par4);
            }

            if (!flag3 && !flag5)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3 + 1, par4);
                renderer.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3 + 1, par4);
            }

            if (renderer.renderMinZ <= 0.0D)
            {
                ++par4;
            }

            i1 = l;

            if (renderer.renderMinZ <= 0.0D || !renderer.blockAccess.isBlockOpaqueCube(par2, par3, par4 - 1))
            {
                i1 = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 - 1);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 - 1);
            f9 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
            f8 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f11 = (renderer.aoLightValueScratchYZNN + f7 + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
            f10 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + f7) / 4.0F;
            f3 = (float)((double)f9 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f8 * renderer.renderMinY * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            f4 = (float)((double)f9 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f8 * renderer.renderMaxY * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            f5 = (float)((double)f9 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f8 * renderer.renderMinY * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            f6 = (float)((double)f9 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f8 * renderer.renderMinY * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
            k1 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
            j1 = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(k1, j1, i2, l1, renderer.renderMaxY * (1.0D - renderer.renderMinX), renderer.renderMaxY * renderer.renderMinX, (1.0D - renderer.renderMaxY) * renderer.renderMinX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(k1, j1, i2, l1, renderer.renderMaxY * (1.0D - renderer.renderMaxX), renderer.renderMaxY * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            renderer.brightnessBottomRight = renderer.mixAoBrightness(k1, j1, i2, l1, renderer.renderMinY * (1.0D - renderer.renderMaxX), renderer.renderMinY * renderer.renderMaxX, (1.0D - renderer.renderMinY) * renderer.renderMaxX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            renderer.brightnessTopRight = renderer.mixAoBrightness(k1, j1, i2, l1, renderer.renderMinY * (1.0D - renderer.renderMinX), renderer.renderMinY * renderer.renderMinX, (1.0D - renderer.renderMinY) * renderer.renderMinX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = par5 * 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = par6 * 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = par7 * 0.8F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            icon = renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 2);
            renderer.renderFaceZNeg(par1Block, (double)par2, (double)par3, (double)par4, icon);
            flag = true;
        }

        if (side == ForgeDirection.SOUTH.ordinal())
        {
            if (renderer.renderMaxZ >= 1.0D)
            {
                ++par4;
            }

            renderer.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4);
            renderer.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4);
            renderer.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4);
            renderer.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4);
            flag3 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
            flag2 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
            flag5 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
            flag4 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];

            if (!flag2 && !flag4)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3 - 1, par4);
                renderer.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3 - 1, par4);
            }

            if (!flag2 && !flag5)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3 + 1, par4);
                renderer.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3 + 1, par4);
            }

            if (!flag3 && !flag4)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3 - 1, par4);
                renderer.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3 - 1, par4);
            }

            if (!flag3 && !flag5)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3 + 1, par4);
                renderer.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3 + 1, par4);
            }

            if (renderer.renderMaxZ >= 1.0D)
            {
                --par4;
            }

            i1 = l;

            if (renderer.renderMaxZ >= 1.0D || !renderer.blockAccess.isBlockOpaqueCube(par2, par3, par4 + 1))
            {
                i1 = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 + 1);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 + 1);
            f9 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + f7 + renderer.aoLightValueScratchYZPP) / 4.0F;
            f8 = (f7 + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            f11 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
            f10 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
            f3 = (float)((double)f9 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f8 * renderer.renderMaxY * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            f4 = (float)((double)f9 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f8 * renderer.renderMinY * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
            f5 = (float)((double)f9 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f8 * renderer.renderMinY * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            f6 = (float)((double)f9 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f8 * renderer.renderMaxY * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            k1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, i1);
            j1 = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * renderer.renderMinX, renderer.renderMaxY * renderer.renderMinX);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * renderer.renderMinX, renderer.renderMinY * renderer.renderMinX);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * renderer.renderMaxX, renderer.renderMinY * renderer.renderMaxX);
            renderer.brightnessTopRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * renderer.renderMaxX, renderer.renderMaxY * renderer.renderMaxX);

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = par5 * 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = par6 * 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = par7 * 0.8F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            icon = renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 3);
            renderer.renderFaceZPos(par1Block, (double)par2, (double)par3, (double)par4, renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 3));
            flag = true;
        }

        if (side == ForgeDirection.WEST.ordinal())
        {
            if (renderer.renderMinX <= 0.0D)
            {
                --par2;
            }

            renderer.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4);
            renderer.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4);
            flag3 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
            flag2 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
            flag5 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
            flag4 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];

            if (!flag5 && !flag2)
            {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4 - 1);
                renderer.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4 - 1);
            }

            if (!flag4 && !flag2)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4 + 1);
                renderer.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4 + 1);
            }

            if (!flag5 && !flag3)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4 - 1);
                renderer.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4 - 1);
            }

            if (!flag4 && !flag3)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4 + 1);
                renderer.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4 + 1);
            }

            if (renderer.renderMinX <= 0.0D)
            {
                ++par2;
            }

            i1 = l;

            if (renderer.renderMinX <= 0.0D || !renderer.blockAccess.isBlockOpaqueCube(par2 - 1, par3, par4))
            {
                i1 = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4);
            f9 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + f7 + renderer.aoLightValueScratchXZNP) / 4.0F;
            f8 = (f7 + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
            f11 = (renderer.aoLightValueScratchXZNN + f7 + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
            f10 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + f7) / 4.0F;
            f3 = (float)((double)f8 * renderer.renderMaxY * renderer.renderMaxZ + (double)f11 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f10 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f9 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
            f4 = (float)((double)f8 * renderer.renderMaxY * renderer.renderMinZ + (double)f11 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f10 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f9 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
            f5 = (float)((double)f8 * renderer.renderMinY * renderer.renderMinZ + (double)f11 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f10 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f9 * (1.0D - renderer.renderMinY) * renderer.renderMinZ);
            f6 = (float)((double)f8 * renderer.renderMinY * renderer.renderMaxZ + (double)f11 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f10 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f9 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, i1);
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * renderer.renderMaxZ, renderer.renderMaxY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * renderer.renderMinZ, renderer.renderMaxY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * renderer.renderMinZ, renderer.renderMinY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * renderer.renderMinZ);
            renderer.brightnessTopRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * renderer.renderMaxZ, renderer.renderMinY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * renderer.renderMaxZ);

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = par5 * 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = par6 * 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = par7 * 0.6F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            icon = renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 4);
            renderer.renderFaceXNeg(par1Block, (double)par2, (double)par3, (double)par4, icon);
            flag = true;
        }

        if (side == ForgeDirection.EAST.ordinal())
        {
            if (renderer.renderMaxX >= 1.0D)
            {
                ++par2;
            }

            renderer.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4);
            renderer.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4);
            flag3 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
            flag2 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
            flag5 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
            flag4 = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];

            if (!flag2 && !flag4)
            {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4 - 1);
                renderer.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4 - 1);
            }

            if (!flag2 && !flag5)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4 + 1);
                renderer.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4 + 1);
            }

            if (!flag3 && !flag4)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4 - 1);
                renderer.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4 - 1);
            }

            if (!flag3 && !flag5)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4 + 1);
                renderer.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4 + 1);
            }

            if (renderer.renderMaxX >= 1.0D)
            {
                --par2;
            }

            i1 = l;

            if (renderer.renderMaxX >= 1.0D || !renderer.blockAccess.isBlockOpaqueCube(par2 + 1, par3, par4))
            {
                i1 = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4);
            f9 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + f7 + renderer.aoLightValueScratchXZPP) / 4.0F;
            f8 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + f7) / 4.0F;
            f11 = (renderer.aoLightValueScratchXZPN + f7 + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
            f10 = (f7 + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            f3 = (float)((double)f9 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ + (double)f8 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f11 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMinY * renderer.renderMaxZ);
            f4 = (float)((double)f9 * (1.0D - renderer.renderMinY) * renderer.renderMinZ + (double)f8 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f11 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMinY * renderer.renderMinZ);
            f5 = (float)((double)f9 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ + (double)f8 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f11 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMaxY * renderer.renderMinZ);
            f6 = (float)((double)f9 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ + (double)f8 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f11 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMaxY * renderer.renderMaxZ);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(k1, l1, i2, j1, (1.0D - renderer.renderMinY) * renderer.renderMaxZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), renderer.renderMinY * (1.0D - renderer.renderMaxZ), renderer.renderMinY * renderer.renderMaxZ);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(k1, l1, i2, j1, (1.0D - renderer.renderMinY) * renderer.renderMinZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), renderer.renderMinY * (1.0D - renderer.renderMinZ), renderer.renderMinY * renderer.renderMinZ);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(k1, l1, i2, j1, (1.0D - renderer.renderMaxY) * renderer.renderMinZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), renderer.renderMaxY * (1.0D - renderer.renderMinZ), renderer.renderMaxY * renderer.renderMinZ);
            renderer.brightnessTopRight = renderer.mixAoBrightness(k1, l1, i2, j1, (1.0D - renderer.renderMaxY) * renderer.renderMaxZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * renderer.renderMaxZ);

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = par5 * 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = par6 * 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = par7 * 0.6F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            icon = renderer.getBlockIcon(par1Block, renderer.blockAccess, par2, par3, par4, 5);
            renderer.renderFaceXPos(par1Block, (double)par2, (double)par3, (double)par4, icon);
            flag = true;
        }
    }

    public void renderBlockRiceField(RenderBlocks renderer, BlockRiceField block, int x,
                                     int y, int z)
    {
        if (block.renderPass == 0)
        {
            this.renderField(renderer, block, x, y, z);
        }
        else
        {
            this.renderBlockFluids(renderer, block, x, y, z);
        }
    }

    private void renderField(RenderBlocks renderer, Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        boolean flag1 = par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3 - 1, par4, 0);
        boolean[] aboolean = new boolean[] {par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 - 1, 2), par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 + 1, 3), par1Block.shouldSideBeRendered(renderer.blockAccess, par2 - 1, par3, par4, 4), par1Block.shouldSideBeRendered(renderer.blockAccess, par2 + 1, par3, par4, 5)};
        Icon top = renderer.getBlockIconFromSide(par1Block, 1);
        Icon other = renderer.getBlockIconFromSide(par1Block, 0);
        tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4));
        renderer.renderFaceYPos(par1Block, par2, par3, par4, top);

        if (flag1)
        {
            renderer.renderFaceYNeg(par1Block, par2, par3, par4, other);
        }

        for (int i = 0; i < 4; i++)
        {
            if (aboolean[i])
            {
                switch (i)
                {
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
    //水レンダー
    private boolean renderBlockFluids(RenderBlocks renderer, Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int l = par1Block.colorMultiplier(renderer.blockAccess, par2, par3, par4);
        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;
        boolean flag = par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3 + 1, par4, 1);
        boolean flag1 = par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3 - 1, par4, 0);
        boolean[] aboolean = new boolean[] {par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 - 1, 2), par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 + 1, 3), par1Block.shouldSideBeRendered(renderer.blockAccess, par2 - 1, par3, par4, 4), par1Block.shouldSideBeRendered(renderer.blockAccess, par2 + 1, par3, par4, 5)};

        if (!flag && !flag1 && !aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3])
        {
            return false;
        }
        else
        {
            boolean flag2 = false;
            float f3 = 0.5F;
            float f4 = 1.0F;
            float f5 = 0.8F;
            float f6 = 0.6F;
            double d0 = 0.0D;
            double d1 = 1.0D;
            Material material = par1Block.blockMaterial;
            int i1 = renderer.blockAccess.getBlockMetadata(par2, par3, par4);
            double d2 = (double)this.getFluidHeight(renderer, par2, par3, par4, material);
            double d3 = (double)this.getFluidHeight(renderer, par2, par3, par4 + 1, material);
            double d4 = (double)this.getFluidHeight(renderer, par2 + 1, par3, par4 + 1, material);
            double d5 = (double)this.getFluidHeight(renderer, par2 + 1, par3, par4, material);
            double d6 = 0.0010000000474974513D;
            float f7;
            float f8;
            float f9;

            if (d2 == 0)
            {
                return false;
            }

            if (renderer.renderAllFaces || flag)
            {
                flag2 = true;
                //水のIconを使用
                Icon icon = renderer.getBlockIconFromSideAndMetadata(Block.waterMoving, 1, i1);
                float f10 = (float)BlockFluid.getFlowDirection(renderer.blockAccess, par2, par3, par4, Material.water);

                if (f10 > -999.0F)
                {
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

                if (f10 < -999.0F)
                {
                    d8 = (double)icon.getInterpolatedU(0.0D);
                    d12 = (double)icon.getInterpolatedV(0.0D);
                    d7 = d8;
                    d11 = (double)icon.getInterpolatedV(16.0D);
                    d10 = (double)icon.getInterpolatedU(16.0D);
                    d14 = d11;
                    d9 = d10;
                    d13 = d12;
                }
                else
                {
                    f9 = MathHelper.sin(f10) * 0.25F;
                    f8 = MathHelper.cos(f10) * 0.25F;
                    f7 = 8.0F;
                    d8 = (double)icon.getInterpolatedU((double)(8.0F + (-f8 - f9) * 16.0F));
                    d12 = (double)icon.getInterpolatedV((double)(8.0F + (-f8 + f9) * 16.0F));
                    d7 = (double)icon.getInterpolatedU((double)(8.0F + (-f8 + f9) * 16.0F));
                    d11 = (double)icon.getInterpolatedV((double)(8.0F + (f8 + f9) * 16.0F));
                    d10 = (double)icon.getInterpolatedU((double)(8.0F + (f8 + f9) * 16.0F));
                    d14 = (double)icon.getInterpolatedV((double)(8.0F + (f8 - f9) * 16.0F));
                    d9 = (double)icon.getInterpolatedU((double)(8.0F + (f8 - f9) * 16.0F));
                    d13 = (double)icon.getInterpolatedV((double)(8.0F + (-f8 - f9) * 16.0F));
                }

                tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4));
                f9 = 1.0F;
                tessellator.setColorOpaque_F(f4 * f9 * f, f4 * f9 * f1, f4 * f9 * f2);
                tessellator.addVertexWithUV((double)(par2 + 0), (double)par3 + d2 + 0.5F, (double)(par4 + 0), d8, d12);
                tessellator.addVertexWithUV((double)(par2 + 0), (double)par3 + d3 + 0.5F, (double)(par4 + 1), d7, d11);
                tessellator.addVertexWithUV((double)(par2 + 1), (double)par3 + d4 + 0.5F, (double)(par4 + 1), d10, d14);
                tessellator.addVertexWithUV((double)(par2 + 1), (double)par3 + d5 + 0.5F, (double)(par4 + 0), d9, d13);
            }

            if (renderer.renderAllFaces || flag1)
            {
                tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4));
                float f11 = 1.0F;
                tessellator.setColorOpaque_F(f3 * f11, f3 * f11, f3 * f11);
                //renderer.renderFaceYNeg(Block.waterMoving, (double)par2, (double)par3 + d6, (double)par4, renderer.getBlockIconFromSide(Block.waterMoving, 0));
                flag2 = true;
            }

            for (int j1 = 0; j1 < 4; ++j1)
            {
                int k1 = par2;
                int l1 = par4;

                if (j1 == 0)
                {
                    l1 = par4 - 1;
                }

                if (j1 == 1)
                {
                    ++l1;
                }

                if (j1 == 2)
                {
                    k1 = par2 - 1;
                }

                if (j1 == 3)
                {
                    ++k1;
                }

                //水のIconを使用するように
                Icon icon1 = renderer.getBlockIconFromSideAndMetadata(Block.waterMoving, j1 + 2, i1);

                if (renderer.renderAllFaces || aboolean[j1])
                {
                    double d15;
                    double d16;
                    double d17;
                    double d18;
                    double d19;
                    double d20;

                    if (j1 == 0)
                    {
                        d15 = d2;
                        d17 = d5;
                        d16 = (double)par2;
                        d18 = (double)(par2 + 1);
                        d19 = (double)par4 + d6;
                        d20 = (double)par4 + d6;
                    }
                    else if (j1 == 1)
                    {
                        d15 = d4;
                        d17 = d3;
                        d16 = (double)(par2 + 1);
                        d18 = (double)par2;
                        d19 = (double)(par4 + 1) - d6;
                        d20 = (double)(par4 + 1) - d6;
                    }
                    else if (j1 == 2)
                    {
                        d15 = d3;
                        d17 = d2;
                        d16 = (double)par2 + d6;
                        d18 = (double)par2 + d6;
                        d19 = (double)(par4 + 1);
                        d20 = (double)par4;
                    }
                    else
                    {
                        d15 = d5;
                        d17 = d4;
                        d16 = (double)(par2 + 1) - d6;
                        d18 = (double)(par2 + 1) - d6;
                        d19 = (double)par4;
                        d20 = (double)(par4 + 1);
                    }

                    flag2 = true;
                    float f12 = icon1.getInterpolatedU(0.0D);
                    f9 = icon1.getInterpolatedU(8.0D);
                    f8 = icon1.getInterpolatedV((1.0D - d15) * 16.0D * 0.5D);
                    f7 = icon1.getInterpolatedV((1.0D - d17) * 16.0D * 0.5D);
                    float f13 = icon1.getInterpolatedV(8.0D);
                    tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(renderer.blockAccess, k1, par3, l1));
                    float f14 = 1.0F;

                    if (j1 < 2)
                    {
                        f14 *= f5;
                    }
                    else
                    {
                        f14 *= f6;
                    }

                    tessellator.setColorOpaque_F(f4 * f14 * f, f4 * f14 * f1, f4 * f14 * f2);
                    tessellator.addVertexWithUV(d16, (double)par3 + d15 + 0.5F, d19, (double)f12, (double)f8);
                    tessellator.addVertexWithUV(d18, (double)par3 + d17 + 0.5F, d20, (double)f9, (double)f7);
                    tessellator.addVertexWithUV(d18, (double)(par3 + 0) + 0.5F, d20, (double)f9, (double)f13);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 0) + 0.5F, d19, (double)f12, (double)f13);
                }
            }

            renderer.renderMinY = d0;
            renderer.renderMaxY = d1;
            return flag2;
        }
    }
    //バニラと違うメタデータによる水の高さ表現のため
    private float getFluidHeight(RenderBlocks renderer, int par1, int par2, int par3, Material par4Material)
    {
        int l = 0;
        float f = 0.0F;

        for (int i1 = 0; i1 < 4; ++i1)
        {
            int j1 = par1 - (i1 & 1);
            int k1 = par3 - (i1 >> 1 & 1);

            if (renderer.blockAccess.getBlockMaterial(j1, par2 + 1, k1) == par4Material)
            {
                return 1.0F;
            }

            Material material1 = renderer.blockAccess.getBlockMaterial(j1, par2, k1);

            if (material1 == par4Material)
            {
                int l1 = renderer.blockAccess.getBlockMetadata(j1, par2, k1) & 3;
                l1 += 4;
                f += getFluidHeightPercent(l1);
                ++l;
            }
            else if (!material1.isSolid())
            {
                ++f;
                ++l;
            }
        }

        return 1.0F - f / (float)l;
    }
    private float getFluidHeightPercent(float par0)
    {
        return (float)(par0 + 1) / (par0 != 7 ? 9.0F : 8.0F);
    }
    //バニラのPanelはメタデータによるIcon変更をサポートしないため追加
    //フチを描画しないオプションも追加
    public boolean renderBlockBambooPane(RenderBlocks renderblocks, BlockBambooPane par1BlockPane, int par2, int par3, int par4)
    {
        int l = renderblocks.blockAccess.getHeight();
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(par1BlockPane.getMixedBrightnessForBlock(renderblocks.blockAccess, par2, par3, par4));
        float f = 1.0F;
        int i1 = par1BlockPane.colorMultiplier(renderblocks.blockAccess, par2, par3, par4);
        float f1 = (float)(i1 >> 16 & 255) / 255.0F;
        float f2 = (float)(i1 >> 8 & 255) / 255.0F;
        float f3 = (float)(i1 & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
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
        int j1;
        j1 = renderblocks.blockAccess.getBlockMetadata(par2, par3, par4);
        icon = renderblocks.getBlockIconFromSideAndMetadata(par1BlockPane, 0, j1);
        icon1 = par1BlockPane.getIcon(0, j1);
        boolean isSideRender = par1BlockPane.isSideRender(renderblocks.blockAccess, par2, par3, par4);
        j1 = icon.getIconWidth();
        int k1 = icon.getIconHeight();
        double d0 = (double)icon.getMinU();
        double d1 = (double)icon.getInterpolatedU(8.0D);
        double d2 = (double)icon.getMaxU();
        double d3 = (double)icon.getMinV();
        double d4 = (double)icon.getMaxV();
        int l1 = icon.getIconWidth();
        int i2 = icon.getIconHeight();
        double d5 = (double)icon1.getInterpolatedU(7.0D);
        double d6 = (double)icon1.getInterpolatedU(9.0D);
        double d7 = (double)icon1.getMinV();
        double d8 = (double)icon1.getInterpolatedV(8.0D);
        double d9 = (double)icon1.getMaxV();
        double d10 = (double)par2;
        double d11 = (double)par2 + 0.5D;
        double d12 = (double)(par2 + 1);
        double d13 = (double)par4;
        double d14 = (double)par4 + 0.5D;
        double d15 = (double)(par4 + 1);
        double d16 = (double)par2 + 0.5D - 0.0625D;
        double d17 = (double)par2 + 0.5D + 0.0625D;
        double d18 = (double)par4 + 0.5D - 0.0625D;
        double d19 = (double)par4 + 0.5D + 0.0625D;
        boolean flag = par1BlockPane.canThisPaneConnectToThisBlockID(renderblocks.blockAccess.getBlockId(par2, par3, par4 - 1));
        boolean flag1 = par1BlockPane.canThisPaneConnectToThisBlockID(renderblocks.blockAccess.getBlockId(par2, par3, par4 + 1));
        boolean flag2 = par1BlockPane.canThisPaneConnectToThisBlockID(renderblocks.blockAccess.getBlockId(par2 - 1, par3, par4));
        boolean flag3 = par1BlockPane.canThisPaneConnectToThisBlockID(renderblocks.blockAccess.getBlockId(par2 + 1, par3, par4));
        boolean flag4 = par1BlockPane.shouldSideBeRendered(renderblocks.blockAccess, par2, par3 + 1, par4, 1);
        boolean flag5 = par1BlockPane.shouldSideBeRendered(renderblocks.blockAccess, par2, par3 - 1, par4, 0);

        if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1))
        {
            if (flag2 && !flag3)
            {
                tessellator.addVertexWithUV(d10, (double)(par3 + 1), d14, d0, d3);
                tessellator.addVertexWithUV(d10, (double)(par3 + 0), d14, d0, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d1, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d1, d3);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d0, d3);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d0, d4);
                tessellator.addVertexWithUV(d10, (double)(par3 + 0), d14, d1, d4);
                tessellator.addVertexWithUV(d10, (double)(par3 + 1), d14, d1, d3);

                if (isSideRender)
                {
                    if (!flag1 && !flag)
                    {
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1), d19, d5, d7);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 0), d19, d5, d9);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 0), d18, d6, d9);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1), d18, d6, d7);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1), d18, d5, d7);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 0), d18, d5, d9);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 0), d19, d6, d9);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1), d19, d6, d7);
                    }

                    if (flag4 || par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2 - 1, par3 + 1, par4))
                    {
                        tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                    }

                    if (flag5 || par3 > 1 && renderblocks.blockAccess.isAirBlock(par2 - 1, par3 - 1, par4))
                    {
                        tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
                    }
                }
            }
            else if (!flag2 && flag3)
            {
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d1, d3);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d1, d4);
                tessellator.addVertexWithUV(d12, (double)(par3 + 0), d14, d2, d4);
                tessellator.addVertexWithUV(d12, (double)(par3 + 1), d14, d2, d3);
                tessellator.addVertexWithUV(d12, (double)(par3 + 1), d14, d1, d3);
                tessellator.addVertexWithUV(d12, (double)(par3 + 0), d14, d1, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d2, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d2, d3);

                if (isSideRender)
                {
                    if (!flag1 && !flag)
                    {
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1), d18, d5, d7);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 0), d18, d5, d9);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 0), d19, d6, d9);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1), d19, d6, d7);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1), d19, d5, d7);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 0), d19, d5, d9);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 0), d18, d6, d9);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1), d18, d6, d7);
                    }

                    if (flag4 || par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2 + 1, par3 + 1, par4))
                    {
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                        tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                    }

                    if (flag5 || par3 > 1 && renderblocks.blockAccess.isAirBlock(par2 + 1, par3 - 1, par4))
                    {
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d7);
                        tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
                    }
                }
            }
        }
        else
        {
            tessellator.addVertexWithUV(d10, (double)(par3 + 1), d14, d0, d3);
            tessellator.addVertexWithUV(d10, (double)(par3 + 0), d14, d0, d4);
            tessellator.addVertexWithUV(d12, (double)(par3 + 0), d14, d2, d4);
            tessellator.addVertexWithUV(d12, (double)(par3 + 1), d14, d2, d3);
            tessellator.addVertexWithUV(d12, (double)(par3 + 1), d14, d0, d3);
            tessellator.addVertexWithUV(d12, (double)(par3 + 0), d14, d0, d4);
            tessellator.addVertexWithUV(d10, (double)(par3 + 0), d14, d2, d4);
            tessellator.addVertexWithUV(d10, (double)(par3 + 1), d14, d2, d3);

            if (isSideRender)
            {
                if (flag4)
                {
                    tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                }
                else
                {
                    if (par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2 - 1, par3 + 1, par4))
                    {
                        tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                    }

                    if (par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2 + 1, par3 + 1, par4))
                    {
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                        tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                    }
                }

                if (flag5)
                {
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d9);
                }
                else
                {
                    if (par3 > 1 && renderblocks.blockAccess.isAirBlock(par2 - 1, par3 - 1, par4))
                    {
                        tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
                        tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
                    }

                    if (par3 > 1 && renderblocks.blockAccess.isAirBlock(par2 + 1, par3 - 1, par4))
                    {
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d7);
                        tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                        tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
                        tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
                    }
                }
            }
        }

        if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1))
        {
            if (flag && !flag1)
            {
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d13, d0, d3);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d13, d0, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d1, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d1, d3);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d0, d3);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d0, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d13, d1, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d13, d1, d3);

                if (isSideRender)
                {
                    if (!flag3 && !flag2)
                    {
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d5, d7);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 0), d14, d5, d9);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 0), d14, d6, d9);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d6, d7);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d5, d7);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 0), d14, d5, d9);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 0), d14, d6, d9);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d6, d7);
                    }

                    if (flag4 || par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2, par3 + 1, par4 - 1))
                    {
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d13, d6, d7);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d13, d5, d7);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d6, d7);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d13, d6, d8);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d13, d5, d8);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d5, d7);
                    }

                    if (flag5 || par3 > 1 && renderblocks.blockAccess.isAirBlock(par2, par3 - 1, par4 - 1))
                    {
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d13, d6, d7);
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d13, d5, d7);
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d6, d7);
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d13, d6, d8);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d13, d5, d8);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d5, d7);
                    }
                }
            }
            else if (!flag && flag1)
            {
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d1, d3);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d1, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d15, d2, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d15, d2, d3);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d15, d1, d3);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d15, d1, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 0), d14, d2, d4);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1), d14, d2, d3);

                if (isSideRender)
                {
                    if (!flag3 && !flag2)
                    {
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d5, d7);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 0), d14, d5, d9);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 0), d14, d6, d9);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d6, d7);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1), d14, d5, d7);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 0), d14, d5, d9);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 0), d14, d6, d9);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1), d14, d6, d7);
                    }

                    if (flag4 || par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2, par3 + 1, par4 + 1))
                    {
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d15, d5, d9);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d15, d6, d9);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d15, d5, d8);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d5, d9);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d6, d9);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d15, d6, d8);
                    }

                    if (flag5 || par3 > 1 && renderblocks.blockAccess.isAirBlock(par2, par3 - 1, par4 + 1))
                    {
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d15, d5, d9);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d15, d6, d9);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d15, d5, d8);
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d5, d9);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d6, d9);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d15, d6, d8);
                    }
                }
            }
        }
        else
        {
            tessellator.addVertexWithUV(d11, (double)(par3 + 1), d15, d0, d3);
            tessellator.addVertexWithUV(d11, (double)(par3 + 0), d15, d0, d4);
            tessellator.addVertexWithUV(d11, (double)(par3 + 0), d13, d2, d4);
            tessellator.addVertexWithUV(d11, (double)(par3 + 1), d13, d2, d3);
            tessellator.addVertexWithUV(d11, (double)(par3 + 1), d13, d0, d3);
            tessellator.addVertexWithUV(d11, (double)(par3 + 0), d13, d0, d4);
            tessellator.addVertexWithUV(d11, (double)(par3 + 0), d15, d2, d4);
            tessellator.addVertexWithUV(d11, (double)(par3 + 1), d15, d2, d3);

            if (isSideRender)
            {
                if (flag4)
                {
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d13, d6, d9);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d15, d6, d7);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d15, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d13, d5, d9);
                }
                else
                {
                    if (par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2, par3 + 1, par4 - 1))
                    {
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d13, d6, d7);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d13, d5, d7);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d6, d7);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d13, d6, d8);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d13, d5, d8);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d5, d7);
                    }

                    if (par3 < l - 1 && renderblocks.blockAccess.isAirBlock(par2, par3 + 1, par4 + 1))
                    {
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d15, d5, d9);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d15, d6, d9);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d15, d5, d8);
                        tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.005D, d14, d5, d9);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d14, d6, d9);
                        tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.005D, d15, d6, d8);
                    }
                }

                if (flag5)
                {
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d13, d6, d9);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d15, d6, d7);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d15, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d13, d5, d9);
                }
                else
                {
                    if (par3 > 1 && renderblocks.blockAccess.isAirBlock(par2, par3 - 1, par4 - 1))
                    {
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d13, d6, d7);
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d13, d5, d7);
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d6, d7);
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d13, d6, d8);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d13, d5, d8);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d5, d7);
                    }

                    if (par3 > 1 && renderblocks.blockAccess.isAirBlock(par2, par3 - 1, par4 + 1))
                    {
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d5, d8);
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d15, d5, d9);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d15, d6, d9);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d6, d8);
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d15, d5, d8);
                        tessellator.addVertexWithUV(d16, (double)par3 - 0.005D, d14, d5, d9);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d14, d6, d9);
                        tessellator.addVertexWithUV(d17, (double)par3 - 0.005D, d15, d6, d8);
                    }
                }
            }
        }

        return true;
    }
}
