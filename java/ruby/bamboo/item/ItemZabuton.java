package ruby.bamboo.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.entity.EntityThrowZabuton;
import ruby.bamboo.entity.EntityZabuton;
import ruby.bamboo.entity.EntityZabuton.EnumZabutonColor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemZabuton extends Item {

    public ItemZabuton() {
        super();
        this.setHasSubtypes(true);
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (!par3World.isRemote) {
            if (!par2EntityPlayer.isSneaking()) {
                Entity entity = new EntityZabuton(par3World, par1ItemStack.getItemDamage());
                entity.setPositionAndRotation(par4 + par8, par5 + par9, par6 + par10, par2EntityPlayer.rotationYaw, 180);
                par3World.spawnEntityInWorld(entity);
            }
        }
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (!par2World.isRemote) {
            if (par3EntityPlayer.isSneaking()) {
                Entity entity = new EntityThrowZabuton(par2World, par3EntityPlayer, par1ItemStack.getItemDamage());
                par2World.spawnEntityInWorld(entity);
            }
        }
        return par1ItemStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < 16; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        return EnumZabutonColor.getColor(par1ItemStack.getItemDamage()).getColor();
    }
}
