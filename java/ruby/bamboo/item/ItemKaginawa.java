package ruby.bamboo.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.entity.EntityKaginawa;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemKaginawa extends Item {

    public ItemKaginawa() {
        super();
        setMaxDamage(0);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        EntityKaginawa et = new EntityKaginawa(par2World, par3EntityPlayer);
        if (BambooCore.proxy.kagimap.containsKey(par3EntityPlayer)) {
            if (BambooCore.proxy.kagimap.get(par3EntityPlayer).isDead) {
                BambooCore.proxy.kagimap.put(par3EntityPlayer, et);
                if (!par2World.isRemote) {
                    par2World.spawnEntityInWorld(et);
                } else {
                    par2World.joinEntityInSurroundings(et);
                }
            }
        } else {
            BambooCore.proxy.kagimap.put(par3EntityPlayer, et);
            if (!par2World.isRemote) {
                par2World.spawnEntityInWorld(et);
            } else {
                par2World.joinEntityInSurroundings(et);
            }
        }
        return par1ItemStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = Items.string.getIconFromDamage(0);
    }

}
