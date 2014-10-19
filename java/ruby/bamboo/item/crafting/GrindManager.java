package ruby.bamboo.item.crafting;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import ruby.bamboo.api.crafting.grind.GrindInputItem;
import ruby.bamboo.api.crafting.grind.GrindInputOreItem;
import ruby.bamboo.api.crafting.grind.GrindRecipe;
import ruby.bamboo.api.crafting.grind.IGrindInputItem;
import ruby.bamboo.api.crafting.grind.IGrindRecipe;
import cpw.mods.fml.common.FMLLog;

public class GrindManager {
    private static final HashMap<IGrindInputItem, IGrindRecipe> recipeMap = new HashMap<IGrindInputItem, IGrindRecipe>();

    private GrindManager() {
    }

    public static void addRecipe(ItemStack output, Block input) {
        addRecipe(output, null, new ItemStack(input), 0);
    }

    public static void addRecipe(ItemStack output, Item input) {
        addRecipe(output, null, new ItemStack(input), 0);
    }

    public static void addRecipe(ItemStack output, ItemStack bonus, Block input, float bonusWeight) {
        addRecipe(output, bonus, new ItemStack(input), bonusWeight);
    }

    public static void addRecipe(ItemStack output, ItemStack bonus, Item input, float bonusWeight) {
        addRecipe(output, bonus, new ItemStack(input), bonusWeight);
    }

    public static void addRecipe(ItemStack output, ItemStack input) {
        addRecipe(output, null, input, 0);
    }

    public static void addRecipe(ItemStack output, ItemStack bonus, ItemStack input, float bonusWeight) {
        if (output != null && input != null) {
            IGrindInputItem iInput = new GrindInputItem(input);
            if (bonus != null) {
                addRecipe(new GrindRecipe(iInput, output, bonus, bonusWeight));
            } else {
                addRecipe(new GrindRecipe(iInput, output, null, bonusWeight));
            }
        } else {
            FMLLog.warning("Grinding recipe wrong: out:" + output + " in:" + input);
        }
    }

    public static void addRecipe(ItemStack output, ItemStack bonus, String inputOreName, int inputStack, float bonusWeight) {
        if (output != null && inputOreName != null && !OreDictionary.getOres(inputOreName).isEmpty()) {
            IGrindInputItem iInput = new GrindInputOreItem(inputOreName, inputStack);
            if (bonus != null) {
                addRecipe(new GrindRecipe(iInput, output, bonus, bonusWeight));
            } else {
                addRecipe(new GrindRecipe(iInput, output, null, bonusWeight));
            }
        } else {
            FMLLog.warning("Grinding recipe wrong: out:" + output + " in:" + inputOreName);
        }
    }

    public static void addRecipe(IGrindRecipe iRecipe) {
        if (recipeMap.containsKey(iRecipe.getInput())) {
            FMLLog.warning("Grinding recipe registered: " + iRecipe.getInput());
            return;
        }

        recipeMap.put(iRecipe.getInput(), iRecipe);
    }

    public static IGrindRecipe getOutput(ItemStack input) {
        int[] oreIds = OreDictionary.getOreIDs(input);
        IGrindInputItem iInput;
        //通常検索
        iInput = new GrindInputItem(input);
        if (recipeMap.containsKey(iInput)) {
            return getResult(recipeMap.get(iInput), iInput);
        }
        //ワイルドカード検索
        ItemStack inputWild = input.copy();
        inputWild.setItemDamage(Short.MAX_VALUE);
        iInput = new GrindInputItem(inputWild);
        if (recipeMap.containsKey(iInput)) {
            return getResult(recipeMap.get(iInput), iInput);
        }
        //鉱石辞書検索
        if (0 < oreIds.length) {
            String oreName;
            for (int id : oreIds) {
                oreName = OreDictionary.getOreName(id);
                iInput = new GrindInputOreItem(oreName, input.stackSize);
                if (recipeMap.containsKey(iInput)) {
                    return getResult(recipeMap.get(iInput), iInput);
                }
            }
        }

        return null;
    }

    private static IGrindRecipe getResult(IGrindRecipe recipe, IGrindInputItem iInput) {
        if (recipe.getInput().getStackSize() <= iInput.getStackSize()) {
            if (isOreMatch(iInput.getOreIDs(), recipe.getInput().getOreIDs()) || recipe.getInput().equals(iInput)) {
                return recipe;
            }
        }
        return null;
    }

    public static boolean isOreMatch(int[] input, int[] target) {
        Arrays.sort(input);
        for (int t : target) {
            if (0 <= Arrays.binarySearch(input, t)) {
                return true;
            }
        }
        return false;
    }

    public static Collection<IGrindRecipe> getRecipeList() {
        return recipeMap.values();
    }
}
