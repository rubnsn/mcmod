package ruby.bamboo.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import ruby.bamboo.BambooCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemNinjaArmor extends ItemArmor implements ISpecialArmor {
    private final static ArmorProperties prop = new ArmorProperties(0, 1, 1);
    public static final byte HELM = 4;
    public static final byte CHEST = 3;
    public static final byte LEG = 2;
    public static final byte BOOTS = 1;

    public ItemNinjaArmor(int slot) {
        super(ItemArmor.ArmorMaterial.IRON, 0, slot);
        this.setNoRepair();
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return BambooCore.resourceDomain + "textures/armor/ninja_layer_" + (slot < 2 ? 1 : 2) + ".png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String getIconString() {
        return BambooCore.resourceDomain + this.iconString;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return prop;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        stack.setItemDamage(stack.getItemDamage() + damage);
    }

    public boolean isEqNinjaArmor(EntityPlayer player, byte eqPos) {
        return player.getEquipmentInSlot(eqPos) != null && player.getEquipmentInSlot(eqPos).getItem() instanceof ItemNinjaArmor;
    }
}
