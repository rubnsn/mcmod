package ruby.bamboo.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import ruby.bamboo.BambooInit;

import net.minecraft.block.Block;
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
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class KatanaDropManager {
    private static final Random rand;
    private static final HashMap<Class<? extends EntityLivingBase>, List<KatanaDropItem>> dropTable;
    static {
        rand = new Random(System.currentTimeMillis());
        dropTable = new HashMap<Class<? extends EntityLivingBase>, List<KatanaDropItem>>();
        // Zombie
        addDrop(EntityZombie.class, new KatanaDropItem(new ItemStack(Item.leather), 0.2F).setRandomAddAmount(3));
        addDrop(EntityZombie.class, new KatanaDropItem(new ItemStack(Item.bone), 0.2F).setRandomAddAmount(3));
        addDrop(EntityZombie.class, new KatanaDropItem(new ItemStack(Item.skull.itemID, 1, 2), 0.002F));
        // Skeleton
        addDrop(EntitySkeleton.class, new KatanaDropItem(new ItemStack(Item.skull.itemID, 1, 0), 0.002F));
        addDrop(EntitySkeleton.class, new KatanaDropItem(new ItemStack(Item.skull.itemID, 1, 1), 0.001F));
        // Creeper
        addDrop(EntityCreeper.class, new KatanaDropItem(new ItemStack(Item.gunpowder), 0.2F).setRandomAddAmount(3));
        addDrop(EntityCreeper.class, new KatanaDropItem(new ItemStack(Block.tnt), 0.01F).setRandomAddAmount(2));
        addDrop(EntityCreeper.class, new KatanaDropItem(new ItemStack(BambooInit.firecrackerIID, 1, 0), 0.5F).setRandomAddAmount(3));
        addDrop(EntityCreeper.class, new KatanaDropItem(new ItemStack(Item.skull.itemID, 1, 4), 0.002F));
        // Spider
        addDrop(EntitySpider.class, new KatanaDropItem(new ItemStack(Block.web), 0.1F));
        // Ghast
        addDrop(EntitySpider.class, new KatanaDropItem(new ItemStack(Item.fireballCharge), 0.5F).setRandomAddAmount(3));
        addDrop(EntitySpider.class, new KatanaDropItem(new ItemStack(Block.tnt), 0.5F).setRandomAddAmount(4));
        addDrop(EntitySpider.class, new KatanaDropItem(new ItemStack(Item.glowstone), 0.5F).setRandomAddAmount(8));
        // Ender
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(Block.plantRed), 0.5F).setRandomAddAmount(4));
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(Block.plantYellow), 0.5F).setRandomAddAmount(4));
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(Block.mushroomBrown), 0.5F).setRandomAddAmount(4));
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(Block.mushroomRed), 0.5F).setRandomAddAmount(4));
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(Item.eyeOfEnder), 0.05F).setRandomAddAmount(3));
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(BambooInit.bambooIID, 1, 0), 0.05F).setRandomAddAmount(3));
        addDrop(EntityEnderman.class, new KatanaDropItem(new ItemStack(BambooInit.sakuraBID, 1, 0), 0.05F).setRandomAddAmount(3));
        // Silverfish
        addDrop(EntitySilverfish.class, new KatanaDropItem(new ItemStack(Item.paper), 0.5F).setRandomAddAmount(4));
        // Blaze
        addDrop(EntityBlaze.class, new KatanaDropItem(new ItemStack(Item.fireballCharge), 0.1F).setRandomAddAmount(3));
        // Pig
        addDrop(EntityPig.class, new KatanaDropItem(new ItemStack(Item.leather), 0.5F).setRandomAddAmount(3));
        // Sheep
        addDrop(EntitySheep.class, new KatanaDropItem(new ItemStack(Item.silk), 0.5F).setRandomAddAmount(4));
        // Bat
        addDrop(EntityBat.class, new KatanaDropItem(new ItemStack(Item.appleRed), 0.5F).setRandomAddAmount(2));
        addDrop(EntityBat.class, new KatanaDropItem(new ItemStack(Item.appleGold), 0.01F));
        // Witch
        addDrop(EntityWitch.class, new KatanaDropItem(new ItemStack(Block.waterlily), 0.5F).setRandomAddAmount(4));
        addDrop(EntityWitch.class, new KatanaDropItem(new ItemStack(Item.glassBottle), 0.5F).setRandomAddAmount(4));
        // Chicken
        addDrop(EntityChicken.class, new KatanaDropItem(new ItemStack(Item.egg), 0.25F).setRandomAddAmount(2));
        // Wolf
        addDrop(EntityWolf.class, new KatanaDropItem(new ItemStack(Item.bone), 0.5F).setRandomAddAmount(2));
        addDrop(EntityWolf.class, new KatanaDropItem(new ItemStack(Item.beefRaw), 0.5F).setRandomAddAmount(4));
        addDrop(EntityWolf.class, new KatanaDropItem(new ItemStack(Item.porkRaw), 0.5F).setRandomAddAmount(3));
        addDrop(EntityWolf.class, new KatanaDropItem(new ItemStack(Item.chickenRaw), 0.5F));
        addDrop(EntityWolf.class, new KatanaDropItem(new ItemStack(Item.feather), 0.5F).setRandomAddAmount(4));
        // Ocelot
        addDrop(EntityOcelot.class, new KatanaDropItem(new ItemStack(Item.fishRaw), 0.75F).setRandomAddAmount(2));

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
