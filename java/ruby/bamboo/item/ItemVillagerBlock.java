package ruby.bamboo.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemVillagerBlock extends ItemBlock {

    public ItemVillagerBlock(Block p_i45328_1_) {
        super(p_i45328_1_);
        this.setMaxStackSize(1);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
        if (armorType == 0) {
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onSoundPlayEvent(PlaySoundAtEntityEvent event) {
        if (event.entity instanceof EntityPlayer) {
            ItemStack is = ((EntityPlayer) event.entity).getEquipmentInSlot(4);
            if (is != null && is.getItem() == this) {
                if (event.name.equals("game.player.hurt")) {
                    if (((EntityPlayer) event.entity).getHealth() % 3 == 0) {
                        event.name = "mob.villager.idle";
                    } else {
                        event.name = "mob.villager.hit";
                    }
                }
            }
        }
    }
}
