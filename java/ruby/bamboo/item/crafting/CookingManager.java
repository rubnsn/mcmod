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
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
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

    public static void addRecipe(ItemStack par1ItemStack, Object... par2ArrayOfObj) {
        ArrayList arraylist = new ArrayList();
        Object[] aobject = par2ArrayOfObj;
        int i = par2ArrayOfObj.length;

        for (int j = 0; j < i; ++j) {
            Object object1 = aobject[j];

            if (object1 instanceof ItemStack) {
                arraylist.add(((ItemStack) object1).copy());
            } else if (object1 instanceof Item) {
                arraylist.add(new ItemStack((Item) object1));
            } else {
                if (!(object1 instanceof Block)) {
                    throw new RuntimeException("Invalid shapeless recipy!");
                }

                arraylist.add(new ItemStack((Block) object1));
            }
        }

        recipes.add(new ShapelessRecipes(par1ItemStack, arraylist));
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

    public static void addShapelessOreRecipe(ItemStack out, Object... objects) {
        addRecipe(new ShapelessOreRecipe(out, objects));
    }
}
