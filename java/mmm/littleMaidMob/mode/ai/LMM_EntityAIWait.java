package mmm.littleMaidMob.mode.ai;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.entity.ai.EntityAISit;

public class LMM_EntityAIWait extends EntityAISit {

    public EntityLittleMaidBase theMaid;

    public LMM_EntityAIWait(EntityLittleMaidBase pEntity) {
        super(pEntity);
        this.setMutexBits(5);

        theMaid = pEntity;
    }

    @Override
    public boolean shouldExecute() {
        return theMaid.isMaidWaitEx() || (!theMaid.isFreedom() && theMaid.mstatMasterEntity == null);
    }

}
