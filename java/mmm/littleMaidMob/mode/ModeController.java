package mmm.littleMaidMob.mode;

import java.util.ArrayList;
import java.util.List;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import mmm.littleMaidMob.mode.ai.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.profiler.Profiler;

/**
 * モード管理用クラス
 * 
 */
public class ModeController {
    public ModeController(EntityLittleMaidBase owner) {
        this.owner = owner;
        activeMode = new ArrayList<EntityModeBase>();
    }

    public final EntityLittleMaidBase owner;
    // default AIs
    public EntityAIBase aiSit;
    // AI

    public EntityAITempt aiTempt;
    public LMM_EntityAIBeg aiBeg;
    public LMM_EntityAIBegMove aiBegMove;
    public EntityAIOpenDoor aiOpenDoor;
    public EntityAIRestrictOpenDoor aiCloseDoor;
    public LMM_EntityAIAvoidPlayer aiAvoidPlayer;
    public LMM_EntityAIFollowOwner aiFollow;
    public LMM_EntityAIAttackOnCollide aiAttack;
    public LMM_EntityAIAttackArrow aiShooting;
    public LMM_EntityAICollectItem aiCollectItem;
    public LMM_EntityAIRestrictRain aiRestrictRain;
    public LMM_EntityAIFleeRain aiFreeRain;
    public LMM_EntityAIWander aiWander;
    public LMM_EntityAIJumpToMaster aiJumpTo;
    public LMM_EntityAIFindBlock aiFindBlock;
    public LMM_EntityAITracerMove aiTracer;
    public EntityAISwimming aiSwiming;
    public EntityAIPanic aiPanic;
    public Profiler aiProfiler;

    private List<EntityModeBase> modeList;
    /** 現在実行中のモード */
    private List<EntityModeBase> activeMode;

    public void initModeList() {
        modeList = ModeManager.instance.getModeList(this);
        // AI
        aiBeg = new LMM_EntityAIBeg(this.owner, 8F);
        aiBegMove = new LMM_EntityAIBegMove(this.owner, 1.0F);
        aiOpenDoor = new EntityAIOpenDoor(this.owner, true);
        aiCloseDoor = new EntityAIRestrictOpenDoor(this.owner);
        aiAvoidPlayer = new LMM_EntityAIAvoidPlayer(this.owner, 1.0F, 3);
        aiFollow = new LMM_EntityAIFollowOwner(this.owner, 1.0F, 36D, 25D, 81D);
        aiAttack = new LMM_EntityAIAttackOnCollide(this.owner, 1.0F, true);
        aiShooting = new LMM_EntityAIAttackArrow(this.owner);
        aiCollectItem = new LMM_EntityAICollectItem(this.owner, 1.0F);
        aiRestrictRain = new LMM_EntityAIRestrictRain(this.owner);
        aiFreeRain = new LMM_EntityAIFleeRain(this.owner, 1.0F);
        aiWander = new LMM_EntityAIWander(this.owner, 1.0F);
        aiJumpTo = new LMM_EntityAIJumpToMaster(this.owner);
        aiFindBlock = new LMM_EntityAIFindBlock(this.owner);
        aiSwiming = new LMM_EntityAISwimming(this.owner);
        aiPanic = new EntityAIPanic(this.owner, 2.0F);
        aiTracer = new LMM_EntityAITracerMove(this.owner);
        aiSit = new LMM_EntityAIWait(this.owner);

        // TODO:これいらなくね？いるのかな？？
        aiProfiler = this.owner.worldObj != null && this.owner.worldObj.theProfiler != null ? this.owner.worldObj.theProfiler : null;

        // 動作モード用のTasksListを初期化
        EntityAITasks ltasks[] = new EntityAITasks[2];
        ltasks[0] = new EntityAITasks(aiProfiler);
        ltasks[1] = new EntityAITasks(aiProfiler);

        // default
        ltasks[0].addTask(1, aiSwiming);
        ltasks[0].addTask(2, aiSit);
        ltasks[0].addTask(3, aiJumpTo);
        ltasks[0].addTask(4, aiFindBlock);
        ltasks[0].addTask(6, aiAttack);
        ltasks[0].addTask(7, aiShooting);
        //      ltasks[0].addTask(8, aiPanic);
        ltasks[0].addTask(10, aiBeg);
        ltasks[0].addTask(11, aiBegMove);
        ltasks[0].addTask(20, aiAvoidPlayer);
        ltasks[0].addTask(21, aiFreeRain);
        ltasks[0].addTask(22, aiCollectItem);
        // 移動用AI
        ltasks[0].addTask(30, aiTracer);
        ltasks[0].addTask(31, aiFollow);
        ltasks[0].addTask(32, aiWander);
        ltasks[0].addTask(33, new EntityAILeapAtTarget(this.owner, 0.3F));
        // Mutexの影響しない特殊行動
        ltasks[0].addTask(40, aiCloseDoor);
        ltasks[0].addTask(41, aiOpenDoor);
        ltasks[0].addTask(42, aiRestrictRain);
        // 首の動き単独
        ltasks[0].addTask(51, new EntityAIWatchClosest(this.owner, EntityLivingBase.class, 10F));
        ltasks[0].addTask(52, new EntityAILookIdle(this.owner));
        for (EntityModeBase ieml : activeMode) {
            ieml.addEntityMode(ltasks[0], ltasks[1]);
        }
    }

