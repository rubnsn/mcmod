package ruby.bamboo.item;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import ruby.bamboo.item.enchant.BambooEnchantment;
import ruby.bamboo.item.enchant.EnchantBase;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemBambooPickaxe extends ItemPickaxe {
    public static final int MAX_DMG = 10000;
    public static final int MAX_LEVEL = 30;
    public static final Enchantment[] enchs = { Enchantment.efficiency, Enchantment.fortune, Enchantment.silkTouch };//, Enchantment.unbreaking };
    private static Random rand = new Random();

    public ItemBambooPickaxe() {
        super(ToolMaterial.GOLD);
        this.setNoRepair();
        this.setMaxDamage(MAX_DMG);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int posX, int posY, int posZ, EntityLivingBase entity) {
        if ((double) block.getBlockHardness(world, posX, posY, posZ) != 0.0D) {
            if (!world.isRemote) {
                if (rand.nextInt(getUnbreaking(itemStack) + 1) == 0) {
                    int exp = MAX_LEVEL - getLevel(itemStack) + 10000;
                    if (block instanceof BlockOre) {
                        exp += block.getExpDrop(world, world.getBlockMetadata(posX, posY, posZ), 0);
                    }

                    addExp(exp, itemStack, entity);
                }
                BambooEnchantment.activate(itemStack, world, entity, posX, posY, posZ);
            }
        }

        return true;
    }

    @Override
    public int getHarvestLevel(ItemStack itemStack, String toolClass) {
        return getSPEnchaLevel(itemStack, BambooEnchantment.power.getId());
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {

    }

    private void addExp(int exp, ItemStack is, EntityLivingBase entity) {
        if (exp + is.getItemDamage() < MAX_DMG) {
            is.damageItem(exp, entity);
            return;
        } else {
            exp = (exp + is.getItemDamage()) - MAX_DMG;
            if (hasLevelup(is)) {
                setLevel(getLevel(is) + 1, is);
                //バニラエンチャント
                HashMap<Enchantment, Short> nomalEnchmap = new HashMap<Enchantment, Short>();
                for (Enchantment e : enchs) {
                    if (rand.nextFloat() < (e.getWeight() / 20F)) {
                        if (rand.nextBoolean()) {
                            nomalEnchmap.put(e, (short) (rand.nextBoolean() ? 1 : -1));
                        }
                    }
                }
                //特殊エンチャント
                HashMap<EnchantBase, Short> specialEnchmap = new HashMap<EnchantBase, Short>();
                for (EnchantBase e : BambooEnchantment.idToEnchantMap.values()) {
                    if (rand.nextFloat() < e.getWeight()) {
                        if (rand.nextBoolean()) {
                            specialEnchmap.put(e, (short) (rand.nextBoolean() ? 1 : -1));
                        }
                    }
                }
                if (getLevel(is) % 10 == 0) {
                    //10の倍数では必ず一つはつく
                    if (nomalEnchmap.isEmpty()) {
                        nomalEnchmap.put(enchs[rand.nextInt(enchs.length)], (short) 1);
                    }
                    if (specialEnchmap.isEmpty()) {
                        specialEnchmap.put(BambooEnchantment.idToEnchantMap.get(rand.nextInt(BambooEnchantment.idToEnchantMap.size())), (short) 1);
                    }
                }
                //表示
                if (entity instanceof EntityPlayer) {
                    ((EntityPlayer) entity).addChatMessage(new ChatComponentText("LevelUp!!:" + getLevel(is)));
                }
                addNomalEnchLevel(is, nomalEnchmap, entity);
                addSpecialEnchLevel(is, specialEnchmap, entity);
                is.setItemDamage(0);
                addExp(exp, is, entity);
            }

        }
    }

    private void addNomalEnchLevel(ItemStack is, HashMap<Enchantment, Short> map, Entity entity) {
        if (is.hasTagCompound() && !map.isEmpty()) {
            if (!is.stackTagCompound.hasKey("ench", 9)) {
                is.stackTagCompound.setTag("ench", new NBTTagList());
            }
            NBTTagList nbttaglist = is.getEnchantmentTagList();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
                short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");
                Enchantment ench = Enchantment.enchantmentsList[short1];
                String s = StatCollector.translateToLocal(ench.getName()) + ":";
                String s2 = "MAX";
                if (map.containsKey(ench)) {
                    if (map.get(ench) > 0 && short2 < 10) {
                        s2 = map.get(ench) > 0 ? "UP" : "DOWN";
                        nbttaglist.getCompoundTagAt(i).setShort("lvl", (short) (short2 + map.get(ench)));
                    } else if (0 > map.get(ench) && short2 == 1) {
                        s2 = "REMOVE";
                        nbttaglist.removeTag(i);
                    }
                    map.remove(ench);
                    if (entity instanceof EntityPlayer) {
                        ((EntityPlayer) entity).addChatMessage(new ChatComponentText(s + s2));
                    }
                }
            }
            if (!map.isEmpty() && hasNewEnchant(is)) {
                for (Entry<Enchantment, Short> entry : map.entrySet()) {
                    if ((short) entry.getValue() > 0) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setShort("id", (short) entry.getKey().effectId);
                        nbt.setShort("lvl", (short) entry.getValue());
                        nbttaglist.appendTag(nbt);
                        if (entity instanceof EntityPlayer) {
                            ((EntityPlayer) entity).addChatMessage(new ChatComponentText(StatCollector.translateToLocal(entry.getKey().getName()) + ":ADD"));
                        }
                    }
                }
            }

        }
    }

    private void addSpecialEnchLevel(ItemStack is, HashMap<EnchantBase, Short> map, Entity entity) {
        if (is.hasTagCompound() && !map.isEmpty()) {
            if (!is.stackTagCompound.hasKey("spench", 9)) {
                is.stackTagCompound.setTag("spench", new NBTTagList());
            }
            NBTTagList nbttaglist = is.stackTagCompound.getTagList("spench", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
                short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");
                EnchantBase ench = BambooEnchantment.idToEnchantMap.get(short1);
                if (map.containsKey(ench)) {
                    String s = StatCollector.translateToLocal("bambooEnch." + ench.getName()) + ":";
                    String s2 = "MAX";
                    if (map.get(ench) > 0 && short2 < 10 && short2 < ench.getMaxLevel()) {
                        s2 = (map.get(ench) > 0 ? "UP" : "DOWN");
                        nbttaglist.getCompoundTagAt(i).setShort("lvl", (short) (short2 + map.get(ench)));
                    } else if (0 > map.get(ench) && short2 == 1) {
                        s2 = "REMOVE";
                        nbttaglist.removeTag(i);
                    }
                    if (entity instanceof EntityPlayer) {
                        ((EntityPlayer) entity).addChatMessage(new ChatComponentText(s + s2));
                    }
                    map.remove(ench);
                }
            }
            if (!map.isEmpty() && hasNewEnchant(is)) {
                for (Entry<EnchantBase, Short> entry : map.entrySet()) {
                    if ((short) entry.getValue() > 0) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setShort("id", (short) entry.getKey().getId());
                        nbt.setShort("lvl", (short) entry.getValue());
                        nbttaglist.appendTag(nbt);
                        if (entity instanceof EntityPlayer) {
                            ((EntityPlayer) entity).addChatMessage(new ChatComponentText(StatCollector.translateToLocal("bambooEnch." + BambooEnchantment.idToEnchantMap.get(entry.getKey()).getName()) + ":ADD"));
                        }
                    }
                }
            }

        }
    }

    private boolean hasNewEnchant(ItemStack is) {
        return (is.stackTagCompound.getTagList("spench", 10).tagCount() + is.getEnchantmentTagList().tagCount()) < 10;
    }

    private int getSPEnchaLevel(ItemStack is, int enchId) {
        NBTTagList nbttaglist = is.stackTagCompound.getTagList("spench", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            if (nbttaglist.getCompoundTagAt(i).getShort("id") == enchId) {
                return nbttaglist.getCompoundTagAt(i).getShort("lvl");
            }
        }
        return 0;
    }

    private boolean hasLevelup(ItemStack is) {
        return getLevel(is) < MAX_LEVEL;
    }

    private void setLevel(int level, ItemStack is) {
        if (is.stackTagCompound == null) {
            initTagCompound(is);
        }
        is.stackTagCompound.setInteger("toolLevel", level);
    }

    private int getLevel(ItemStack is) {
        if (is.stackTagCompound == null) {
            initTagCompound(is);
        }
        return is.stackTagCompound.getInteger("toolLevel");
    }

    private int getUnbreaking(ItemStack is) {
        if (!is.stackTagCompound.hasKey("ench", 9)) {
            is.stackTagCompound.setTag("ench", new NBTTagList());
        }
        NBTTagList nbttaglist = is.getEnchantmentTagList();
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            if (nbttaglist.getCompoundTagAt(i).getShort("id") == Enchantment.unbreaking.effectId) {
                return nbttaglist.getCompoundTagAt(i).getShort("lvl");
            }
        }
        return 0;
    }

    private NBTTagCompound getTagCompound(ItemStack is) {
        return is.stackTagCompound != null ? is.stackTagCompound : initTagCompound(is);
    }

    private NBTTagCompound initTagCompound(ItemStack is) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("toolLevel", 0);
        return is.stackTagCompound = nbt;
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 - ((double) stack.getItemDamageForDisplay() / (double) stack.getMaxDamage());
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent e) {
        ItemStack is = e.itemStack;
        if (is.getItem() == this) {
            String s = e.toolTip.get(0);
            e.toolTip.set(0, s + " Level:" + getLevel(is));
            if (is.hasTagCompound()) {
                NBTTagList nbttaglist = is.stackTagCompound.getTagList("spench", 10);

                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    e.toolTip.add(e.toolTip.size() - 1, StatCollector.translateToLocal("bambooEnch." + BambooEnchantment.idToEnchantMap.get(nbttaglist.getCompoundTagAt(i).getShort("id")).getName()) + " " + StatCollector.translateToLocal("enchantment.level." + nbttaglist.getCompoundTagAt(i).getShort("lvl")));
                }
            }
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        return getSPEnchaLevel(par1ItemStack, BambooEnchantment.eat.getId()) != 0 ? EnumAction.eat : EnumAction.none;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return getSPEnchaLevel(par1ItemStack, BambooEnchantment.eat.getId()) != 0 ? 32 : 0;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (getSPEnchaLevel(par1ItemStack, BambooEnchantment.eat.getId()) != 0) {
            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        }

        return par1ItemStack;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        --par1ItemStack.stackSize;
        par3EntityPlayer.getFoodStats().func_151686_a(new ItemFood(2, false), par1ItemStack);
        par2World.playSoundAtEntity(par3EntityPlayer, "random.burp", 0.5F, par2World.rand.nextFloat() * 0.1F + 0.9F);
        return par1ItemStack;
    }
}
