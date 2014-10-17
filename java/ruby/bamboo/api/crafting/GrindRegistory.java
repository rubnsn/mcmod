package ruby.bamboo.api.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;

public class GrindRegistory {
    /**
     * Grind
     * 
     * @param output
     * @param input
     */
    public static void addRecipe(ItemStack output, Block input) {
        addRecipe(output, null, new ItemStack(input), 0);
    }

    /**
     * Grind
     * 
     * @param output
     * @param input
     */
    public static void addRecipe(ItemStack output, Item input) {
        addRecipe(output, null, new ItemStack(input), 0);
    }

    /**
     * Grind
     * 
     * @param output
     * @param bonus
     * @param input
     * @param bonusWeight (rand.nectFloat()<=bonusWeight)
     */
    public static void addRecipe(ItemStack output, ItemStack bonus, Block input, float bonusWeight) {
        addRecipe(output, bonus, new ItemStack(input), bonusWeight);
    }

    /**
     * Grind
     * 
     * @param output
     * @param bonus
     * @param input
     * @param bonusWeight (rand.nectFloat()<=bonusWeight)
     */
    public static void addRecipe(ItemStack output, ItemStack bonus, Item input, float bonusWeight) {
        addRecipe(output, bonus, new ItemStack(input), bonusWeight);
    }

    /**
     * Grind
     * 
     * @param output
     * @param input
     */
    public static void addRecipe(ItemStack output, ItemStack input) {
        addRecipe(output, null, input, 0);
    }

    /**
     * Grind
     * 
     * @param output
     * @param bonus
     * @param input
     * @param bonusWeight (rand.nectFloat()<=bonusWeight)
     */
    public static void addRecipe(ItemStack output, ItemStack bonus, ItemStack input, float bonusWeight) {
        if (Loader.isModLoaded("BambooMod")) {
            ruby.bamboo.item.crafting.GrindRegistory.addRecipe(output, bonus, input, bonusWeight);
        }
    }
}
