package mmm.littleMaidMob.mode;

import static mmm.littleMaidMob.Statics.dataWatch_Flags_Freedom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mmm.littleMaidMob.RenderLittleMaid;
import mmm.littleMaidMob.Statics;
import mmm.littleMaidMob.littleMaidMob;
import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import mmm.littleMaidMob.mode.ai.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * モード管理用クラス
 * 
 */
public class ModeController implements IExtendedEntityProperties {
    private EntityLittleMaidBase owner;
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
    /** 全モードリスト */
    private List<EntityModeBase> modeList;
    /** 現在実行中のモード */
    private EntityModeBase activeMode;

    /** 待機判定 */
    private boolean maidFreedom;

    public Map<Integer, EntityAITasks[]> maidModeList;
    public Map<String, Integer> maidModeIndexList;
    public int maidMode; // 2Byte
    private boolean velocityChanged;
    private int mstatWorkingInt;
    private String mstatModeName;

    public ModeController(EntityLittleMaidBase entity) {
        this.owner = (EntityLittleMaidBase) entity;
        modeList = ModeManager.instance.getModeList(this);
        activeMode = null;
        maidModeList = new HashMap<Integer, EntityAITasks[]>();
        maidModeIndexList = new HashMap<String, Integer>();
        initModeList();
        mstatModeName = "";
        maidMode = 65535;
        //モードイニシャライズ
        for (EntityModeBase lem : modeList) {
            lem.initEntity();
        }
    }

    public void initModeList() {
        // AI
        aiBeg = new LMM_EntityAIBeg(this.owner, 8F);//完コピ
        aiBegMove = new LMM_EntityAIBegMove(this.owner, 1.0F);//完コピ
        aiOpenDoor = new EntityAIOpenDoor(this.owner, true);
        aiCloseDoor = new EntityAIRestrictOpenDoor(this.owner);
        aiAvoidPlayer = new LMM_EntityAIAvoidPlayer(this.owner, 1.0F, 3);//完コピ
        aiFollow = new LMM_EntityAIFollowOwner(this.owner, 1.0F, 36D, 25D, 81D);//完コピ
        aiAttack = new LMM_EntityAIAttackOnCollide(this.owner, 1.0F, true);//完コピ
        aiShooting = new LMM_EntityAIAttackArrow(this.owner);
        aiCollectItem = new LMM_EntityAICollectItem(this.owner, 1.0F);
        aiRestrictRain = new LMM_EntityAIRestrictRain(this.owner);
        aiFreeRain = new LMM_EntityAIFleeRain(this.owner, 1.0F);
        aiWander = new LMM_EntityAIWander(this.owner, 1.0F);
        aiJumpTo = new LMM_EntityAIJumpToMaster(this.owner);//完コピ
        aiFindBlock = new LMM_EntityAIFindBlock(this.owner);//完コピ
        aiSwiming = new LMM_EntityAISwimming(this.owner);//問題無さそう
        aiPanic = new EntityAIPanic(this.owner, 2.0F);
        aiTracer = new LMM_EntityAITracerMove(this.owner);//完コピ
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
        for (EntityModeBase ieml : modeList) {
            ieml.addEntityMode(ltasks[0], ltasks[1]);
        }
    }

    /**
     * メインとなるアクティブモード
     */
    public EntityModeBase getActiveModeClass() {
        if (this.activeMode == null) {
            this.setMaidMode("Wild");
        }
        return this.activeMode;
    }

    public void addMaidMode(EntityAITasks[] peaiTasks, String pmodeName, int pmodeIndex) {
        maidModeList.put(pmodeIndex, peaiTasks);
        maidModeIndexList.put(pmodeName, pmodeIndex);
    }

    public int getMaidModeInt() {
        return this.maidMode;
    }

    public boolean isActiveModeClass() {
        return activeMode != null;
    }

    public void setActiveModeClass(EntityModeBase pEntityMode) {
        activeMode = pEntityMode;
    }

