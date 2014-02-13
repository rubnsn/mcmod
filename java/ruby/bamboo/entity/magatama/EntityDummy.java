package ruby.bamboo.entity.magatama;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;

public class EntityDummy extends Entity {
    private Entity entity;

    public EntityDummy(World par1World) {
        super(par1World);
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        this.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
        dataWatcher.updateObject(10, EntityList.getEntityString(entity));
        ItemStack is = new ItemStack(Blocks.dirt);
        is.stackTagCompound = new NBTTagCompound();
        entity.writeToNBT(is.stackTagCompound);
        dataWatcher.updateObject(11, is);
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public void onUpdate() {
        //サーバーとの同期タイミング次第では、クライアントのentityがnullになる対策
        if (worldObj.isRemote && entity == null) {
            String name = dataWatcher.getWatchableObjectString(10);
            ItemStack is = dataWatcher.getWatchableObjectItemStack(11);
            if (name != null && is != null) {
                try {
                    entity = (Entity) ((Class) EntityList.stringToClassMapping.get(name)).getConstructor(World.class).newInstance(worldObj);
                } catch (Exception e) {
                    FMLLog.warning("Invalid entity name:" + name);
                }
                if (entity != null) {
                    entity.readFromNBT(is.stackTagCompound);
                }
            }
        }
        //setDead();
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObjectByDataType(10, 4);
        dataWatcher.addObjectByDataType(11, 5);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.setDead();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {

    }
}
