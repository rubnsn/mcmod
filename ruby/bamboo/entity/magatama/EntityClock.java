package ruby.bamboo.entity.magatama;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.CanUpdate;

public class EntityClock extends Entity{
	private int time;
	private int preTime;
	private int tickRate;
	private int skipTickRate;
	private int effectTime;
	private boolean[] isSkip;
	private boolean isTimeStop;
	private HashSet<Entity> hookedEntitys;
	private HashMap<Entity,NBTTagCompound> hookedEntitysSaveDate;
	public EntityClock(World par1World) {
		super(par1World);
		setSize(4F, 8F);
		preTime=time=(int) ((par1World.getWorldTime()+6000)%12000);
		tickRate=200;
		effectTime=0;
		skipTickRate=1;
		isTimeStop=true;
		hookedEntitys=new HashSet<Entity>();
		hookedEntitysSaveDate=new HashMap<Entity,NBTTagCompound>();
		isSkip=new boolean[20];
		Arrays.fill(isSkip, false);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		time++;
		effectTime++;
		//worldObj.getChunkProvider().
		if(time%10==0&&isTimeStop&&skipTickRate<isSkip.length){
			++skipTickRate;
			Arrays.fill(isSkip, false);
			for(int i=0;i<isSkip.length;i++){
				if(i%(isSkip.length/skipTickRate)==0){
					isSkip[i]=true;
				}
			}
		}
		if(time%10==0){
			hookedEntitys.addAll(worldObj.getEntitiesWithinAABBExcludingEntity(this,this.boundingBox.expand(40, 40, 40)));
		}
		for(Entity e:hookedEntitys){
			bindEntity(e);
		}
		if(effectTime>400){
			for(Entity e:hookedEntitys){
				releaseEntity(e);
			}
			hookedEntitys.clear();
			hookedEntitysSaveDate.clear();
			setDead();
		}
		if(effectTime<20||effectTime>380){
			preTime+=(effectTime%2==0?1:0);
		}

	}
	private void bindEntity(Entity entity){
		if(!hookedEntitysSaveDate.containsKey(entity)){
			NBTTagCompound nbt=new NBTTagCompound();
			entity.writeToNBT(nbt);
			hookedEntitysSaveDate.put(entity,nbt);
		}
		if(!(entity instanceof EntityPlayer)){
			entity.fallDistance=0;
			entity.motionX=0;
			entity.motionY=0;
			entity.motionZ=0;
			NBTTagList pos = hookedEntitysSaveDate.get(entity).getTagList("Pos");
			NBTTagList rotation=hookedEntitysSaveDate.get(entity).getTagList("Rotation");
			entity.rotationYaw=((NBTTagFloat)rotation.tagAt(0)).data;
			entity.rotationPitch=((NBTTagFloat)rotation.tagAt(1)).data;
			entity.prevPosX=entity.posX=entity.lastTickPosX=((NBTTagDouble)pos.tagAt(0)).data;
			entity.prevPosY=entity.posY=entity.lastTickPosY=((NBTTagDouble)pos.tagAt(1)).data;
			entity.prevPosZ=entity.posZ=entity.lastTickPosZ=((NBTTagDouble)pos.tagAt(2)).data;
			if(entity instanceof EntityLivingBase){
				EntityLivingBase entityLiving=((EntityLivingBase)entity);
				entityLiving.rotationYawHead=entityLiving.prevRotationYawHead;
				entityLiving.moveForward=0;
			}
		}
		
	}
	private void releaseEntity(Entity entity){
		if(hookedEntitysSaveDate.containsKey(entity)){
			if(!(entity instanceof EntityPlayer)){
				NBTTagCompound nbt=hookedEntitysSaveDate.get(entity);
				NBTTagList rotation=hookedEntitysSaveDate.get(entity).getTagList("Rotation");
				NBTTagList motion = nbt.getTagList("Motion");
				entity.motionX= ((NBTTagDouble)motion.tagAt(0)).data;
				entity.motionY = ((NBTTagDouble)motion.tagAt(1)).data;
				entity.motionZ = ((NBTTagDouble)motion.tagAt(2)).data;
				entity.rotationYaw=((NBTTagFloat)rotation.tagAt(0)).data;
				entity.rotationPitch=((NBTTagFloat)rotation.tagAt(1)).data;
				entity.fallDistance=nbt.getFloat("FallDistance");
			}
		}
	}
	public int getTime(){
		return preTime;
	}
	@Override
	public void setDead(){
		super.setDead();
	}
	@Override
	public void moveEntity(double par1, double par3, double par5){
	}
	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		setDead();
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}
	@Override
	@SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
    }


}
