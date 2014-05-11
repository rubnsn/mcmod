package ruby.bamboo.item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemBambooPickaxe extends ItemPickaxe {
    public static final int MAX_DMG = 10000;
    public static final int MAX_LEVEL = 30;
    public static final Enchantment[] enchs = { Enchantment.efficiency, Enchantment.fortune, Enchantment.silkTouch };//, Enchantment.unbreaking };
    private static Random rand = new Random();

    public enum EnumBambooEnchantment {
        blood(0, 3, 0.1F),
        teleport(1, 3, 0.1F),
        lucky(2, 5, 0.01F),
        power(3, 3, 1F),
        accele(4, 3, 0.1F),
        summons(5, 2, 0.1F),
        breaker(6, 2, 0.05F),
        burst(7, 3, 0.05F),
        chain(8, 1, 0.25F),
        eat(9, 1, 0.5F);
        EnumBambooEnchantment(int id, int max, float weight) {
            this.id = id;
            this.max = max;
            this.weight = weight;
        }

        public static final EnumBambooEnchantment[] values = { blood, teleport, lucky, power, accele, summons, breaker, burst, chain, eat };
        private int id;
        private int max;
        private float weight;
    }

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
                //だめーじ
                if (canUseSPEnch(itemStack, EnumBambooEnchantment.blood, 0.05F)) {
                    entity.attackEntityFrom(DamageSource.magic, rand.nextInt(getSPEnchaLevel(itemStack, EnumBambooEnchantment.blood.id) * 3));
                }
                //強制テレポ
                if (canUseSPEnch(itemStack, EnumBambooEnchantment.teleport, 0.05F)) {
                    int tposX, tposY, tposZ;
                    int range = getSPEnchaLevel(itemStack, EnumBambooEnchantment.teleport.id);
                    tposX = rand.nextInt(range * 20) - range * 10;
                    tposY = rand.nextInt(range * 20) - range * 10;
                    tposZ = rand.nextInt(range * 20) - range * 10;
                    teleportTo(entity, entity.posX + tposX, entity.posY + tposY, entity.posZ + tposZ);
                }
                //幸運
                if (canUseSPEnch(itemStack, EnumBambooEnchantment.lucky, 0.05F)) {
                    extraDrop(entity, rand.nextInt(getSPEnchaLevel(itemStack, EnumBambooEnchantment.lucky.id)));
                }
                //(空腹までの)時を早める
                if (getSPEnchaLevel(itemStack, EnumBambooEnchantment.accele.id) != 0) {
                    if (entity instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) entity;
                        if (player.getFoodStats().getFoodLevel() > 0 && rand.nextFloat() < 0.5F) {
                            player.getFoodStats().addExhaustion(getSPEnchaLevel(itemStack, EnumBambooEnchantment.accele.id) * 10F);
                        }
                    }
                }
                //召喚
                if (canUseSPEnch(itemStack, EnumBambooEnchantment.summons, 0.01F)) {
                    int tposX, tposY, tposZ;
                    int temp = getSPEnchaLevel(itemStack, EnumBambooEnchantment.summons.id);
                    for (int i = 0; i < temp; i++) {
                        tposX = rand.nextInt(10) - 5;
                        tposY = rand.nextInt(10) - 5;
                        tposZ = rand.nextInt(10) - 5;
                        Entity e = new EntityCreeper(world);
                        e.setPosition(entity.posX + tposX, entity.posY + tposY, entity.posZ + tposZ);
                        if (world.getCollidingBoundingBoxes(e, e.boundingBox).size() == 0) {
                            world.spawnEntityInWorld(e);
                        }
                    }
                }
                //破壊
                if (canUseSPEnch(itemStack, EnumBambooEnchantment.breaker, 0.1F)) {
                    int p = getSPEnchaLevel(itemStack, EnumBambooEnchantment.breaker.id);
                    Block b;
                    for (int i = -p; i <= p; i++) {
                        for (int j = -p; j <= p; j++) {
                            for (int k = -p; k <= p; k++) {
                                b = world.getBlock(posX + i, posY + j, posZ + k);
                                if (b.getBlockHardness(world, posX + i, posY + j, posZ + k) >= 0) {
                                    b.dropBlockAsItem(world, posX + i, posY + j, posZ + k, world.getBlockMetadata(posX + i, posY + j, posZ + k), 0);
                                    world.setBlockToAir(posX + i, posY + j, posZ + k);
                                }
                            }
                        }
                    }
                }
                //爆発
                if (canUseSPEnch(itemStack, EnumBambooEnchantment.burst, 0.1F)) {
                    createExplosion(world, entity, posX, posY, posZ, getSPEnchaLevel(itemStack, EnumBambooEnchantment.burst.id) * 2F);
                }
                //連鎖
                if (canUseSPEnch(itemStack, EnumBambooEnchantment.chain, 1F)) {
                    chainBreak(world, posX, posY, posZ, getFortune(itemStack), 0);
                }
            }
        }

        return true;
    }

    private void chainBreak(World world, int posX, int posY, int posZ, int fortune, int loopCount) {
        if (loopCount > 100) {
            return;
        }
        Block block;
        for (ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS) {
            block = world.getBlock(posX + fd.offsetX, posY + fd.offsetY, posZ + fd.offsetZ);
            if (block instanceof BlockOre) {
                block.dropBlockAsItem(world, posX, posY, posZ, world.getBlockMetadata(posX + fd.offsetX, posY + fd.offsetY, posZ + fd.offsetZ), fortune);
                world.setBlockToAir(posX + fd.offsetX, posY + fd.offsetY, posZ + fd.offsetZ);
                this.chainBreak(world, posX + fd.offsetX, posY + fd.offsetY, posZ + fd.offsetZ, fortune, ++loopCount);
            }
        }
    }

    private boolean canUseSPEnch(ItemStack is, EnumBambooEnchantment ebe, float probability) {
        return rand.nextFloat() < probability && getSPEnchaLevel(is, ebe.id) != 0;
    }

    private void createExplosion(World world, EntityLivingBase entity, int posX, int posY, int posZ, float f) {
        Explosion e = new Explosion(world, entity, posX, posY, posZ, f) {
            private int field_77289_h = 16;
            private World worldObj;

            public Explosion setWorld(World world) {
                this.worldObj = world;
                return this;
            }

            @Override
            public void doExplosionA() {
                float f = this.explosionSize;
                HashSet hashset = new HashSet();
                int i;
                int j;
                int k;
                double d5;
                double d6;
                double d7;

                for (i = 0; i < this.field_77289_h; ++i) {
                    for (j = 0; j < this.field_77289_h; ++j) {
                        for (k = 0; k < this.field_77289_h; ++k) {
                            if (i == 0 || i == this.field_77289_h - 1 || j == 0 || j == this.field_77289_h - 1 || k == 0 || k == this.field_77289_h - 1) {
                                double d0 = (double) ((float) i / ((float) this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                                double d1 = (double) ((float) j / ((float) this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                                double d2 = (double) ((float) k / ((float) this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                                d0 /= d3;
                                d1 /= d3;
                                d2 /= d3;
                                float f1 = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
                                d5 = this.explosionX;
                                d6 = this.explosionY;
                                d7 = this.explosionZ;

                                for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
                                    int j1 = MathHelper.floor_double(d5);
                                    int k1 = MathHelper.floor_double(d6);
                                    int l1 = MathHelper.floor_double(d7);
                                    Block block = this.worldObj.getBlock(j1, k1, l1);

                                    if (block.getMaterial() != Material.air) {
                                        float f3 = this.exploder != null ? this.exploder.func_145772_a(this, this.worldObj, j1, k1, l1, block) : block.getExplosionResistance(this.exploder, worldObj, j1, k1, l1, explosionX, explosionY, explosionZ);
                                        f1 -= (f3 + 0.3F) * f2;
                                    }

                                    if (f1 > 0.0F && (this.exploder == null || this.exploder.func_145774_a(this, this.worldObj, j1, k1, l1, block, f1))) {
                                        hashset.add(new ChunkPosition(j1, k1, l1));
                                    }

                                    d5 += d0 * (double) f2;
                                    d6 += d1 * (double) f2;
                                    d7 += d2 * (double) f2;
                                }
                            }
                        }
                    }
                }

                this.affectedBlockPositions.addAll(hashset);
            }
        }.setWorld(world);
        e.isSmoking = true;
        e.doExplosionA();
        e.doExplosionB(true);
    }

    @Override
    public int getHarvestLevel(ItemStack itemStack, String toolClass) {
        return getSPEnchaLevel(itemStack, EnumBambooEnchantment.power.id);
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {

    }

    private void extraDrop(Entity entity, int i) {

        if (rand.nextFloat() < 0.1F) {
            switch (i) {
            case 0:
                entity.dropItem(Items.clay_ball, 8);
            case 1:
                entity.dropItem(Items.coal, 32);
            case 2:
                entity.dropItem(Items.iron_ingot, 16);
            case 3:
                entity.dropItem(Items.gold_nugget, 16);
            case 4:
                entity.dropItem(Items.gold_ingot, 8);
            case 5:
                entity.dropItem(Items.diamond, 4);
            }
            extraDrop(entity, i + 1);
        }

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
                HashMap<EnumBambooEnchantment, Short> specialEnchmap = new HashMap<EnumBambooEnchantment, Short>();
                for (EnumBambooEnchantment e : EnumBambooEnchantment.values()) {
                    if (rand.nextFloat() < (e.weight)) {
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
                        specialEnchmap.put(EnumBambooEnchantment.values[rand.nextInt(EnumBambooEnchantment.values.length)], (short) 1);
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

    private void addSpecialEnchLevel(ItemStack is, HashMap<EnumBambooEnchantment, Short> map, Entity entity) {
        if (is.hasTagCompound() && !map.isEmpty()) {
            if (!is.stackTagCompound.hasKey("spench", 9)) {
                is.stackTagCompound.setTag("spench", new NBTTagList());
            }
            NBTTagList nbttaglist = is.stackTagCompound.getTagList("spench", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
                short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");
                EnumBambooEnchantment ench = EnumBambooEnchantment.values[short1];
                if (map.containsKey(ench)) {
                    String s = StatCollector.translateToLocal("bambooEnch." + ench.name()) + ":";
                    String s2 = "MAX";
                    if (map.get(ench) > 0 && short2 < 10 && short2 < ench.max) {
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
                for (Entry<EnumBambooEnchantment, Short> entry : map.entrySet()) {
                    if ((short) entry.getValue() > 0) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setShort("id", (short) entry.getKey().id);
                        nbt.setShort("lvl", (short) entry.getValue());
                        nbttaglist.appendTag(nbt);
                        if (entity instanceof EntityPlayer) {
                            ((EntityPlayer) entity).addChatMessage(new ChatComponentText(StatCollector.translateToLocal("bambooEnch." + EnumBambooEnchantment.values[entry.getKey().id]) + ":ADD"));
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

    private int getFortune(ItemStack is) {
        if (!is.stackTagCompound.hasKey("ench", 9)) {
            is.stackTagCompound.setTag("ench", new NBTTagList());
        }
        NBTTagList nbttaglist = is.getEnchantmentTagList();
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            if (nbttaglist.getCompoundTagAt(i).getShort("id") == Enchantment.fortune.effectId) {
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
                    e.toolTip.add(e.toolTip.size() - 1, StatCollector.translateToLocal("bambooEnch." + EnumBambooEnchantment.values[nbttaglist.getCompoundTagAt(i).getShort("id")].name()) + " " + StatCollector.translateToLocal("enchantment.level." + nbttaglist.getCompoundTagAt(i).getShort("lvl")));
                }
            }
        }
    }

    private boolean teleportTo(EntityLivingBase target, double par1, double par3, double par5) {

        double var7 = target.posX;
        double var9 = target.posY;
        double var11 = target.posZ;
        target.posX = par1;
        target.posY = par3;
        target.posZ = par5;
        boolean var13 = false;
        int var14 = MathHelper.floor_double(target.posX);
        int var15 = MathHelper.floor_double(target.posY);
        int var16 = MathHelper.floor_double(target.posZ);
        Block var18;

        if (target.worldObj.blockExists(var14, var15, var16)) {
            boolean var17 = false;

            while (!var17 && var15 > 0) {
                var18 = target.worldObj.getBlock(var14, var15 - 1, var16);

                if (var18 != Blocks.air && var18.getMaterial().blocksMovement()) {
                    var17 = true;
                } else {
                    --target.posY;
                    --var15;
                }
            }

            if (var17) {
                target.setPositionAndUpdate(target.posX, target.posY, target.posZ);

                if (target.worldObj.getCollidingBoundingBoxes(target, target.boundingBox).size() == 0 && !target.worldObj.isAnyLiquid(target.boundingBox)) {
                    var13 = true;
                }
            }
        }

        if (!var13) {
            target.setPositionAndUpdate(var7, var9, var11);
            return false;
        } else {
            short var30 = 128;

            for (int i = 0; i < var30; ++i) {
                double var19 = i / (var30 - 1.0D);
                float var21 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float var22 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float var23 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                double var24 = var7 + (target.posX - var7) * var19 + (this.rand.nextDouble() - 0.5D) * target.width * 2.0D;
                double var26 = var9 + (target.posY - var9) * var19 + this.rand.nextDouble() * target.height;
                double var28 = var11 + (target.posZ - var11) * var19 + (this.rand.nextDouble() - 0.5D) * target.width * 2.0D;
                target.worldObj.spawnParticle("portal", var24, var26, var28, var21, var22, var23);
            }

            target.worldObj.playSoundEffect(var7, var9, var11, "mob.endermen.portal", 1.0F, 1.0F);
            target.worldObj.playSoundAtEntity(target, "mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        return canUseSPEnch(par1ItemStack, EnumBambooEnchantment.eat, 1F) ? EnumAction.eat : EnumAction.none;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return canUseSPEnch(par1ItemStack, EnumBambooEnchantment.eat, 1F) ? 32 : 0;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (canUseSPEnch(par1ItemStack, EnumBambooEnchantment.eat, 1F)) {
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
