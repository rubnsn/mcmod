package ruby.bamboo;

import ruby.bamboo.item.ItemSack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingHandler implements ICraftingHandler
{
    private static CraftingHandler instance = new CraftingHandler();
    public static void init()
    {
        GameRegistry.registerCraftingHandler(instance);
    }
    @Override
    public void onCrafting(EntityPlayer player, ItemStack item,
                           IInventory craftMatrix)
    {
        if (player.worldObj.isRemote)
        {
            return;
        }

        if (item.getItem() instanceof ItemSack)
        {
            int lim = craftMatrix.getSizeInventory();

            while (lim-- > 0)
            {
                if (craftMatrix.getStackInSlot(lim) == null)
                {
                    continue;
                }

                Item target = craftMatrix.getStackInSlot(lim).getItem();

                if (target instanceof ItemSack)
                {
                    ((ItemSack)target).release(craftMatrix.getStackInSlot(lim), player.worldObj, player);
                }
            }
        }
    }

    @Override
    public void onSmelting(EntityPlayer player, ItemStack item)
    {
    }
}
