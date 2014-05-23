package ruby.bamboo;

import net.minecraft.item.ItemHoe;
import net.minecraftforge.common.MinecraftForge;
import ruby.bamboo.event.enchant.CanEnchantItemEvent;
import ruby.bamboo.event.enchant.ItemEnchantabilityEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnchantHandler {
    public EnchantHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void canEnchantItemEvent(CanEnchantItemEvent e) {
        System.out.println("HOGEEEEEE");
        e.setCanceled(true);
        e.setResult(Result.ALLOW);
    }

    @SubscribeEvent
    public void canApplyAtEnchantingTableEvent(ItemEnchantabilityEvent e) {
        if (e.item instanceof ItemHoe) {
            e.setResult(Result.ALLOW);
            e.enchantability = 3;
        }
    }
}
