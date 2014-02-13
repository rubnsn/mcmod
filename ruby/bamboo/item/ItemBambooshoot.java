package ruby.bamboo.item;

import ruby.bamboo.BambooCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBambooshoot extends ItemReed {
    private final int spawnID;

    public ItemBambooshoot(int i, int spawnID) {
        super(i, Block.blocksList[spawnID]);
        this.spawnID = spawnID;
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        int blockid = par3World.getBlockId(par4, par5, par6);

        if (blockid == Block.snow.blockID) {
            par5 -= 1;
        }

        if (!par3World.isAirBlock(par4, par5 + 1, par6)) {
            return false;
        }

        blockid = par3World.getBlockId(par4, par5, par6);

        if (blockid != Block.grass.blockID && blockid != Block.dirt.blockID) {
            return false;
        }

        if (par1ItemStack.stackSize == 0) {
            return false;
        }

        Block block = Block.blocksList[spawnID];

        if (par3World.setBlock(par4, par5 + 1, par6, spawnID, 15, 3)) {
            if (par3World.getBlockId(par4, par5, par6) == spawnID) {
                Block.blocksList[spawnID].onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer, par1ItemStack);
            }
            par1ItemStack.stackSize--;
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(BambooCore.resourceDomain + "bambooshoot");
    }
}
