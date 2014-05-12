package ruby.bamboo.item.enchant;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EnchantBlood extends EnchantBase {

    public EnchantBlood(int id, String name, int maxLevel, float weight, float tp) {
        super(id, name, maxLevel, weight, tp);
    }

    @Override
    public float getTriggerPercent(int enchantLevel) {
        return tp * enchantLevel;
    }

    @Override
    public void effect(ItemStack itemStack, World world, int posX, int posY, int posZ, EntityLivingBase entity, int enchantLvl) {
        entity.attackEntityFrom(DamageSource.magic, rand.nextInt(enchantLvl * 3));
    }

}
