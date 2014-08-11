package ruby.bamboo.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import ruby.bamboo.BambooCore;
import ruby.bamboo.item.crafting.CookingManager;

public class ItemCookBook extends Item {
    private static final int MAX_LENGTH = 200;

    public ItemCookBook() {
        setMaxDamage(0);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        ItemStack book = new ItemStack(Items.written_book);
        book.setTagCompound(getCookRecipeNBT());
        return book;
    }

    private NBTTagCompound getCookRecipeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("title", StatCollector.translateToLocal(this.getUnlocalizedName() + ".name") + " ver:" + BambooCore.BAMBOO_VER);
        nbt.setString("author", "ruby");
        NBTTagList pagelist = new NBTTagList();
        StringBuffer page = new StringBuffer();
        for (IRecipe recipe : CookingManager.getInstance().getRecipeList()) {
            StringBuffer stb = new StringBuffer();
            stb.append(getLocalizedName(recipe.getRecipeOutput()) + ":");
            if (recipe instanceof ShapelessOreRecipe) {
                for (Object obj : ((ShapelessOreRecipe) recipe).getInput()) {
                    if (obj instanceof ItemStack) {
                        stb.append(getLocalizedName((ItemStack) obj));
                    } else if (obj instanceof List) {
                        stb.append("[");
                        stb.append(getLocalizedName(((List<ItemStack>) obj).get(0)) + ",");
                        stb.deleteCharAt(stb.length() - 1);
                        stb.append("]");
                    }
                    stb.append(",");
                }
                stb.deleteCharAt(stb.length() - 1);
            }
            stb.append("\n");
            if (MAX_LENGTH < page.length() + stb.length()) {
                pagelist.appendTag(new NBTTagString(page.toString()));
                page = new StringBuffer();
            }
            page.append(stb);
        }
        if (page.length() != 0) {
            pagelist.appendTag(new NBTTagString(page.toString()));
        }
        nbt.setTag("pages", pagelist);
        return nbt;
    }

    private String getLocalizedName(ItemStack is) {
        return StatCollector.translateToLocal(is.getUnlocalizedName() + ".name");
    }
}