    private String getMaidModeString(int pindex) {
        for (Entry<String, Integer> entry : maidModeIndexList.entrySet()) {
            if (entry.getValue() == pindex) {
                return entry.getKey();
            }
        }
        return "";
    }

    public boolean callPreInteract(EntityPlayer par1EntityPlayer, ItemStack is) {
        for (EntityModeBase iem : modeList) {
            if (iem.preInteract(par1EntityPlayer, is)) {
                return true;
            }
        }
        return false;
    }

    public boolean callInteract(EntityPlayer par1EntityPlayer, ItemStack is) {
        for (EntityModeBase iem : modeList) {
            if (iem.interact(par1EntityPlayer, is)) {
                return true;
            }
        }
        return false;
    }

    public EntityModeBase callChangeMode(EntityPlayer par1EntityPlayer) {
        for (EntityModeBase iem : modeList) {
            if (iem.changeMode(par1EntityPlayer)) {
                return iem;
            }
        }
        return null;
    }

    public String getDisplayModeName() {
        return owner.isWait() ? "WAIT" : isFreedom() ? "FREEDOM" : mstatModeName;
    }

    public String getModeName() {
        return mstatModeName;
    }

    public boolean setMaidMode(String pname) {
        return setMaidMode(pname, false);
    }

    public boolean setMaidMode(String pname, boolean pplaying) {
        if (!maidModeIndexList.containsKey(pname)) {
            littleMaidMob.Debug("Mode not found", pname);
            return false;
        }
        return setMaidMode(maidModeIndexList.get(pname), pplaying);
    }

    public boolean setMaidMode(int pindex) {
        return setMaidMode(pindex, false);
    }

    public boolean setMaidMode(int pindex, boolean pplaying) {
        // モードに応じてAIを切り替える
        velocityChanged = true;
        if (!maidModeList.containsKey(pindex))
            return false;
        if (maidMode == pindex)
            return true;

        if (pplaying) {

        } else {
            mstatWorkingInt = pindex;
        }
        mstatModeName = getMaidModeString(pindex);
        maidMode = pindex;
        owner.getDataWatcher().updateObject(Statics.dataWatch_Mode, (short) maidMode);
        EntityAITasks[] ltasks = maidModeList.get(pindex);

        // AIを根底から書き換える
        if (ltasks.length > 0 && ltasks[0] != null) {
            setMaidModeAITasks(ltasks[0], owner.tasks);
        } else {
            setMaidModeAITasks(null, owner.tasks);
        }
        if (ltasks.length > 1 && ltasks[1] != null) {
            setMaidModeAITasks(ltasks[1], owner.targetTasks);
        } else {
            setMaidModeAITasks(null, owner.targetTasks);
        }

        // モード切替に応じた処理系を確保
        if (owner.avatar != null) {
            owner.avatar.stopUsingItem();
        }
        owner.setSitting(false);
        owner.setSneaking(false);
        aiJumpTo.setEnable(true);
        //      aiFollow.setEnable(true);
        aiAttack.setEnable(true);
        aiShooting.setEnable(false);
        aiAvoidPlayer.setEnable(true);
        //      aiWander.setEnable(maidFreedom);
        owner.setBloodsuck(false);
        owner.getTileContainer().clearTilePosAll();
        for (EntityModeBase iem : modeList) {
            if (iem.setMode(maidMode)) {
                setActiveModeClass(iem);
                aiFollow.minDist = iem.getRangeToMaster(0);
                aiFollow.maxDist = iem.getRangeToMaster(1);
                break;
            }
        }
        owner.getNextEquipItem();

        return true;
    }

