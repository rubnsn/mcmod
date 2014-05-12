package ruby.bamboo.item.enchant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EnchantLucky extends EnchantBase {

    public EnchantLucky(int id, String name, int maxLevel, float weight, float tp) {
        super(id, name, maxLevel, weight, tp);
    }

    @Override
    void effect(ItemStack itemStack, World world, int posX, int posY, int posZ, EntityLivingBase entity, int enchantLvl) {
        extraDrop(entity, rand.nextInt(enchantLvl));
    }

    private ItemStack[] dropTable = { new ItemStack(Items.gold_ingot, 8), new ItemStack(Items.diamond, 4) };

    enum EnumDrop {
        bone(Items.bone, 4),
        clay(Items.clay_ball, 8),
        coal(Items.coal, 32),
        iron(Items.iron_ingot, 16),
        goald_nugget(Items.gold_nugget, 16),
        goald(Items.gold_ingot, 8),
        diamond(Items.diamond, 4);
        EnumDrop(Item item, int stackSize) {
            this.item = item;
            this.stackSize = stackSize;
        }

        static EnumDrop[] values = EnumDrop.values();
        Item item;
        int stackSize;
    }

    private void extraDrop(Entity entity, int i) {

        if (rand.nextFloat() < 0.1F && dropTable.length < i) {
            entity.dropItem(EnumDrop.values[i].item, EnumDrop.values[i].stackSize);
            extraDrop(entity, i + 1);
        }

    }
}
