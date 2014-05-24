package ruby.bamboo;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraftforge.common.MinecraftForge;
import ruby.bamboo.event.enchant.CanEnchantItemEvent;
import ruby.bamboo.event.enchant.ItemEnchantabilityEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class EnchantHandler {
    public EnchantHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void canEnchantItemEvent(CanEnchantItemEvent e) {
        if (e.item instanceof ItemHoe) {
            //判定キャンセル常にtrue
            e.setCanceled(e.type == EnumEnchantmentType.digger);
        }
    }

    @SubscribeEvent
    public void getItemEnchantabilityEvent(ItemEnchantabilityEvent event) {
        if (event.item instanceof ItemHoe) {
            try {
                event.setResult(Result.ALLOW);
                event.enchantability = ((Item.ToolMaterial) ReflectionHelper.getPrivateValue(ItemHoe.class, (ItemHoe) event.item, 0)).getEnchantability();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
