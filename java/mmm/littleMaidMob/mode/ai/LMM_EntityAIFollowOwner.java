package mmm.littleMaidMob.mode.ai;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;

public class LMM_EntityAIFollowOwner extends EntityAIBase implements
        LMM_IEntityAI {

    private EntityLittleMaidBase theMaid;
    private Entity theOwner;
    private World theWorld;
    private float moveSpeed;
    private PathNavigate petPathfinder;
    private int field_48310_h;
    public double maxDist;
    public double minDist;
    protected double sprintDist;
    protected double toDistance;
    private boolean lastAvoidWater;
    private boolean isEnable;

    public LMM_EntityAIFollowOwner(EntityLittleMaidBase par1EntityLittleMaid, float pSpeed, double pMin, double pMax, double pSprintDistSQ) {
        theMaid = par1EntityLittleMaid;
        theWorld = par1EntityLittleMaid.worldObj;
        moveSpeed = pSpeed;
        petPathfinder = par1EntityLittleMaid.getNavigator();
        minDist = pMin;
        maxDist = pMax;
        sprintDist = pSprintDistSQ;
        isEnable = true;
        setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (!isEnable)
            return false;

        Entity entityliving = theMaid.getOwner();
        if (entityliving == null) {
            return false;
        }

        if (theMaid.isSitting()) {
            return false;
        }

        toDistance = theMaid.getDistanceSqToEntity(entityliving);
        if (toDistance < minDist) {
            return false;
        } else {
            theOwner = entityliving;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
        toDistance = theMaid.getDistanceSqToEntity(theOwner);
        return !petPathfinder.noPath() && toDistance > maxDist && !theMaid.isSitting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        field_48310_h = 0;
        lastAvoidWater = petPathfinder.getAvoidsWater();
        petPathfinder.setAvoidsWater(false);
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {
        theMaid.setSprinting(false);
        theOwner = null;
        petPathfinder.clearPathEntity();
        petPathfinder.setAvoidsWater(lastAvoidWater);
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask() {
        theMaid.getLookHelper().setLookPositionWithEntity(theOwner, 10F, theMaid.getVerticalFaceSpeed());

        if (theMaid.isSitting()) {
            return;
        }
        // 指定距離以上ならダッシュ
        theMaid.setSprinting(toDistance > sprintDist);
        if (--field_48310_h > 0) {
            return;
        }

        field_48310_h = 10;
        petPathfinder.tryMoveToEntityLiving(theOwner, moveSpeed);
    }

    @Override
    public void setEnable(boolean pFlag) {
        isEnable = pFlag;
    }

    @Override
    public boolean getEnable() {
        return isEnable;
    }

}
