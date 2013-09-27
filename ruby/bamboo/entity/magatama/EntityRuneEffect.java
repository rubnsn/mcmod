package ruby.bamboo.entity.magatama;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityRuneEffect extends Entity{
	
	private float ringSize;
	public float roll;
	private float rollSpeed;
	private Entity parent;
	private int colorCode;
	public EntityRuneEffect(World par1World) {
		super(par1World);
		ringSize=1;
		roll=0;
		rollSpeed=0;
	}
	@Override
	public void onUpdate(){
		super.onUpdate();
		if(worldObj.isRemote){
			this.roll=roll<360?roll+rollSpeed:0;
			if(parent==null||parent.isDead){
				this.setDead();
			}
		}
	}
	public void setRingColor(int color){
		colorCode=color;
	}
	public int getRingColor(){
		return colorCode;
	}
	public void setParentEntity(Entity entity){
		parent=entity;
	}

	public void setRollSpeed(float speed){
		rollSpeed=speed;
	}
	public float getRollSpeed(){
		return rollSpeed;
	}

	public void setRingsize(float i) {
		ringSize=i;
		setSize(i, 1);
	}

	public float getRingsize() {
		return ringSize;
	}
	
	public EntityRuneEffect copy(){
		EntityRuneEffect copy=new EntityRuneEffect(worldObj);
		copy.setRingsize(this.ringSize);
		copy.setRollSpeed(this.rollSpeed);
		copy.setParentEntity(this.parent);
		copy.setRingColor(this.colorCode);
		return copy;
	}
	@Override
	public void moveEntity(double par1, double par3, double par5){
	}
	@Override
	protected void entityInit() {	
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {	
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}
	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}
}
