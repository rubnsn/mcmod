package ruby.bamboo.item;

import ruby.bamboo.BambooCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBambooshoot extends Item {
    private final int spawnID;

    public ItemBambooshoot(int i, int spawnID) {
        super(i);
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

        /*
         * if (par3World.canPlaceEntityOnSide(spawnID, par4,par5+1,par6, false,
         * par7,par2EntityPlayer)) {
         */
        Block block = Block.blocksList[spawnID];

        if (par3World.setBlock(par4, par5 + 1, par6, spawnID, 15, 3)) {
            if (par3World.getBlockId(par4, par5, par6) == spawnID) {
                // Block.blocksList[spawnID].onBlockPlaced(par3World,
                // par4,par5,par6, par7);
                Block.blocksList[spawnID].onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer, par1ItemStack);
            }

            // world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k
            // + 0.5F, block.stepSound.stepSoundDir2(),
            // (block.stepSound.getVolume() + 1.0F) / 2.0F,
            // block.stepSound.getPitch() * 0.8F);
            par1ItemStack.stackSize--;
        }

        /*
         * } else { return false; }
         */
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(BambooCore.resourceDomain + "bambooshoot");
    }
}
