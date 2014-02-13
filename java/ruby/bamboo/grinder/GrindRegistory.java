package ruby.bamboo.grinder;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GrindRegistory {
    private static final ArrayList<GrindRecipe> recipe = new ArrayList<GrindRecipe>();
    public static final short WILD_CARD = Short.MAX_VALUE;

    private GrindRegistory() {
    }

    public static void addRecipe(ItemStack output, Block input) {
        addRecipe(output, null, new ItemStack(input), 0);
    }

    public static void addRecipe(ItemStack output, Item input) {
        addRecipe(output, null, new ItemStack(input), 0);
    }

    public static void addRecipe(ItemStack output, ItemStack bonus, Block input, int bonusWeight) {
        addRecipe(output, bonus, new ItemStack(input), bonusWeight);
    }

    public static void addRecipe(ItemStack output, ItemStack bonus, Item input, int bonusWeight) {
        addRecipe(output, bonus, new ItemStack(input), bonusWeight);
    }

    public static void addRecipe(ItemStack output, ItemStack input) {
        addRecipe(output, null, input, 0);
    }

    public static void addRecipe(ItemStack output, ItemStack bonus, ItemStack input, int bonusWeight) {
        if (output != null && input != null) {
            if (bonus != null) {
                recipe.add(new GrindRecipe(input, output, bonus, bonusWeight));
            } else {
                recipe.add(new GrindRecipe(input, output, null, bonusWeight));
            }
        } else {
            System.out.println("Grinding recipe wrong:" + output.getItem().getUnlocalizedName());
        }
    }

    public static GrindRecipe getOutput(ItemStack input) {
        GrindRecipe result = null;

        for (GrindRecipe gr : recipe) {
            if (input.getItem() == gr.getInput().getItem() && gr.getInput().stackSize <= input.stackSize) {
                if (gr.getInput().getItemDamage() == WILD_CARD || input.getItemDamage() == gr.getInput().getItemDamage()) {
                    result = gr;
                    break;
                }
            }
        }

        return result;
    }
}
