package ruby.bamboo;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import ruby.bamboo.item.ItemBambooPickaxe;
import ruby.bamboo.item.ItemSack;
import ruby.bamboo.item.enchant.BambooEnchantment;
import ruby.bamboo.item.enchant.EnchantBase;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class CraftingHandler {
    private static final String[] nameTable = { "hero", "beast", "lily", "old", "shine", "admired", "nomal", "cool", "subtle", "toy", "alive", "hope" };
    private static final String[] tyoeTable = { "pickaxe", "shadow", "mass", "breaker", "cracker", "stinger", "drop", "oath", "precept", "sin" };

    private enum EnumEnchant {
        strange(BambooEnchantment.eat, 1),
        bad(BambooEnchantment.accele, 1),
        call(BambooEnchantment.summons, 1),
        crude(BambooEnchantment.accele, 1, Enchantment.unbreaking, 1),
        aging(Enchantment.unbreaking, 2),
        evil(Enchantment.efficiency, 1, BambooEnchantment.blood, 1),
        bloody(BambooEnchantment.blood, 2),
        chaos(BambooEnchantment.summons, 2, BambooEnchantment.teleport, 1),
        terrible(BambooEnchantment.blood, 2, BambooEnchantment.burst, 2),
        greedy(BambooEnchantment.accele, 3, BambooEnchantment.lucky, 1),
        cursed(Enchantment.efficiency, 3, BambooEnchantment.teleport, 2, BambooEnchantment.blood, 1),
        fallingdown(BambooEnchantment.accele, 3, BambooEnchantment.teleport, 3, BambooEnchantment.blood, 3),
        good(Enchantment.efficiency, 1),
        powerful(BambooEnchantment.breaker, 1),
        burst(BambooEnchantment.burst, 1),
        uncommon(Enchantment.efficiency, 1, Enchantment.fortune, 1),
        cut(Enchantment.silkTouch, 1),
        light(Enchantment.efficiency, 2),
        rare(Enchantment.efficiency, 2, Enchantment.unbreaking, 1),
        epic(Enchantment.efficiency, 2, BambooEnchantment.breaker, 1),
        fortune(Enchantment.fortune, 2, BambooEnchantment.lucky, 1),
        chain(BambooEnchantment.chain, 1),
        legendary(Enchantment.efficiency, 3, Enchantment.unbreaking, 2, BambooEnchantment.chain, 1),
        mythology(Enchantment.efficiency, 4, Enchantment.unbreaking, 2, BambooEnchantment.lucky, 2, BambooEnchantment.chain, 1);
        EnumEnchant(Object... objects) {
            this.nbt = new NBTTagCompound();
            nbt.setTag("ench", new NBTTagList());
            nbt.setTag("spench", new NBTTagList());
            NBTTagList enchList = nbt.getTagList("ench", 10);
            NBTTagList spenchList = nbt.getTagList("spench", 10);
            Iterator ite = Arrays.asList(objects).iterator();
            Object o;
            while (ite.hasNext()) {
                o = ite.next();
                if (o instanceof Enchantment) {
                    NBTTagCompound nbt = new NBTTagCompound();
                    nbt.setShort("id", (short) ((Enchantment) o).effectId);
                    nbt.setShort("lvl", ((Integer) ite.next()).shortValue());
                    enchList.appendTag(nbt);
                } else {
                    NBTTagCompound nbt = new NBTTagCompound();
                    nbt.setShort("id", (short) ((EnchantBase) o).getId());
                    nbt.setShort("lvl", ((Integer) ite.next()).shortValue());
                    spenchList.appendTag(nbt);
                }
            }
        }

        public static EnumEnchant[] badTable = { strange, bad, call, crude, aging, evil, bloody, chaos, terrible, greedy, cursed, fallingdown };
        public static EnumEnchant[] goodTable = { good, powerful, burst, uncommon, rare, cut, light, epic, fortune, chain, legendary, mythology };
        private NBTTagCompound nbt;
    }

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event) {
        if (event.player.worldObj.isRemote) {
            return;
        }

        if (event.crafting.getItem() instanceof ItemSack) {
            int lim = event.craftMatrix.getSizeInventory();

            while (lim-- > 0) {
                if (event.craftMatrix.getStackInSlot(lim) == null) {
                    continue;
                }

                Item target = event.craftMatrix.getStackInSlot(lim).getItem();

                if (target instanceof ItemSack) {
                    ((ItemSack) target).release(event.craftMatrix.getStackInSlot(lim), event.player.worldObj, event.player);
                }
            }
        } else if (event.crafting.getItem() instanceof ItemBambooPickaxe) {
            float reality = 0;
            Random rand = event.player.worldObj.rand;
            for (int i = 0; i < 10; i++) {
                reality += rand.nextGaussian();
            }
            NBTTagCompound nbt = new NBTTagCompound();
            if (reality < -1.5) {
                int length = EnumEnchant.badTable.length;
                reality -= length * rand.nextFloat();
                reality = Math.abs(Math.round(reality));
                if (length < reality) {
                    reality = length;
                }
                EnumEnchant ee = EnumEnchant.badTable[rand.nextInt((int) reality)];
                nbt = (NBTTagCompound) ee.nbt.copy();
                this.addArticle(nbt, ee);
            } else if (reality > 1.5) {
                int length = EnumEnchant.goodTable.length;
                reality += length * rand.nextFloat();
                reality = Math.abs(Math.round(reality));
                if (length < reality) {
                    reality = length;
                }
                EnumEnchant ee = EnumEnchant.goodTable[rand.nextInt((int) reality)];
                nbt = (NBTTagCompound) ee.nbt.copy();
                this.addArticle(nbt, ee);
            }
            this.addName(rand, nbt);
            this.addIcon(rand, nbt);
            event.crafting.stackTagCompound = nbt;

            spawnEntityItem(event.player, new ItemStack(Blocks.dragon_egg));
        }
    }

    private void addIcon(Random rand, NBTTagCompound nbt) {
        nbt.setByte("iconNum", (byte) rand.nextInt(7));
    }

    private void addName(Random rand, NBTTagCompound nbt) {
        String s = StatCollector.translateToLocal("bambooEnch." + nameTable[rand.nextInt(nameTable.length)] + ".name").trim();
        if (nbt.hasKey("display", 10)) {
            s = ((NBTTagCompound) nbt.getTag("display")).getString("Name") + s;
        }
        s += StatCollector.translateToLocal("bambooEnch." + tyoeTable[rand.nextInt(tyoeTable.length)] + ".name");
        NBTTagCompound nameNBT = new NBTTagCompound();
        nameNBT.setString("Name", s.trim());
        nbt.setTag("display", nameNBT);
    }

    private void addArticle(NBTTagCompound nbt, EnumEnchant ee) {
        NBTTagCompound nameNBT = new NBTTagCompound();
        nameNBT.setString("Name", StatCollector.translateToLocal("bambooEnch." + ee.name() + ".name").trim());
        nbt.setTag("display", nameNBT);
    }

    private void spawnEntityItem(Entity entity, ItemStack is) {
        entity.worldObj.spawnEntityInWorld(new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, is));
    }
}
