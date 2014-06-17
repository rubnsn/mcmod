package ruby.bamboo.item.enchant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EnchantFire extends EnchantBase {

    public EnchantFire(int id, String name, int maxLevel, float weight, float tp) {
        super(id, name, maxLevel, weight, tp);
    }

    @Override
    void effect(ItemStack itemStack, World world, int posX, int posY, int posZ, EntityLivingBase entity, int enchantLvl) {

    }

    @Override
    void onUpdate(ItemStack itemStack, World world, Entity entity) {
        if (entity.isBurning()) {
            entity.extinguish();
        }
    }

}
