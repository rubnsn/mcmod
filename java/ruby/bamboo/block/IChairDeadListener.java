package ruby.bamboo.block;

import net.minecraft.world.World;

public interface IChairDeadListener {
    public void onChairDead(World world, int posX, int posY, int posZ);
}
