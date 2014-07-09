package ruby.bamboo.entity.magatama;

import java.util.ArrayList;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityThunderStorm extends Entity implements IEntitySelector {
    private int timer;
    private static final int MAX_TIME = 1200;
    private ArrayList<Entity> entitylist = new ArrayList<Entity>();

    public EntityThunderStorm(World par1World) {
        super(par1World);
        this.setSize(1, 1);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote) {
            if (timer++ == 0) {
                this.worldObj.getWorldInfo().setRaining(true);
                this.worldObj.getWorldInfo().setRainTime(MAX_TIME);
                this.worldObj.getWorldInfo().setThundering(true);
                this.worldObj.getWorldInfo().setThunderTime(MAX_TIME);
            } else if (timer >= 60) {
                if (timer % 60 == 0) {
                    entitylist.clear();
                    entitylist.addAll(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(100, 40, 100), this));
                }
                if (timer % 10 == 0 && entitylist.size() > 0) {
                    Entity e = entitylist.get(this.rand.nextInt(entitylist.size()));
                    this.worldObj.updateWeatherBody();
                    this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, e.posX, e.posY, e.posZ));
                    this.worldObj.createExplosion(this, e.posX, e.posY, e.posZ, 5, true);
                }
            }
            if (timer > MAX_TIME) {
                this.setDead();
            }
        }
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {
        setDead();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {
    }

    @Override
    public boolean isEntityApplicable(Entity var1) {
        return var1 instanceof EntityLivingBase && !(var1 instanceof EntityPlayer) && this.worldObj.canLightningStrikeAt((int) var1.posX, (int) var1.posY, (int) var1.posZ);
    }

}
