package ruby.bamboo.item.magatama;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MagatamaBlue implements IMagatama{
//水属性？
	@Override
	public int getColor() {
		return 0x3333FF;
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
		if(entity instanceof EntityPlayer){
			if(!((EntityPlayer)entity).capabilities.isCreativeMode){
				if(entity.isInWater()){
					entity.motionX*=1.1;
					entity.motionZ*=1.1;
					//((EntityPlayer)entity).capabilities.isFlying=true;
				}else{
					//((EntityPlayer)entity).capabilities.isFlying=false;
				}
			}
		}
	}

}
