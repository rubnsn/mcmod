package ruby.bamboo;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import ruby.bamboo.item.ItemSack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class CraftingHandler {
    private static CraftingHandler instance = new CraftingHandler();

    public static void init() {
        MinecraftForge.EVENT_BUS.register(instance);
    }

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event) {
        if (event.player.worldObj.isRemote) {
            return;
        }

        if (event.crafting.getItem() instanceof ItemSack) {
            int lim = event.craftMatrix.getSizeInventory();

            while (lim-- > 0) {
                if (event.craftMatrix.getStackInSlot(lim) == null) {
                    continue;
                }

                Item target = event.craftMatrix.getStackInSlot(lim).getItem();

                if (target instanceof ItemSack) {
                    ((ItemSack) target).release(event.craftMatrix.getStackInSlot(lim), event.player.worldObj, event.player);
                }
            }
        }
    }
}
