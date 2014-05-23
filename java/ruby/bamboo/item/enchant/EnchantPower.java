package ruby.bamboo.item.enchant;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EnchantPower extends EnchantBase {

    public EnchantPower(int id, String name, int maxLevel, float weight, float tp) {
        super(id, name, maxLevel, weight, tp);
    }

    @Override
    public float getTriggerPercent(int enchantLvl) {
        return enchantLvl * tp;
    }

    @Override
    void effect(ItemStack itemStack, World world, int posX, int posY, int posZ, EntityLivingBase entity, int enchantLvl) {
        entity.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 50, 1));
    }

}
