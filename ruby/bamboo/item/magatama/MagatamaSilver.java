package ruby.bamboo.item.magatama;

import java.util.Iterator;

import ruby.bamboo.entity.magatama.EntityClock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class MagatamaSilver implements IMagatama{


	@Override
	public Class getEffectClass() {
		return EntityClock.class;
	}

	@Override
	public void holdingEffect(Entity entity,int invIndex) {
		if(entity instanceof EntityPlayer){
			ItemStack is;
			for(int i=0;i<invIndex;i++){
				is=((EntityPlayer) entity).inventory.getStackInSlot(i);
				if(isItemStackIsThis(is)){
					return;
				}
			}
			Iterator<PotionEffect> potions=((EntityLivingBase) entity).getActivePotionEffects().iterator();
			while(potions.hasNext()){
				potions.next().duration+=entity.worldObj.rand.nextBoolean()?1:0;
				((EntityPlayer) entity).getFoodStats().addExhaustion(0.05F);
			}
			
		}
	}
	private boolean isItemStackIsThis(ItemStack is){
		return is!=null&&is.getItem() instanceof ItemMagatama&&ItemMagatama.getMagatama(is.getItemDamage())==this;
	}
	@Override
	public int getReality() {
		return 1;
	}

	@Override
	public int getColor() {
		return 0xEEEEEE;
	}
}
