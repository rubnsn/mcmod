package ruby.bamboo.item.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class CookingManager {
    private static ArrayList<IRecipe> recipes = new ArrayList<IRecipe>();
    private static final CookingManager instance = new CookingManager();
    private Container dummyContainer = new Container() {
        @Override
        public boolean canInteractWith(EntityPlayer var1) {
            return false;
        }
    };
    private InventoryCrafting crafting = new InventoryCrafting(dummyContainer, 3, 3);

    private CookingManager() {
    }

    public static void addRecipe(IRecipe recipe) {
        CookingManager.getInstance().getRecipeList().add(recipe);
    }

    public List<IRecipe> getRecipeList() {
        return recipes;
    }

    public static CookingManager getInstance() {
        return instance;
    }

    public ItemStack findMatchingRecipe(ItemStack[] itemStack, World par2World) {
        for (int i = 0; i < 9; i++) {
            crafting.setInventorySlotContents(i, itemStack[i]);
        }
        return findMatchingRecipe(crafting, par2World);
    }

    public ItemStack findMatchingRecipe(InventoryCrafting par1InventoryCrafting, World par2World) {
        int i = 0;
        ItemStack is = null;
        int j;

        for (j = 0; j < par1InventoryCrafting.getSizeInventory(); ++j) {
            ItemStack is2 = par1InventoryCrafting.getStackInSlot(j);
            if (is2 != null) {
                if (i == 0) {
                    is = is2;
                }
                ++i;
            }
        }
        //単一アイテムの場合、バニラ竈互換の精錬レシピをまずチェック、
        if (i == 1) {
            is = FurnaceRecipes.smelting().getSmeltingResult(is);
            if (is != null) {
                return is.copy();
            }
        }

        for (j = 0; j < this.recipes.size(); ++j) {
            IRecipe irecipe = this.recipes.get(j);
            if (irecipe.matches(par1InventoryCrafting, par2World)) {
                return irecipe.getCraftingResult(par1InventoryCrafting);
            }
        }

        return null;
    }

    public static void addShapedRecipe(Block out, Object... recipe) {
        addRecipe(new ShapedOreRecipe(out, recipe));
    }

    public static void addShapedRecipe(Item out, Object... recipe) {
        addRecipe(new ShapedOreRecipe(out, recipe));
    }

    public static void addShapedRecipe(ItemStack out, Object... recipe) {
        addRecipe(new ShapedOreRecipe(out, recipe));
    }

    public static void addShapelessRecipe(ItemStack out, Object... recipe) {
        addRecipe(new ShapelessOreRecipe(out, recipe));
    }
}
