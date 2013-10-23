package ruby.bamboo.block;

import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockKayabukiRoof extends BlockStairs {
    /** The block that is used as model for the stair. */

    public BlockKayabukiRoof(int par1) {
        super(par1, Block.blocksList[BambooInit.dSquareBID], 4);
        this.setStepSound(Block.soundSandFootstep);
        setHardness(1.0F);
        // setLightValue(0.01F);
        this.setLightOpacity(1);
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
    public void registerIcons(IconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(BambooCore.resorceDmain + "kaya_y");
    }

}
