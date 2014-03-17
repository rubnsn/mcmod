package ruby.bamboo.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TelepoterBamboo extends Teleporter {

    public TelepoterBamboo(WorldServer par1WorldServer) {
        super(par1WorldServer);
    }

    @Override
    public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {

    }

    @Override
    public void removeStalePortalLocations(long par1) {

    }

}
