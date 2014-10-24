package mmm.littleMaidMob.mode.ai;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import mmm.littleMaidMob.mode.EntityModeBase;
import net.minecraft.entity.ai.EntityAITasks;

public class EntityAIEscort extends EntityModeBase {

    public EntityAIEscort(EntityLittleMaidBase pEntity) {
        super(pEntity);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
    }

}
