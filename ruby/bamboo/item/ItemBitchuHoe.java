package ruby.bamboo.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBitchuHoe extends Item {
    public ItemBitchuHoe(int par1) {
        super(par1);
        setMaxDamage(EnumToolMaterial.IRON.getMaxUses());
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (!par3World.isRemote) {
            if (par3World.getBlockId(par4, par5, par6) == Block.dirt.blockID || par3World.getBlockId(par4, par5, par6) == Block.grass.blockID) {
                if (par2EntityPlayer != null && !par2EntityPlayer.capabilities.isCreativeMode) {
                    par1ItemStack.damageItem(1, par2EntityPlayer);
                }

                par3World.setBlock(par4, par5, par6, BambooInit.riceFieldBID);
                return true;
            }
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(BambooCore.resourceDomain + "bitchuhoe");
    }
}
