package ruby.bamboo.item;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.server.FMLServerHandler;
import ruby.bamboo.render.magatama.RenderDummy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemSoulMiller extends Item {
    private static Render defaultPlayerRender = null;
    private static Entity dummyEntity;

    public ItemSoulMiller(int par1) {
        super(par1);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par3World, EntityPlayer par2EntityPlayer) {
        if (par3World.isRemote) {
            if (defaultPlayerRender != null && dummyEntity != null) {
                if (RenderManager.instance.entityRenderMap.containsKey(par2EntityPlayer.getClass())) {
                    RenderManager.instance.entityRenderMap.remove(par2EntityPlayer.getClass());
                    RenderManager.instance.entityRenderMap.put(par2EntityPlayer.getClass(), defaultPlayerRender);
                    dummyEntity = null;
                }
            }
        } else {
            //Entity entity = getPointedEntity(par3World, par2EntityPlayer);
            List<Entity> entityList = par3World.getEntitiesWithinAABBExcludingEntity(par2EntityPlayer, par2EntityPlayer.boundingBox.expand(4F, 2F, 4F));
            if (!entityList.isEmpty()) {
                Entity entity = entityList.get(par3World.rand.nextInt(entityList.size()));
                if (entity != null) {
                    NBTTagCompound nbt = new NBTTagCompound();
                    entity.writeToNBT(nbt);
                    nbt.setString("uniqueEntityName", (String) EntityList.classToStringMapping.get(entity.getClass()));
                    try {
                        PacketDispatcher.sendPacketToAllPlayers(new Packet250CustomPayload("soulMiller", new CompressedStreamTools().compress(nbt)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return par1ItemStack;
    }

    @SideOnly(Side.CLIENT)
    public static void createClientDummyEntity(NBTTagCompound nbt) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (defaultPlayerRender == null) {
            defaultPlayerRender = RenderManager.instance.getEntityClassRenderObject(player.getClass());
        }
        dummyEntity = EntityList.createEntityByName(nbt.getString("uniqueEntityName"), player.worldObj);
        if (dummyEntity != null) {
            dummyEntity.readFromNBT(nbt);
            dummyEntity.yOffset = player.yOffset;
            if (dummyEntity instanceof EntityLiving) {
                ((EntityLiving) dummyEntity).tasks.taskEntries.clear();
            }
            if (RenderManager.instance.entityRenderMap.containsKey(player.getClass())) {
                RenderManager.instance.entityRenderMap.remove(player.getClass());
                RenderManager.instance.entityRenderMap.put(player.getClass(), new RenderDummy(dummyEntity));
            }
        }
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        if (par2World != null && par2World.isRemote && dummyEntity != null && par3Entity != null) {
            dummyEntity.copyLocationAndAnglesFrom(par3Entity);
            dummyEntity.prevPosX = par3Entity.prevPosX;
            dummyEntity.prevPosY = par3Entity.prevPosY;
            dummyEntity.prevPosZ = par3Entity.prevPosZ;
            dummyEntity.prevRotationPitch = par3Entity.prevRotationPitch;
            dummyEntity.prevRotationYaw = par3Entity.prevRotationYaw;
            dummyEntity.fallDistance = par3Entity.fallDistance;
            dummyEntity.onGround = par3Entity.onGround;
            dummyEntity.fallDistance = par3Entity.fallDistance;
            if (dummyEntity instanceof EntityLivingBase && par3Entity instanceof EntityLivingBase) {
                ((EntityLivingBase) dummyEntity).rotationYawHead = ((EntityLivingBase) par3Entity).rotationYawHead;
                ((EntityLivingBase) dummyEntity).prevRotationYawHead = ((EntityLivingBase) par3Entity).prevRotationYawHead;
                ((EntityLivingBase) dummyEntity).isSwingInProgress = ((EntityLivingBase) par3Entity).isSwingInProgress;
                ((EntityLivingBase) dummyEntity).limbSwing = ((EntityLivingBase) par3Entity).limbSwing;
                ((EntityLivingBase) dummyEntity).limbSwingAmount = ((EntityLivingBase) par3Entity).limbSwingAmount;
                ((EntityLivingBase) dummyEntity).prevLimbSwingAmount = ((EntityLivingBase) par3Entity).prevLimbSwingAmount;
                ((EntityLivingBase) dummyEntity).renderYawOffset = ((EntityLivingBase) par3Entity).renderYawOffset;
                ((EntityLivingBase) dummyEntity).prevRenderYawOffset = ((EntityLivingBase) par3Entity).prevRenderYawOffset;
            }

        }
    }

    //直線上のEntityを取得、クライアント側のみ
    private Entity getPointedEntity(World world, EntityPlayer player) {
        double reachDistance = Minecraft.getMinecraft().playerController.getBlockReachDistance();
        Vec3 vec3 = player.getPosition(1.0F);
        Vec3 vec31 = player.getLookVec();
        double d2 = reachDistance;
        if (Minecraft.getMinecraft().objectMouseOver != null) {
            d2 = Minecraft.getMinecraft().objectMouseOver.hitVec.distanceTo(vec3);
        }
        Vec3 vec32 = vec3.addVector(vec31.xCoord * reachDistance, vec31.yCoord * reachDistance, vec31.zCoord * reachDistance);
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.addCoord(vec31.xCoord * reachDistance, vec31.yCoord * reachDistance, vec31.zCoord * reachDistance).expand(1.0, 1.0, 1.0));
        Entity pointedEntity = null;
        for (Entity entity : list) {
            if (entity.canBeCollidedWith()) {
                float f2 = entity.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f2, f2, f2);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (0.0D < d2 || d2 == 0.0D) {
                        pointedEntity = entity;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        if (entity == player.ridingEntity && !entity.canRiderInteract()) {
                            if (d2 == 0.0D) {
                                pointedEntity = entity;
                            }
                        } else {
                            pointedEntity = entity;
                            d2 = d3;
                        }
                    }
                }
            }
        }
        return pointedEntity;
    }
}
