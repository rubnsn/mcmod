package ruby.bamboo;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class LivingDropsEventHandler {
    private static final LivingDropsEventHandler instance = new LivingDropsEventHandler();
    private Multimap<Class<? extends Entity>, ItemStack> dropRareTable = ArrayListMultimap.create();

    public static void addRareDrop(Class<? extends Entity> dropEntity, ItemStack dropItem) {
        instance.dropRareTable.put(dropEntity, dropItem);
    }

    private LivingDropsEventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent event) {
        if (event.recentlyHit && event.specialDropValue < 5) {
            List<ItemStack> drops = (List<ItemStack>) dropRareTable.get(event.entity.getClass());
            if (drops != null && 0 < drops.size()) {
                Entity entity = event.entityLiving;
                ItemStack dropItem = drops.get(entity.worldObj.rand.nextInt(drops.size())).copy();
                event.drops.add(new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, dropItem));
            }
        }
    }
}
