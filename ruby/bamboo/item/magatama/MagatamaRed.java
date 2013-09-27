package ruby.bamboo.item.magatama;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.entity.magatama.EntityFlareEffect;

public class MagatamaRed implements IMagatama{

	@Override
	public Class getEffectClass() {
		return EntityFlareEffect.class;
	}

	@Override
	public void holdingEffect(Entity entity,int invIndex) {
		entity.extinguish();
	}

	@Override
	public int getReality() {
		return 1;
	}

	@Override
	public int getColor() {
		return 0xDD2222;
	}
}
