package ruby.bamboo;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ManekiHandler
{
    private final ArrayList<Coordinate> manekiList = new ArrayList<Coordinate>();
    public static ManekiHandler instance = new ManekiHandler();
    private ManekiHandler()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @ForgeSubscribe
    public void livingSpawn(LivingSpawnEvent event)
    {
        if (event.entityLiving instanceof EntityMob)
        {
            if (isManekiRange((int)event.x, (int)event.z))
            {
                event.setResult(Result.DENY);
            }
        }
    }
    public void clearManekiList()
    {
        manekiList.clear();
    }
    public boolean addManeki(int posX, int posZ)
    {
        if (isManekiNotAddRange(posX, posZ))
        {
            manekiList.add(new Coordinate().setPosition(posX, posZ));
            return true;
        }

        return false;
    }
    private boolean isManekiNotAddRange(int posX, int posZ)
    {
        for (Coordinate coord: manekiList)
        {
            if (coord.isEnableRange(posX, posZ, true))
            {
                return false;
            }
        }

        return true;
    }
    public void removeManeki(int posX, int posZ)
    {
        Iterator<Coordinate> itr = manekiList.iterator();

        while (itr.hasNext())
        {
            Coordinate coord = itr.next();

            if (coord.isEnableRange(posX, posZ, false))
            {
                itr.remove();
                break;
            }
        }
    }
    public boolean isManekiRange(int posX, int posZ)
    {
        for (Coordinate coord: manekiList)
        {
            if (coord.isEnableRange(posX, posZ, false))
            {
                return true;
            }
        }

        return false;
    }
    private class Coordinate
    {
        private int posX;
        private int posZ;
        private final static int ENABLE_RANGE = 1;
        Coordinate setPosition(int blockX, int blockZ)
        {
            posX = blockX >> 4;
            posZ = blockZ >> 4;
            return this;
        }
        //
        boolean isEnableRange(int blockX, int blockZ, boolean isPlaceBlock)
        {
            if (isPlaceBlock)
            {
                return Math.abs(this.posX - (blockX >> 4)) <= ENABLE_RANGE * 2 && Math.abs(this.posZ - (blockZ >> 4)) <= ENABLE_RANGE * 2;
            }
            else
            {
                return Math.abs(this.posX - (blockX >> 4)) <= ENABLE_RANGE && Math.abs(this.posZ - (blockZ >> 4)) <= ENABLE_RANGE;
            }
        }
    }
}
