package ruby.bamboo.item.magatama;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class MagatamaPink implements IMagatama {

	@Override
	public int getColor() {
		return 0xFF66FF;
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
			EntityPlayer player=(EntityPlayer)entity;
			//player.func_110143_aJ() 現在HP
			//player.func_110138_aP() 最大HP
			if(player.getFoodStats().getFoodLevel()>0&&player.func_110138_aP()>player.func_110143_aJ()){
				if(entity.worldObj.getWorldTime()%5==0){
					player.heal(1F);
					player.getFoodStats().addExhaustion(10F);	
				}
			}
		}
	}

}
