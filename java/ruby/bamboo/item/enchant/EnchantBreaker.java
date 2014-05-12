package ruby.bamboo.item.enchant;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EnchantBreaker extends EnchantBase {

    public EnchantBreaker(int id, String name, int maxLevel, float weight, float tp) {
        super(id, name, maxLevel, weight, tp);
    }

    @Override
    void effect(ItemStack itemStack, World world, int posX, int posY, int posZ, EntityLivingBase entity, int enchantLvl) {
        Block b;
        for (int i = -enchantLvl; i <= enchantLvl; i++) {
            for (int j = -enchantLvl; j <= enchantLvl; j++) {
                for (int k = -enchantLvl; k <= enchantLvl; k++) {
                    b = world.getBlock(posX + i, posY + j, posZ + k);
                    if (b.getBlockHardness(world, posX + i, posY + j, posZ + k) >= 0) {
                        b.dropBlockAsItem(world, posX + i, posY + j, posZ + k, world.getBlockMetadata(posX + i, posY + j, posZ + k), 0);
                        world.setBlockToAir(posX + i, posY + j, posZ + k);
                    }
                }
            }
        }
    }

}
