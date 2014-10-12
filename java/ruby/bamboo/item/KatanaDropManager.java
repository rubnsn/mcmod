package ruby.bamboo.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import ruby.bamboo.BambooInit;

public class KatanaDropManager {
    private static final Random rand;
    private static final HashMap<Class<? extends EntityLivingBase>, List<KatanaDropItem>> dropTable;
    static {
        rand = new Random(System.currentTimeMillis());
        dropTable = new HashMap<Class<? extends EntityLivingBase>, List<KatanaDropItem>>();
        // Zombie
        addDrop(EntityZombie.class, new KatanaDropItem(new ItemStack(Items.leather), 0.2F).setRandomAddAmount(3));
        addDrop(EntityZombie.class, new KatanaDropItem(new ItemStack(Items.bone), 0.2F).setRandomAddAmount(3));
        addDrop(EntityZombie.class, new KatanaDropItem(new ItemStack(Items.skull, 1, 2), 0.002F));
        // Skeleton
        addDrop(EntitySkeleton.class, new KatanaDropItem(new ItemStack(Items.skull, 1, 0), 0.002F));
        addDrop(EntitySkeleton.class, new KatanaDropItem(new ItemStack(Items.skull, 1, 1), 0.001F));
        // Creeper
        addDrop(EntityCreeper.class, new KatanaDropItem(new ItemStack(Items.gunpowder), 0.2F).setRandomAddAmount(3));
        addDrop(EntityCreeper.class, new KatanaDropItem(new ItemStack(Blocks.tnt), 0.01F).setRandomAddAmount(2));
        addDrop(EntityCreeper.class, new KatanaDropItem(new ItemStack(BambooInit.firecracker, 1, 0), 0.5F).setRandomAddAmount(3));
        addDrop(EntityCreeper.class, new KatanaDropItem(new ItemStack(Items.skull, 1, 4), 0.002F));
        // Spider
        addDrop(EntitySpider.class, new KatanaDropItem(new ItemStack(Blocks.web), 0.1F));
        // Ghast
        addDrop(EntitySpider.class, new KatanaDropItem(new ItemStack(Items.fire_charge), 0.5F).setRandomAddAmount(3));
        addDrop(EntitySpider.class, new KatanaDropItem(new ItemStack(Blocks.tnt), 0.5F).setRandomAddAmount(4));
        addDrop(EntitySpider.class, new KatanaDropItem(new ItemStack(Items.glowstone_dust), 0.5F).setRandomAddAmount(8));
        // Ender
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(Blocks.red_flower), 0.5F).setRandomAddAmount(4));
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(Blocks.yellow_flower), 0.5F).setRandomAddAmount(4));
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(Blocks.brown_mushroom), 0.5F).setRandomAddAmount(4));
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(Blocks.red_mushroom_block), 0.5F).setRandomAddAmount(4));
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(Items.ender_eye), 0.05F).setRandomAddAmount(3));
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(BambooInit.bamboo, 1, 0), 0.05F).setRandomAddAmount(3));
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(BambooInit.sakura, 1, 0), 0.05F).setRandomAddAmount(3));
        // Silverfish
        addDrop(EntitySilverfish.class, new KatanaDropItem(new ItemStack(Items.paper), 0.5F).setRandomAddAmount(4));
        // Blaze
        addDrop(EntityBlaze.class, new KatanaDropItem(new ItemStack(Items.fire_charge), 0.1F).setRandomAddAmount(3));
        // Pig
        addDrop(EntityPig.class, new KatanaDropItem(new ItemStack(Items.leather), 0.5F).setRandomAddAmount(3));
        // Sheep
        addDrop(EntitySheep.class, new KatanaDropItem(new ItemStack(Items.string), 0.5F).setRandomAddAmount(4));
        // Bat
        addDrop(EntityBat.class, new KatanaDropItem(new ItemStack(Items.apple), 0.5F).setRandomAddAmount(2));
        addDrop(EntityBat.class, new KatanaDropItem(new ItemStack(Items.golden_apple), 0.01F));
        // Villager
        addDrop(EntityVillager.class, new KatanaDropItem(new ItemStack(BambooInit.villagerBlock), 0.002F));
        // Witch
        addDrop(EntityWitch.class, new KatanaDropItem(new ItemStack(Blocks.waterlily), 0.5F).setRandomAddAmount(4));
        addDrop(EntityWitch.class, new KatanaDropItem(new ItemStack(Items.glass_bottle), 0.5F).setRandomAddAmount(4));
        // Chicken
        addDrop(EntityChicken.class, new KatanaDropItem(new ItemStack(Items.egg), 0.25F).setRandomAddAmount(2));
        // Wolf
        addDrop(EntityWolf.class, new KatanaDropItem(new ItemStack(Items.bone), 0.5F).setRandomAddAmount(2));
        addDrop(EntityWolf.class, new KatanaDropItem(new ItemStack(Items.beef), 0.5F).setRandomAddAmount(4));
        addDrop(EntityWolf.class, new KatanaDropItem(new ItemStack(Items.porkchop), 0.5F).setRandomAddAmount(3));
        addDrop(EntityWolf.class, new KatanaDropItem(new ItemStack(Items.chicken), 0.5F));
        addDrop(EntityWolf.class, new KatanaDropItem(new ItemStack(Items.feather), 0.5F).setRandomAddAmount(4));
        // Ocelot
        addDrop(EntityOcelot.class, new KatanaDropItem(new ItemStack(Items.fish), 0.75F).setRandomAddAmount(2));

    }

    public static void addDrop(Class<? extends EntityLivingBase> entityCls, KatanaDropItem item) {
        if (dropTable.containsKey(entityCls)) {
            dropTable.get(entityCls).add(item);
        } else {
            List list = new ArrayList<KatanaDropItem>();
            list.add(item);
            dropTable.put(entityCls, list);
        }
    }

    public static ItemStack getRandomDrop(Class<? extends EntityLivingBase> entityCls) {
        if (dropTable.containsKey(entityCls)) {
            List<KatanaDropItem> list = dropTable.get(entityCls);
            KatanaDropItem drop = list.get(rand.nextInt(list.size()));
            if (rand.nextFloat() < drop.getReality()) {
                ItemStack result = drop.getDropItem().copy();
                if (drop.getRandomAmount() != 0) {
                    result.stackSize += rand.nextInt(drop.getRandomAmount() + 1);
                }
                return result;
            }
        }
        return null;
    }
}
