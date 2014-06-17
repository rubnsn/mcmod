package ruby.bamboo.item.enchant;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class BambooEnchantment {
    public static HashMap<Short, EnchantBase> idToEnchantMap = new HashMap<Short, EnchantBase>();
    public static final EnchantBase blood = new EnchantBlood(0, "blood", 3, 0.1F, 0.05F);
    public static final EnchantBase teleport = new EnchantTeleport(1, "teleport", 3, 0.1F, 0.05F);
    public static final EnchantBase lucky = new EnchantLucky(2, "lucky", 5, 0.01F, 0.01F);
    public static final EnchantBase power = new EnchantPower(3, "power", 3, 0.1F, 0.1F);
    public static final EnchantBase accele = new EnchantAccele(4, "accele", 3, 0.1F, 0.5F);
    public static final EnchantBase summons = new EnchantSummons(5, "summons", 2, 0.1F, 0.01F);
    public static final EnchantBase breaker = new EnchantBreaker(6, "breaker", 2, 0.05F, 0.1F);
    public static final EnchantBase burst = new EnchantBurst(7, "burst", 3, 0.05F, 0.1F);
    public static final EnchantBase chain = new EnchantChain(8, "chain", 1, 0.25F, 1F);
    public static final EnchantBase eat = new EnchantEat(9, "eat", 1, 0.5F, 1F);
    public static final EnchantBase fire = new EnchantFire(10, "fire", 1, 0.01F, 1F);

    public static void activate(ItemStack itemStack, World world, EntityLivingBase entity, int posX, int posY, int posZ) {
        if (itemStack.stackTagCompound != null) {
            NBTTagList nbttaglist = itemStack.stackTagCompound.getTagList("spench", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                idToEnchantMap.get(nbttaglist.getCompoundTagAt(i).getShort("id")).activate(itemStack, world, posX, posY, posZ, entity, nbttaglist.getCompoundTagAt(i).getShort("lvl"));
            }
        }
    }

    public static void onUpdate(ItemStack itemStack, World world, Entity entity) {
        if (itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey("spench")) {
            NBTTagList nbttaglist = itemStack.stackTagCompound.getTagList("spench", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                idToEnchantMap.get(nbttaglist.getCompoundTagAt(i).getShort("id")).onUpdate(itemStack, world, entity);
            }
        }
    }
}
