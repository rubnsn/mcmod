package ruby.bamboo.event.enchant;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

@Cancelable
public class CanEnchantItemEvent extends Event {
    public EnumEnchantmentType type;
    public Item item;

    public CanEnchantItemEvent(EnumEnchantmentType type, Item item) {
        this.type = type;
        this.item = item;
    }

}
