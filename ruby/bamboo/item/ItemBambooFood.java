package ruby.bamboo.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ruby.bamboo.BambooInit;
import ruby.bamboo.Config;
import ruby.bamboo.BambooCore;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemBambooFood extends ItemFood
{
    private static Map<Integer, EnumFood> foodMap;
    private int heal;
    private static final int MAX_ELEMENT_COUNT;
    static
    {
        foodMap = new HashMap<Integer, EnumFood>();

        for (EnumFood ef: EnumFood.values())
        {
            foodMap.put(ef.getId(), ef);
        }

        MAX_ELEMENT_COUNT = foodMap.size();
    }
    public ItemBambooFood(int id)
    {
        super(id, 0, false);
        setMaxDamage(0);
        setHasSubtypes(true);
        this.setCreativeTab(BambooCore.tabBamboo);
    }

    @Override
    public int getHealAmount()
    {
        return heal;
    }
    @Override
    public Icon getIconFromDamage(int par1)
    {
        return foodMap.get(par1 % MAX_ELEMENT_COUNT).getTex();
    }
    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return foodMap.get(par1ItemStack.getItemDamage() % MAX_ELEMENT_COUNT).getDuration();
    }
    @Override
    public float getSaturationModifier()
    {
        return 1;
    }
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < MAX_ELEMENT_COUNT; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par2World.isRemote)
        {
            heal = foodMap.get(par1ItemStack.getItemDamage() % MAX_ELEMENT_COUNT).getHeal();
            int dmg = par1ItemStack.getItemDamage();

            if (BambooCore.getConf().returnBowl)
            {
                if (EnumFood.GYUKUSI.getId() == dmg || EnumFood.BUTAKUSI.getId() == dmg || EnumFood.TORIKUSI.getId() == dmg)
                {
                    returnItem(par3EntityPlayer, new ItemStack(Item.stick));
                }
                else
                {
                    returnItem(par3EntityPlayer, new ItemStack(BambooInit.bambooBasketIID, 1, 0));
                }
            }
        }

        return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
    }
    private void returnItem(EntityPlayer entity, ItemStack is)
    {
        if (!entity.inventory.addItemStackToInventory(is))
        {
            entity.dropPlayerItem(is);
        }
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (EnumFood ef: EnumFood.values())
        {
            ef.setTex(par1IconRegister.registerIcon(ef.name().toLowerCase()));
        }
    }
}
