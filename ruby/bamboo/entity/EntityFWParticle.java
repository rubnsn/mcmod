package ruby.bamboo.entity;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityFWParticle extends EntityFX
{
    private double rDiff;
    private double gDiff;
    private double bDiff;
    private float size;

    public EntityFWParticle(World world, double x, double y, double z,
                            double r, double g, double b, double rT, double gT, double bT,
                            int maxAge, float particleSize)
    {
        super(world, x, y, z, 0, 0, 0);
        super.particleGravity = -1F;
        this.size = particleSize + (float)((Math.random() - 0.5) / 100);
        setSize(1f, 1f);
        noClip = true;
        particleMaxAge = maxAge;
        motionX = Math.random() / 100;
        motionY = Math.random() / 100;
        motionZ = Math.random() / 100;
        particleRed = (float) r;
        particleGreen = (float) g;
        particleBlue = (float) b;
        rDiff = (rT - r) / maxAge;
        gDiff = (gT - g) / maxAge;
        bDiff = (bT - b) / maxAge;
        setParticleTextureIndex((int)(4 + Math.random() * 4D));
    }

    @Override
    public void renderParticle(Tessellator tessellator, float f, float f1,
                               float f2, float f3, float f4, float f5)
    {
        float f6 = (particleAge + f) / particleMaxAge;
        particleScale = size * (1.0F - f6 * f6 * 0.5F);
        super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
    }

    @Override
    public int getBrightnessForRender(float f)
    {
        if (getBrightness(1.0F) < 0.5)
        {
            return 0x7FFF;
        }

        return super.getBrightnessForRender(f);
    }

    @Override
    public void onUpdate()
    {
        if (inWater)
        {
            setDead();
        }

        if (particleAge >= particleMaxAge)
        {
            setDead();
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        moveEntity(motionX, motionY, motionZ);
        particleAge++;
        particleRed += rDiff;
        particleGreen += gDiff;
        particleBlue += bDiff;
    }
}