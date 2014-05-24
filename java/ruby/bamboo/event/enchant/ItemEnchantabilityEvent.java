package ruby.bamboo.event.enchant;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.Event;

public class ItemEnchantabilityEvent extends Event {
    public Item item;
    public int enchantability;

    public ItemEnchantabilityEvent(Item item) {
        this.item = item;
    }

    public static int getItemEnchantability(Item item) {
        ItemEnchantabilityEvent e = new ItemEnchantabilityEvent(item);
        MinecraftForge.EVENT_BUS.post(e);
        if (e.getResult() == Result.ALLOW) {
            return e.enchantability;
        }
        return 0;
    }
}
