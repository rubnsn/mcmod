package ruby.bamboo.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBambooFood extends ItemFood {
    private static Map<Integer, EnumFood> foodMap;
    private int heal;
    private static final int MAX_ELEMENT_COUNT;
    static {
        foodMap = new HashMap<Integer, EnumFood>();

        for (EnumFood ef : EnumFood.values()) {
            foodMap.put(ef.getId(), ef);
        }

        MAX_ELEMENT_COUNT = foodMap.size();
    }

    public ItemBambooFood() {
        super(0, false);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int func_150905_g(ItemStack itemstack) {
        return heal;
    }

    @Override
    public IIcon getIconFromDamage(int par1) {
        return foodMap.get(par1 % MAX_ELEMENT_COUNT).getTex();
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return foodMap.get(par1ItemStack.getItemDamage() % MAX_ELEMENT_COUNT).getDuration();
    }

    @Override
    public float func_150906_h(ItemStack itemstack) {
        return 1;
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < MAX_ELEMENT_COUNT; i++) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (!par2World.isRemote) {
            heal = foodMap.get(par1ItemStack.getItemDamage() % MAX_ELEMENT_COUNT).getHeal();
            int dmg = par1ItemStack.getItemDamage();
        }

        return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
    }

    private void returnItem(EntityPlayer entity, ItemStack is) {
        if (!entity.inventory.addItemStackToInventory(is)) {
            entity.entityDropItem(is, 0.5F);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
        for (EnumFood ef : EnumFood.values()) {
            ef.setTex(par1IconRegister.registerIcon(BambooCore.resourceDomain + ef.name().toLowerCase()));
        }
    }
}
