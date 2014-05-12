package ruby.bamboo.item.enchant;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;

public abstract class EnchantBase {

    final int id;
    final String name;
    final int maxLevel;
    final float weight;
    final float tp;
    static final Random rand = new Random();

    //ゆにーくID,最大エンチャントレベル,エンチャント付与率,エンチャント基礎発動率
    public EnchantBase(int id, String name, int maxLevel, float weight, float tp) {
        this.id = id;
        this.name = name;
        this.maxLevel = maxLevel;
        this.weight = weight;
        this.tp = tp;
        if (!BambooEnchantment.idToEnchantMap.containsKey(id)) {
            BambooEnchantment.idToEnchantMap.put(id, this);
        } else {
            FMLLog.warning("[BambooMod] EnchantId Collision:" + id);
        }

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public float getWeight() {
        return weight;
    }

    public float getTriggerPercent(int enchantLvl) {
        return tp;
    }

    public void activate(ItemStack itemStack, World world, int posX, int posY, int posZ, EntityLivingBase entity, int enchantLvl) {
        if (rand.nextFloat() < getTriggerPercent(enchantLvl)) {
            this.effect(itemStack, world, posX, posY, posZ, entity, enchantLvl);
        }
    }

    abstract void effect(ItemStack itemStack, World world, int posX, int posY, int posZ, EntityLivingBase entity, int enchantLvl);
}
