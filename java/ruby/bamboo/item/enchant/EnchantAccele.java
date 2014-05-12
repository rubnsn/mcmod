package ruby.bamboo.item.enchant;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EnchantAccele extends EnchantBase {

    public EnchantAccele(int id, String name, int maxLevel, float weight, float tp) {
        super(id, name, maxLevel, weight, tp);
    }

    @Override
    void effect(ItemStack itemStack, World world, int posX, int posY, int posZ, EntityLivingBase entity, int enchantLvl) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (player.getFoodStats().getFoodLevel() > 0) {
                player.getFoodStats().addExhaustion(enchantLvl * 10F);
            }
        }
    }

}
