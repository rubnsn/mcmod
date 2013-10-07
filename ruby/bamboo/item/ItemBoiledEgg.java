package ruby.bamboo.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBoiledEgg extends ItemFood
{
    private int[] potions = new int[] {0, 8289, 8297, 0, 8195, 0, 0, 0, 8290, 0, 0, 0, 0, 0, 0, 8292};

    public ItemBoiledEgg(int par1)
    {
        super(par1, 4, false);
        setHasSubtypes(true);
        setMaxStackSize(16);
        setMaxDamage(0);
        this.setCreativeTab(null);
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
    }
    @Override
    public int getColorFromItemStack(ItemStack itemStack, int par2)
    {
        return 0xffcccc;
    }/*
	@Override
    public Icon getIconFromDamage(int par1)
    {
        return Item.egg.getIconFromDamage(0);
    }*/
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("egg");
    }
}
