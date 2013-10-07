package ruby.bamboo.item.magatama;

import ruby.bamboo.entity.magatama.EntityGravityHole;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class MagatamaPurple implements IMagatama{

	@Override
	public Class getEffectClass() {
		return EntityGravityHole.class;
	}
	@Override
	public void holdingEffect(Entity entity,int invIndex) {
		if(entity instanceof EntityLivingBase){
				//弱体化
			if(((EntityLivingBase) entity).isPotionActive(18)){
				((EntityLivingBase) entity).removePotionEffect(18);
				//ぽいずん
			}else if(((EntityLivingBase) entity).isPotionActive(19)){
				((EntityLivingBase) entity).removePotionEffect(19);
			}
		}
	}
	@Override
	public int getReality() {
		return 1;
	}
	@Override
	public int getColor() {
		return 0x6600FF;
	}

}
