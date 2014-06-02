package ruby.bamboo.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBambooshoot extends ItemBlock {
    private final Block block;

    public ItemBambooshoot(Block block) {
        super(block);
        this.block = block;
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        Block block = par3World.getBlock(par4, par5, par6);

        if (block == Blocks.snow) {
            par5 -= 1;
        }

        if (!par3World.isAirBlock(par4, par5 + 1, par6)) {
            return false;
        }

        block = par3World.getBlock(par4, par5, par6);

        if (block != Blocks.grass && block != Blocks.dirt) {
            return false;
        }

        if (par1ItemStack.stackSize == 0) {
            return false;
        }

        if (par3World.setBlock(par4, par5 + 1, par6, this.block, 15, 3)) {
            if (par3World.getBlock(par4, par5, par6) == this.block) {
                this.block.onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer, par1ItemStack);
            }
            par1ItemStack.stackSize--;
        }
        return true;
    }

}
