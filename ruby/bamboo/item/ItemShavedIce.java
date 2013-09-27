package ruby.bamboo.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ruby.bamboo.BambooCore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;

public class ItemShavedIce extends ItemFood
{
    private Icon tableware;
    private Icon syrup;
    private Icon milk;
    private int heal;
    private static final int MAX_ELEMENT_COUNT;
    private static Map<Integer, EnumShavedIce> iceMap;
    static
    {
        EnumShavedIce[] esis = EnumShavedIce.values();
        iceMap = new HashMap<Integer, EnumShavedIce>();

        for (EnumShavedIce esi: esis)
        {
            iceMap.put(esi.id, esi);
        }

        MAX_ELEMENT_COUNT = esis.length;
    }
    public ItemShavedIce(int par1)
    {
        super(par1, 4, false);
        setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(64);
        this.setCreativeTab(BambooCore.tabBamboo);
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        heal = iceMap.get(par1ItemStack.getItemDamage() % MAX_ELEMENT_COUNT).rarity;

        if (par1ItemStack.getItemDamage() >= MAX_ELEMENT_COUNT)
        {
            heal += 3;
        }

        return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
    }
    @Override
    public int getHealAmount()
    {
        int random = itemRand.nextInt(6) - 4 + heal;
        return super.getHealAmount() + random;
    }
    @Override
    public int getColorFromItemStack(ItemStack itemStack, int par2)
    {
        int dmg = itemStack.getItemDamage();

        if (par2 == 2 && dmg > MAX_ELEMENT_COUNT)
        {
            return 0xffffff;
        }

        return par2 != 0 ? iceMap.get(dmg % MAX_ELEMENT_COUNT).color : 0xffffff;
    }

    @Override
    public Icon getIconFromDamageForRenderPass(int dmg, int par2)
    {
        if (par2 == 2)
        {
            if (dmg < MAX_ELEMENT_COUNT)
            {
                return dmg % MAX_ELEMENT_COUNT != 0 ? syrup : tableware;
            }

            return milk;
        }
        else if (par2 == 1)
        {
            return dmg % MAX_ELEMENT_COUNT != 0 ? syrup : tableware;
        }
        else
        {
            return tableware;
        }
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
    @Override
    public int getRenderPasses(int metadata)
    {
        if (metadata % MAX_ELEMENT_COUNT != 0)
        {
            return 3;
        }
        else
        {
            return 2;
        }
    }
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return this.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < MAX_ELEMENT_COUNT * 2; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.tableware = par1IconRegister.registerIcon("iceglass");
        this.syrup = par1IconRegister.registerIcon("syrup");
        this.milk = par1IconRegister.registerIcon("condensedmilk");
    }
}