    /**
     * アクティブモードの変更を試す
     */
    public boolean tryChangeMode(EntityPlayer master) {
        activeMode.clear();
        for (EntityModeBase mode : modeList) {
            if (mode.changeMode(master)) {
                addMode(mode);
                return true;
            }
        }
        //何もなかった
        return false;
    }

    /**
     * メインとなるアクティブモード
     */
    public EntityModeBase getActiveMode() {
        if (this.activeMode.isEmpty()) {
            this.addModeFromName("default");
        }
        return this.activeMode.get(0);
    }

    /**
     * 現在有効なモード一覧
     */
    public List<EntityModeBase> getActiveModeList() {
        if (this.activeMode.isEmpty()) {
            this.addModeFromName("default");
        }
        return this.activeMode;
    }

    /**
     * モード名から追加する
     */
    public EntityModeBase addModeFromName(String pName) {
        EntityModeBase base = ModeManager.instance.createModeInstance(this, pName);
        return addMode(base);
    }

    /**
     * モードインスタンスから追加する
     */
    public EntityModeBase addMode(EntityModeBase mode) {
        this.activeMode.add(mode);
        EntityModeBase subMode = mode.getSubMode();
        //サブモードの追加
        while (subMode != null) {
            this.activeMode.add(mode.getSubMode());
            subMode = subMode.getSubMode();
        }
        return mode;
    }

    public void readModeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("maidmode")) {
            NBTTagList list = nbt.getTagList("maidmodeList", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound inner = list.getCompoundTagAt(i);
                EntityModeBase base = this.addModeFromName(inner.getString("maidmode"));
                if (inner.hasKey("modeNBT")) {
                    base.readEntityFromNBT((NBTTagCompound) inner.getTag("modeNBT"));
                }
            }
        } else {
            this.addModeFromName("default");
        }
    }

    public void writeModeNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (EntityModeBase base : activeMode) {
            NBTTagCompound inner = new NBTTagCompound();
            inner.setString("maidmode", base.getName());
            NBTTagCompound modeNbt = new NBTTagCompound();
            base.writeEntityToNBT(modeNbt);
            inner.setTag("modeNBT", modeNbt);
            list.appendTag(inner);
        }
        nbt.setTag("maidmodeList", list);
    }

    public void addMaidMode(EntityAITasks[] ltasks) {
        initModeList();
    }

}
