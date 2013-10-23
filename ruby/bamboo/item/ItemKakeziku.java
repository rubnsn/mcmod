package ruby.bamboo.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.BambooCore;
import ruby.bamboo.entity.EntityKakeziku;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemKakeziku extends Item {
    public ItemKakeziku(int i) {
        super(i);
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (par7 == 0) {
            return false;
        } else if (par7 == 1) {
            return false;
        } else {
            byte var11 = 0;

            if (par7 == 4) {
                var11 = 1;
            }

            if (par7 == 3) {
                var11 = 2;
            }

            if (par7 == 5) {
                var11 = 3;
            }

            if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
                return false;
            } else {
                EntityKakeziku var12 = new EntityKakeziku(par3World, par4, par5, par6, var11);

                if (var12.onValidSurface()) {
                    if (!par3World.isRemote) {
                        par3World.spawnEntityInWorld(var12);
                    }/*
                      * else{
                      * 
                      * var12.getDataWatcher().updateObject(17,var11); }
                      */
                    --par1ItemStack.stackSize;
                }

                return true;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(BambooCore.resorceDmain + "kakeziku");
    }
}
