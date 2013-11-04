package ruby.bamboo.render.block;

import ruby.bamboo.block.IDelude;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.RenderBlocks;

public class RenderDelude implements IRenderBlocks {

    @Override
    public void render(RenderBlocks renderblocks, Block par1Block, int par2, int par3, int par4) {
        boolean isGrass = false;

        if (renderblocks.getBlockIcon(par1Block, renderblocks.blockAccess, par2, par3, par4, 1).getIconName().equals("grass_top")) {
            ((IDelude) par1Block).setIconGrass(true);
            isGrass = true;
            RenderBlocks.fancyGrass = true;
        }
        switch (((IDelude) par1Block).getOriginalRenderType()) {
        case 0:
            renderblocks.renderStandardBlock(par1Block, par2, par3, par4);
            break;

        case 10:
            renderblocks.renderBlockStairs((BlockStairs) par1Block, par2, par3, par4);
            break;
        }

        if (isGrass) {
            ((IDelude) par1Block).setIconGrass(false);
            RenderBlocks.fancyGrass = false;
        }
    }

}
