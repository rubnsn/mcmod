package ruby.bamboo.item.enchant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EnchantSummons extends EnchantBase {

    public EnchantSummons(int id, String name, int maxLevel, float weight, float tp) {
        super(id, name, maxLevel, weight, tp);
    }

    @Override
    void effect(ItemStack itemStack, World world, int posX, int posY, int posZ, EntityLivingBase entity, int enchantLvl) {
        int tposX, tposY, tposZ;
        for (int i = 0; i < enchantLvl; i++) {
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

}