    protected void setMaidModeAITasks(EntityAITasks pTasksSRC, EntityAITasks pTasksDEST) {
        // 既存のAIを削除して置き換える。
        // 動作をクリア
        try {
            ArrayList<EntityAITaskEntry> ltasksDoDEST = (ArrayList<EntityAITaskEntry>) ReflectionHelper.getPrivateValue(EntityAITasks.class, pTasksDEST, 1);
            ArrayList<EntityAITaskEntry> ltasksExeDEST = (ArrayList<EntityAITaskEntry>) ReflectionHelper.getPrivateValue(EntityAITasks.class, pTasksDEST, 2);

            if (pTasksSRC == null) {
                ltasksDoDEST.clear();
                ltasksExeDEST.clear();
            } else {
                ArrayList<EntityAITaskEntry> ltasksDoSRC = (ArrayList<EntityAITaskEntry>) ReflectionHelper.getPrivateValue(EntityAITasks.class, pTasksSRC, 1);
                ArrayList<EntityAITaskEntry> ltasksExeSRC = (ArrayList<EntityAITaskEntry>) ReflectionHelper.getPrivateValue(EntityAITasks.class, pTasksSRC, 2);

                Iterator iterator;
                iterator = ltasksExeDEST.iterator();
                while (iterator.hasNext()) {
                    EntityAITaskEntry ltaskentory = (EntityAITaskEntry) iterator.next();
                    ltaskentory.action.resetTask();
                }
                ltasksExeDEST.clear();

                ltasksDoDEST.clear();
                ltasksDoDEST.addAll(ltasksDoSRC);
                // TODO: 未実装の機能、モードチェンジ時の初期化を行う。
                for (EntityAITaskEntry ltask : ltasksDoSRC) {
                    if (ltask instanceof LMM_IEntityAI) {
                        //                      ((LMM_IEntityAI)ltask).setDefaultEnable();
                    }
                }
            }
        } catch (Exception s) {
            littleMaidMob.Debug("tasks replace rxception");
        }

    }

    // 自由行動
    public void setFreedom(boolean pFlag) {
        // AI関連のリセットもここで。
        maidFreedom = pFlag;
        aiRestrictRain.setEnable(pFlag);
        aiFreeRain.setEnable(pFlag);
        aiWander.setEnable(pFlag);
        //      aiJumpTo.setEnable(!pFlag);
        aiAvoidPlayer.setEnable(!pFlag);
        aiFollow.setEnable(!pFlag);
        aiTracer.setEnable(false);
        //      setAIMoveSpeed(pFlag ? moveSpeed_Nomal : moveSpeed_Max);
        //      setMoveForward(0.0F);

        if (maidFreedom && owner.isContract()) {
            //          func_110171_b(
            owner.setHomeArea(MathHelper.floor_double(owner.posX), MathHelper.floor_double(owner.posY), MathHelper.floor_double(owner.posZ), 16);
        } else {
            //          func_110177_bN();
            owner.detachHome();
            //          setPlayingRole(0);
        }

        owner.setMaidFlags(maidFreedom, dataWatch_Flags_Freedom);
    }

    public boolean isFreedom() {
        return maidFreedom;
    }

    public void showSpecial(RenderLittleMaid renderLittleMaid, double par2, double par4, double par6) {
        for (EntityModeBase iem : modeList) {
            iem.showSpecial(renderLittleMaid, par2, par4, par6);
        }
    }

    public EntityLittleMaidBase getOwner() {
        return owner;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        for (EntityModeBase iem : modeList) {
            iem.writeEntityToNBT(compound);
        }
        compound.setBoolean("Freedom", maidFreedom);
        compound.setString("Mode", mstatModeName);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        for (EntityModeBase iem : modeList) {
            iem.readEntityFromNBT(compound);
        }
        setFreedom(compound.getBoolean("Freedom"));
        setMaidMode(compound.getString("Mode"));
    }

    @Override
    public void init(Entity entity, World world) {

    }

    /** クライアント同期用、サーバーではsetFreedomを使用すること */
    @SideOnly(Side.CLIENT)
    public void setMaidFreedom(boolean bool) {
        maidFreedom = bool;
    }

    public void callOnupdate() {
        for (EntityModeBase iem : modeList) {
            iem.onUpdate(maidMode);
        }
    }

}
