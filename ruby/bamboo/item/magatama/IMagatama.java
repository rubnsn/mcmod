package ruby.bamboo.item.magatama;

import net.minecraft.entity.Entity;

public interface IMagatama {
    int getColor();

    Class getEffectClass();

    int getReality();

    void holdingEffect(Entity entity, int invIndex);
}
