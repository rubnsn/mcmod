package mmm.littleMaidMob.mode;

import mmm.littleMaidMob.mode.ai.EntityAICovet;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;

public class ModeDefault extends EntityModeBase {

    public ModeDefault(ModeController pEntity) {
        super(pEntity);
    }

    @Override
    public String getName() {
        return "default";
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
        pDefaultMove.addTask(0, new EntityAISwimming(owner));
        pDefaultMove.addTask(1, new EntityAIPanic(owner, 1.4D));
        pDefaultMove.addTask(2, new EntityAICovet(owner, 5F));
        pDefaultMove.addTask(5, new EntityAIWander(owner, 1.0D));
        pDefaultMove.addTask(9, new EntityAILookIdle(owner));
    }

}
