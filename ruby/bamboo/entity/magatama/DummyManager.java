package ruby.bamboo.entity.magatama;

import java.lang.reflect.InvocationTargetException;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class DummyManager {
    public static EntityDummy replace(Entity entity) {
        World world = entity.worldObj;
        EntityDummy dummy = new EntityDummy(world);
        dummy.setEntity(entity);
        entity.setDead();
        world.spawnEntityInWorld(dummy);
        return dummy;
    }

    public static Entity restore(EntityDummy dummy) {
        NBTTagCompound nbt = new NBTTagCompound();
        World world = dummy.worldObj;
        Entity entity = null;
        try {
            entity = dummy.getEntity().getClass().getConstructor(World.class).newInstance(world);
            dummy.getEntity().writeToNBT(nbt);
            entity.readFromNBT(nbt);
            world.spawnEntityInWorld(entity);
            dummy.setDead();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }
}
