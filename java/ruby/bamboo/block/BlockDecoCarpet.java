package ruby.bamboo.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ruby.bamboo.BambooInit;
import ruby.bamboo.BambooUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDecoCarpet extends BlockCarpet {
    public BlockDecoCarpet() {
        super();
        setTickRandomly(false);
        setStepSound(Block.soundTypeSand);
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        int meta = (par1World.getBlockMetadata(par2, par3, par4)) + ((BambooUtil.getPlayerDir(par5EntityLivingBase) & 1) != 0 ? 0 : 1);
        par1World.setBlockMetadataWithNotify(par2, par3, par4, meta, 3);
    }

    @Override
    public int damageDropped(int par1) {
        return par1 & 0x0E;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        switch (par2) {
        case 0:
            return BambooInit.dSquare.getIcon(par1, 0);

        case 1:
            return BambooInit.dSquare.getIcon(par1, 1);

        case 2:
            return BambooInit.dSquare.getIcon(par1, 4);

        case 3:
            return BambooInit.dSquare.getIcon(par1, 5);

        case 4:
            return BambooInit.dSquare.getIcon(par1, 8);

        case 5:
            return BambooInit.dSquare.getIcon(par1, 9);

        case 6:
            return BambooInit.dSquare.getIcon(par1, 10);

        case 7:
            return BambooInit.dSquare.getIcon(par1, 11);

        case 8:
            return BambooInit.dSquare.getIcon(par1, 12);

        case 9:
            return BambooInit.dSquare.getIcon(par1, 13);

        default:
            return BambooInit.dSquare.getIcon(par1, 0);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < 10; i += 2) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
}
