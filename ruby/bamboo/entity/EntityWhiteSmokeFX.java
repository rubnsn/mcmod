package ruby.bamboo.entity;

import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.world.World;

public class EntityWhiteSmokeFX extends EntitySmokeFX {
    public EntityWhiteSmokeFX(World world, double d, double d1, double d2, double d3, double d4, double d5, float f) {
        super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
        particleRed = particleGreen = particleBlue = 1 - (float) (Math.random() * 0.30000001192092896D);
    }
}
