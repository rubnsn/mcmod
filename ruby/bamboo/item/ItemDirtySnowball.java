package ruby.bamboo.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ruby.bamboo.BambooCore;
import ruby.bamboo.entity.EntityDirtySnowball;
import ruby.bamboo.entity.EnumDirtySnowball;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;

public class ItemDirtySnowball extends ItemSnowball
{
    private static Map<Integer, EnumDirtySnowball> dmgMap;
    private static final int MAX_ELEMENT_COUNT;
    static
    {
        dmgMap = new HashMap<Integer, EnumDirtySnowball>();

        for (EnumDirtySnowball eds: EnumDirtySnowball.values())
        {
            dmgMap.put(eds.getId(), eds);
        }

        MAX_ELEMENT_COUNT = dmgMap.size();
    }
    public ItemDirtySnowball(int par1)
    {
        super(par1);
        this.maxStackSize = 64;
        setHasSubtypes(true);
        setMaxDamage(0);
        this.setCreativeTab(BambooCore.tabBamboo);
    }
    public static EnumDirtySnowball getEDS(int id)
    {
        return dmgMap.get(id);
    }
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par2World.isRemote)
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            --par1ItemStack.stackSize;
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        par2World.spawnEntityInWorld(new EntityDirtySnowball(par2World, par3EntityPlayer, dmgMap.get(par1ItemStack.getItemDamage())));
        return par1ItemStack;
    }
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return this.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < MAX_ELEMENT_COUNT; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }/*
    @Override
    public Icon getIconFromDamage(int par1)
    {
    	return Item.snowball.getIconFromDamage(0);
    }*/
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("snowball");
    }
}
