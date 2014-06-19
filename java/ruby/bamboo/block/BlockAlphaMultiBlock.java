package ruby.bamboo.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import ruby.bamboo.BambooCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAlphaMultiBlock extends BlockMultiBlock {

    @Override
    public boolean canPlaceBlock(Block block) {
        return block.getRenderType() == 0 && isCube(block) && block.getRenderBlockPass() == 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getItemIconName() {
        return BambooCore.resourceDomain + "alphamultiblock";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.blockIcon = p_149651_1_.registerIcon(BambooCore.resourceDomain + "alphamultiblock");
    }

}
