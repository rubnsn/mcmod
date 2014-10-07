package ruby.bamboo.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemVillagerBlock extends ItemBlock {

    public ItemVillagerBlock(Block p_i45328_1_) {
        super(p_i45328_1_);
        this.setMaxStackSize(1);
    }

    @Override
    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
        if (armorType == 0) {
            return true;
        }
        return false;
    }
}
