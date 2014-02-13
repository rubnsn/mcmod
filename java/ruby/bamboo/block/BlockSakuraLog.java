package ruby.bamboo.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import ruby.bamboo.BambooCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSakuraLog extends BlockLog {
    private static IIcon top;
    private static IIcon side;

    public BlockSakuraLog() {
        super();
        setHardness(2.0F);
        setStepSound(Block.soundTypeWood);
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
    }

    @Override
    public IIcon getIcon(int par1, int par2) {
        if (par2 == 0) {
            if (par1 == 0 || par1 == 1) {
                return top;
            } else {
                return side;
            }
        } else if (par2 == 4) {
            if (par1 == 4 || par1 == 5) {
                return top;
            } else {
                return side;
            }
        } else // if(par2==8){
        {
            if (par1 == 2 || par1 == 3) {
                return top;
            } else {
                return side;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        BlockSakuraLog.top = par1IconRegister.registerIcon(BambooCore.resourceDomain + "sakuralog_t");
        BlockSakuraLog.side = par1IconRegister.registerIcon(BambooCore.resourceDomain + "sakuralog_s");
    }
}
