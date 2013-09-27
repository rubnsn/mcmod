package ruby.bamboo.entity.magatama;

import java.lang.reflect.InvocationTargetException;

import ruby.bamboo.item.magatama.ItemMagatama;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityMagatama extends EntityThrowable {
	private ItemStack itemStack;
	private final static int ICON = 10;
	private int count;
	private boolean isImpacted = false;
	public EntityMagatama(World par1World) {
		super(par1World);
	}

	public EntityMagatama(World par1World,
			EntityLivingBase par2EntityLivingBase, ItemStack itemStack) {
		super(par1World, par2EntityLivingBase);
		this.itemStack = itemStack;
		setItemStack(itemStack);
	}

	public Icon getThrowableIcon() {
		return this.getItemStack().getIconIndex();
	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		this.motionX = this.motionY = this.motionZ = 0;
		this.posY+=0.25;
		count = 60;
		isImpacted = true;
	}

	@Override
	public void onUpdate() {
		if (isImpacted) {
			if (--count < 0) {
				if (!worldObj.isRemote) {
					Class cls = ItemMagatama.getMagatama(getItemStack().getItemDamage()).getEffectClass();
					if(cls!=null){
						Entity e=null;
						try {
							e = (Entity) cls.getConstructor(World.class).newInstance(worldObj);
						} catch (Exception e1) {e1.printStackTrace();
						}
						e.setPosition(this.posX, this.posY, this.posZ);
						worldObj.spawnEntityInWorld(e);
					}
				}
				this.setDead();
			} else {
				motionY = 0.05;
			}
		}
		super.onUpdate();
	}
	public int getColor(){
		return ItemMagatama.getMagatama(getItemStack().getItemDamage()).getColor();
	}
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(ICON, new ItemStack(0, 0, 0));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		if (itemStack != null) {
			itemStack.writeToNBT(par1NBTTagCompound);
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		itemStack = new ItemStack(0, 0, 0);
		itemStack.readFromNBT(par1NBTTagCompound);
		setItemStack(itemStack);
	}

	private void setItemStack(ItemStack itemStack) {
		this.dataWatcher.updateObject(ICON, itemStack);
	}

	private ItemStack getItemStack() {
		return this.dataWatcher.getWatchableObjectItemStack(ICON);
	}
}
