package mmm.littleMaidMob.mode;

import mmm.littleMaidMob.mode.ai.LMM_EntityAIHurtByTarget;
import mmm.littleMaidMob.mode.ai.LMM_EntityAINearestAttackableTarget;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;

public class EntityModeBlazingstar extends EntityModeBase {

    public EntityModeBlazingstar(ModeController pEntity) {
        super(pEntity);
    }

    @Override
    public String getName() {
        return "Blazingstar";
    }

    @Override
    public int priority() {
        return 3300;
    }

    @Override
    public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
        // Blazingstar:0x00c3
        EntityAITasks[] ltasks2 = new EntityAITasks[2];
        ltasks2[0] = pDefaultMove;
        ltasks2[1] = new EntityAITasks(getOwner().modeController.aiProfiler);

        ltasks2[1].addTask(1, new LMM_EntityAIHurtByTarget(getOwner(), true));
        ltasks2[1].addTask(2, new LMM_EntityAINearestAttackableTarget(getOwner(), EntityLivingBase.class, 0, true));
    }

}
