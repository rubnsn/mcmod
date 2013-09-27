package ruby.bamboo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.world.World;

import com.google.common.base.Function;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.EntitySpawnPacket;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ClientSpawnHandler  implements Function<EntitySpawnPacket, Entity>
{
    private static ClientSpawnHandler instance = new ClientSpawnHandler();
    public static ClientSpawnHandler getInstance()
    {
        return instance;
    }
    @Override
    public Entity apply(EntitySpawnPacket input)
    {
        return spawnEntity(input.entityId, FMLClientHandler.instance().getClient().theWorld, input.scaledX, input.scaledY, input.scaledZ);
    }

    public Entity spawnEntity(int entityId, World world, double scaledX, double scaledY, double scaledZ)
    {
        Class entityclass = (Class) EntityList.IDtoClassMapping.get(entityId);

        try
        {
            Entity entity = (Entity) entityclass.getConstructor(World.class).newInstance(world);
            entity.entityId = entityId;
            entity.setLocationAndAngles(scaledX, scaledY, scaledZ, 0F, 0F);
            return entity;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
