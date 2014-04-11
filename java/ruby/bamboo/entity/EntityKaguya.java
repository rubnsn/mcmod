package ruby.bamboo.entity;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.World;

public class EntityKaguya extends EntityVillager {
    public EntityKaguya(World par1World) {
        super(par1World);
        this.setDead();
    }

    public EntityKaguya(World par1World, int par2) {
        super(par1World, par2);
        this.setDead();
    }

    @Override
    protected String getLivingSound() {
        return null;
    }

    @Override
    protected String getHurtSound() {
        return "game.neutral.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "game.neutral.die";
    }
}
