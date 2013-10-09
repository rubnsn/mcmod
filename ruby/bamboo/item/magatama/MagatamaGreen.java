package ruby.bamboo.item.magatama;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MagatamaGreen implements IMagatama {

    @Override
    public int getColor() {
        return 0x33FF33;
    }

    @Override
    public Class getEffectClass() {
        return null;
    }

    @Override
    public int getReality() {
        return 1;
    }

    @Override
    public void holdingEffect(Entity entity, int invIndex) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (player.getFoodStats().getFoodLevel() > 0 && player.fallDistance > 2) {
                player.fallDistance *= 0.9;
                player.getFoodStats().addExhaustion(5F);
            }
        }
    }

}
