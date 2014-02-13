package ruby.bamboo.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKayabukiRoof extends BlockStairs {
    public BlockKayabukiRoof() {
        super(BambooInit.dSquare, 4);
        this.setStepSound(Block.soundTypeSand);
        setHardness(1.0F);
        useNeighborBrightness = true;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(BambooCore.resourceDomain + "kaya_y");
    }

}
