package ruby.bamboo.render.block;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import ruby.bamboo.block.IPillarRender;

public class RenderPillar implements IRenderBlocks, IRenderInventory {
    public static final RenderPillar instance = new RenderPillar();

    @Override
    public void renderBlock(RenderBlocks renderblocks, Block par1Block, int par2, int par3, int par4) {
        renderBlockPillar(renderblocks, par1Block, par2, par3, par4);
    }

    private void renderBlockPillar(RenderBlocks renderblocks, Block pillar, int par2, int par3, int par4) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);
        IIcon icon = renderblocks.getBlockIcon((Block) pillar);
        int meta = renderblocks.blockAccess.getBlockMetadata(par2, par3, par4);
        boolean[] isSideRender = new boolean[] { pillar.shouldSideBeRendered(renderblocks.blockAccess, par2, par3 - 1, par4, 0), pillar.shouldSideBeRendered(renderblocks.blockAccess, par2, par3 + 1, par4, 1), pillar.shouldSideBeRendered(renderblocks.blockAccess, par2, par3, par4 - 1, 2), pillar.shouldSideBeRendered(renderblocks.blockAccess, par2, par3, par4 + 1, 3), pillar.shouldSideBeRendered(renderblocks.blockAccess, par2 - 1, par3, par4, 4), pillar.shouldSideBeRendered(renderblocks.blockAccess, par2 + 1, par3, par4, 5) };
        boolean[] isRender = new boolean[6];
        boolean isSmallScale;
        renderblocks.enableAO = true;

        for (ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS) {
            if (((IPillarRender) pillar).isLinkSkipp() && (fd == ForgeDirection.getOrientation(meta) || fd == ForgeDirection.getOrientation(meta).getOpposite())) {
                continue;// 同一方向のリンクはなし
            }

            Arrays.fill(isRender, false);
            isSmallScale = false;

            if (renderblocks.blockAccess.getBlock(par2 + fd.offsetX, par3 + fd.offsetY, par4 + fd.offsetZ) instanceof IPillarRender) {
                if (((IPillarRender) pillar).canDifferentMetaLink(meta, renderblocks.blockAccess.getBlockMetadata(par2 + fd.offsetX, par3 + fd.offsetY, par4 + fd.offsetZ))) {
                    continue;
                }

                if (((IPillarRender) pillar).getSize() > ((IPillarRender) renderblocks.blockAccess.getBlock(par2 + fd.offsetX, par3 + fd.offsetY, par4 + fd.offsetZ)).getSize()) {
                    isSmallScale = true;
                }
            }

            if (((IPillarRender) pillar).isLink(renderblocks.blockAccess, par2, par3, par4, fd)) {
                switch (fd) {
                case DOWN:
                    isRender[fd.ordinal()] = ((IPillarRender) pillar).setDownBoundsBox(renderblocks.blockAccess, par2, par3, par4, isSmallScale);
                    break;
                case UP:
                    isRender[fd.ordinal()] = ((IPillarRender) pillar).setUpBoundsBox(renderblocks.blockAccess, par2, par3, par4, isSmallScale);
                    break;

                case EAST:
                    isRender[fd.ordinal()] = ((IPillarRender) pillar).setEastBoundsBox(renderblocks.blockAccess, par2, par3, par4, isSmallScale);
                    break;

                case WEST:
                    isRender[fd.ordinal()] = ((IPillarRender) pillar).setWestBoundsBox(renderblocks.blockAccess, par2, par3, par4, isSmallScale);
                    break;

                case NORTH:
                    isRender[fd.ordinal()] = ((IPillarRender) pillar).setNorthBoundsBox(renderblocks.blockAccess, par2, par3, par4, isSmallScale);
                    break;

                case SOUTH:
                    isRender[fd.ordinal()] = ((IPillarRender) pillar).setSouthBoundsBox(renderblocks.blockAccess, par2, par3, par4, isSmallScale);
                    break;

                default:
                    continue;
                }

                renderblocks.setRenderBoundsFromBlock(pillar);
                if (!(isRender[0] || isRender[1])) {
                    if (isSideRender[0] || isRender[2] || isRender[3] || isRender[4] || isRender[5]) {
                        renderBlockWithAmbientOcclusion(renderblocks, pillar, par2, par3, par4, 1, 1, 1, 0);
                    }

                    if (isSideRender[1] || isRender[2] || isRender[3] || isRender[4] || isRender[5]) {
                        renderBlockWithAmbientOcclusion(renderblocks, pillar, par2, par3, par4, 1, 1, 1, 1);
                    }
                }

                if (!(isRender[2] || isRender[3])) {
                    if (isSideRender[2] || isRender[0] || isRender[1] || isRender[4] || isRender[5]) {
                        renderBlockWithAmbientOcclusion(renderblocks, pillar, par2, par3, par4, 1, 1, 1, 2);
                    }

                    if (isSideRender[3] || isRender[0] || isRender[1] || isRender[4] || isRender[5]) {
                        renderBlockWithAmbientOcclusion(renderblocks, pillar, par2, par3, par4, 1, 1, 1, 3);
                    }
                }

                if (!(isRender[4] || isRender[5])) {
                    if (isSideRender[4] || isRender[0] || isRender[1] || isRender[2] || isRender[3]) {
                        renderBlockWithAmbientOcclusion(renderblocks, pillar, par2, par3, par4, 1, 1, 1, 4);
                    }

                    if (isSideRender[5] || isRender[0] || isRender[1] || isRender[2] || isRender[3]) {
                        renderBlockWithAmbientOcclusion(renderblocks, pillar, par2, par3, par4, 1, 1, 1, 5);
                    }
                }
            }
        }

        renderblocks.enableAO = false;
        ((IPillarRender) pillar).setCoreBoundsBox(renderblocks.blockAccess, par2, par3, par4);
        renderblocks.setRenderBoundsFromBlock(pillar);
        renderblocks.renderStandardBlockWithAmbientOcclusion(pillar, par2, par3, par4, 1F, 1F, 1F);
    }

    //RenderBlocksの同名メソッドがprivateなため?
    private void renderBlockWithAmbientOcclusion(RenderBlocks renderer, Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7, int side) {
        ((IPillarRender) par1Block).setRenderSide(side);
        renderer.renderStandardBlockWithAmbientOcclusion(par1Block, par2, par3, par4, par5, par6, par7);
    }

    @Override
    public void renderInventory(RenderBlocks renderBlocks, Block block, int metadata) {
        RenderInventoryHelper.standardItemRender(block, renderBlocks, metadata);
    }

}
