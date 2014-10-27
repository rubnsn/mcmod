package mmm.littleMaidMob.mode;

import java.util.ArrayList;
import java.util.List;

import mmm.littleMaidMob.littleMaidMob;
import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import mmm.littleMaidMob.mode.ai.LMM_EntityAIHurtByTarget;
import mmm.littleMaidMob.sound.EnumSound;
import mmm.util.MMM_Helper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

public class ModeBasic extends EntityModeBlockBase {

    public static final int mmode_Wild = 0x0000;
    public static final int mmode_Escorter = 0x0001;

    private IInventory myInventory;
    private IInventory myChest;
    private List<IInventory> fusedTiles;
    private boolean isWorking;
    private double lastdistance;
    private int maidSearchCount;

    /**
     * Wild, Escorter
     */
    public ModeBasic(ModeController pEntity) {
        super(pEntity);
        fusedTiles = new ArrayList<IInventory>();
        //          myTile = null;
    }

    @Override
    public int priority() {
        // TODO Auto-generated method stub
        return 9000;
    }

    @Override
    public void init() {
        /*
        ModLoader.addLocalization("littleMaidMob.mode.Strike", "Strike");
        ModLoader.addLocalization("littleMaidMob.mode.Wait", "Wait");
        ModLoader.addLocalization("littleMaidMob.mode.Wild", "Wild");
        ModLoader.addLocalization("littleMaidMob.mode.Wild", "ja_JP", "野生種");
        ModLoader.addLocalization("littleMaidMob.mode.Escorter", "Escorter");
        ModLoader.addLocalization("littleMaidMob.mode.Escorter", "ja_JP", "従者");
        ModLoader.addLocalization("littleMaidMob.mode.F-Escorter", "Freedom");
        ModLoader.addLocalization("littleMaidMob.mode.D-Escorter", "D-Escorter");
        ModLoader.addLocalization("littleMaidMob.mode.T-Escorter", "Tracer");*/
    }

    @Override
    public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
        // Wild
        EntityAITasks[] ltasks = new EntityAITasks[2];
        ltasks[0] = new EntityAITasks(null);
        ltasks[1] = new EntityAITasks(null);

        ltasks[0].addTask(1, controller.aiSwiming);
        ltasks[0].addTask(2, controller.aiAttack);
        ltasks[0].addTask(3, controller.aiPanic);
        ltasks[0].addTask(4, controller.aiBegMove);
        ltasks[0].addTask(4, controller.aiBeg);
        ltasks[0].addTask(5, controller.aiRestrictRain);
        ltasks[0].addTask(6, controller.aiFreeRain);
        //            ltasks[0].addTask(4, new EntityAIMoveIndoors(this));
        //          ltasks[0].addTask(7, getOwner().aiCloseDoor);
        //          ltasks[0].addTask(8, getOwner().aiOpenDoor);
        ltasks[0].addTask(9, controller.aiCollectItem);
        ltasks[0].addTask(10, new EntityAILeapAtTarget(getOwner(), 0.3F));
        ltasks[0].addTask(11, controller.aiWander);
        ltasks[0].addTask(12, new EntityAIWatchClosest2(getOwner(), EntityLivingBase.class, 10F, 0.02F));
        ltasks[0].addTask(13, new EntityAIWatchClosest2(getOwner(), EntityLittleMaidBase.class, 10F, 0.02F));
        ltasks[0].addTask(13, new EntityAIWatchClosest2(getOwner(), EntityPlayer.class, 10F, 0.02F));
        ltasks[0].addTask(13, new EntityAILookIdle(getOwner()));

        ltasks[1].addTask(1, new LMM_EntityAIHurtByTarget(getOwner(), false));

        controller.addMaidMode(ltasks, "Wild", mmode_Wild);

        // Escorter:0x0001
        ltasks = new EntityAITasks[2];
        ltasks[0] = pDefaultMove;
        ltasks[1] = pDefaultTargeting;
        controller.addMaidMode(ltasks, "Escorter", mmode_Escorter);

    }

    @Override
    public boolean changeMode(EntityPlayer pentityplayer) {
        // 強制的に割り当てる
        getOwner().setMaidMode("Escorter");
        return true;
    }

    @Override
    public boolean setMode(int pMode) {
        switch (pMode) {
        case mmode_Wild:
            getOwner().setFreedom(true);
            //              getOwner().aiWander.setEnable(true);
            return true;
        case mmode_Escorter:
            controller.aiAvoidPlayer.setEnable(false);
            /*
            for (int li = 0; li < getOwner().mstatSwingStatus.length; li++) {
                getOwner().setEquipItem(li, -1);
            }*/
            return true;
        }
        //          getOwner().getNavigator().clearPathEntity()
        return false;
    }

    @Override
    public int getNextEquipItem(int pMode) {
        return pMode == mmode_Wild ? 0 : -1;
    }

    @Override
    public boolean checkItemStack(ItemStack pItemStack) {
        return true;
    }

    @Override
    public boolean isSearchBlock() {
        if (controller.getMaidModeInt() == mmode_Escorter && getOwner().isFreedom() && getOwner().inventory.getFirstEmptyStack() == -1) {
            // 対象をまだ見つけていないときは検索を行う。
            fDistance = 100F;
            return myInventory == null;
        }
        //          clearMy();
        //          fusedTiles.clear();
        return false;
    }

    @Override
    public boolean shouldBlock(int pMode) {
        return myInventory instanceof TileEntity;
    }

    @Override
    public boolean checkBlock(int pMode, int px, int py, int pz) {
        TileEntity ltile = getOwner().worldObj.getTileEntity(px, py, pz);
        if (!(ltile instanceof IInventory)) {
            return false;
        }
        if (((IInventory) ltile).getSizeInventory() < 18) {
            // インベントリのサイズが１８以下なら対象としない。
            return false;
        }

        // 世界のメイドから
        if (checkWorldMaid(ltile))
            return false;
        // 使用済みチェック
        if (fusedTiles.contains(ltile)) {
            // 既に通り過ぎた場所よッ！
            return false;
        }

        double ldis = getOwner().getTileContainer().getDistanceTilePosSq(ltile);
        if (fDistance > ldis) {
            myInventory = (IInventory) ltile;
            fDistance = ldis;
        }
        return false;
    }

    @Override
    public boolean overlooksBlock(int pMode) {
        // チェストカートの検索
        List<Entity> list = getOwner().worldObj.getEntitiesWithinAABB(IInventory.class, getOwner().boundingBox.expand(8D, 2D, 8D));
        double cartl = 256D;
        for (Entity lentity : list) {
            if (!fusedTiles.contains(lentity)) {
                if (((IInventory) lentity).getSizeInventory() < 18) {
                    // インベントリが一定サイズ以下はスキップ
                    continue;
                }
                double lr = lentity.getDistanceSqToEntity(getOwner());
                // 見える位置にある最も近い調べていないカートチェスト

                if (fDistance > lr && getOwner().getEntitySenses().canSee(lentity)) {
                    myInventory = (IInventory) lentity;
                    fDistance = lr;
                }
            }
        }
        lastdistance = -1;
        myChest = null;
        maidSearchCount = 0;
        if (myInventory instanceof TileEntity) {
            getOwner().getTileContainer().setTilePos((TileEntity) myInventory);
            return myInventory != null;
        } else {
            getOwner().setTarget((Entity) myInventory);
            return false;
        }
        //          return myInventory != null;
    }

    @Override
    public void startBlock(int pMode) {
    }

    @Override
    public void resetBlock(int pMode) {
        clearMy();
        //          fusedTiles.clear();
    }

    protected void clearMy() {
        myInventory = null;
        if (myChest != null) {
            myChest.closeInventory();
            myChest = null;
        }
        getOwner().getTileContainer().clearTilePos();
        getOwner().setTarget(null);
    }

    @Override
    public boolean executeBlock(int pMode, int px, int py, int pz) {
        //          isMaidChaseWait = true;
        if (myInventory instanceof TileEntityChest) {
            // ブロック系のチェスト
            TileEntityChest lchest = (TileEntityChest) myInventory;
            if (!lchest.isInvalid()) {
                // 使用直前に可視判定
                if (MMM_Helper.canBlockBeSeen(getOwner(), lchest.xCoord, lchest.yCoord, lchest.zCoord, false, true)) {
                    if (myChest == null) {
                        getChest();
                        if (myChest != null) {
                            myChest.openInventory();
                        } else {
                            // 開かないチェスト
                            myInventory = null;
                        }
                    }
                    // チェストに収納
                    getOwner().setWorking(true);
                    putChest();
                    return true;
                } else {
                    // 見失った
                    clearMy();
                }
            } else {
                // Tileの消失
                clearMy();
            }
        } else {
            // 想定外のインベントリ
            if (myInventory != null) {
                fusedTiles.add(myInventory);
            }
            clearMy();
        }
        return false;
    }

    @Override
    public boolean outrangeBlock(int pMode, int pX, int pY, int pZ) {
        // チェストまでのパスを作る
        boolean lf = false;
        if (!getOwner().isMaidWaitEx()) {
            double distance;
            if (myInventory instanceof TileEntity) {
                distance = getOwner().getTileContainer().getDistanceTilePos();
                if (distance == lastdistance) {
                    // TODO:現状無意味
                    // 移動が固まらないように乱数加速
                    littleMaidMob.Debug("Assert.");
                    //getOwner().updateWanderPath();
                    lf = true;
                } else {
                    //lf = MMM_Helper.setPathToTile(getOwner(), (TileEntity) myInventory, false);
                }
            } else {
                distance = 0;
            }
            lastdistance = distance;
            // レンジ外のチェストは閉じる
            if (myChest != null) {
                myChest.closeInventory();
                myChest = null;
            }
        }
        return lf;
    }

    @Override
    public void farrangeBlock() {
        super.farrangeBlock();
        clearMy();
    }

    protected boolean getChest() {
        // チェストを獲得
        if (myInventory == null) {
            return false;
        }
        // 検索済みにスタック
        fusedTiles.add(myInventory);
        if (myInventory instanceof TileEntityChest) {
            TileEntityChest lchest = (TileEntityChest) myInventory;
            if (!lchest.adjacentChestChecked) {
                lchest.checkForAdjacentChests();
            }
            fusedTiles.add(lchest.adjacentChestXNeg);
            fusedTiles.add(lchest.adjacentChestXPos);
            fusedTiles.add(lchest.adjacentChestZNeg);
            fusedTiles.add(lchest.adjacentChestZPos);
        }

        TileEntity ltile = (TileEntity) myInventory;
        Block lblock = getOwner().worldObj.getBlock(ltile.xCoord, ltile.yCoord, ltile.zCoord);
        myChest = myInventory;
        if (lblock instanceof BlockChest) {
            myChest = ((BlockChest) lblock).func_149951_m(getOwner().worldObj, ltile.xCoord, ltile.yCoord, ltile.zCoord);
            //myChest = ((BlockChest) lblock).getInventory(getOwner().worldObj, ltile.xCoord, ltile.yCoord, ltile.zCoord);
        }

        return myChest != null;
    }

    protected void putChest() {
        // チェストに近接
        if (getOwner().getSwingStatus().getSwingStatusDominant().canAttack() && myChest != null) {
            // 砂糖、時計、被っているヘルム以外のアイテムを突っ込む
            ItemStack is;
            littleMaidMob.Debug(String.format("getChest:%d", maidSearchCount));
            while ((is = getOwner().inventory.getStackInSlot(maidSearchCount)) == null && maidSearchCount < getOwner().inventory.mainInventory.length) {
                maidSearchCount++;
            }
            if (is != null && !(is.getItem() == Items.sugar || is.getItem() == Items.clock || (is == getOwner().inventory.armorItemInSlot(3))
            //                      || (is.getItem() instanceof ItemArmor && ((ItemArmor)is.getItem()).armorType == 0)
            )) {
                //                  mod_littleMaidMob.Debug("getchest2.");
                boolean f = false;
                for (int j = 0; j < myChest.getSizeInventory() && is.stackSize > 0; j++) {
                    ItemStack isc = myChest.getStackInSlot(j);
                    if (isc == null) {
                        //                          mod_littleMaidMob.Debug(String.format("%s -> NULL", is.getItemName()));
                        myChest.setInventorySlotContents(j, is.copy());
                        is.stackSize = 0;
                        f = true;
                        break;
                    } else if (isc.isStackable() && isc.isItemEqual(is)) {
                        //                          mod_littleMaidMob.Debug(String.format("%s -> %s", is.getItemName(), isc.getItemName()));
                        f = true;
                        isc.stackSize += is.stackSize;
                        if (isc.stackSize > isc.getMaxStackSize()) {
                            is.stackSize = isc.stackSize - isc.getMaxStackSize();
                            isc.stackSize = isc.getMaxStackSize();
                        } else {
                            is.stackSize = 0;
                            break;
                        }
                    }
                }
                if (is.stackSize <= 0) {
                    getOwner().inventory.setInventorySlotContents(maidSearchCount, null);
                }
                if (f) {
                    getOwner().playSound("random.pop");
                    getOwner().getSwingStatus().setSwing(2, EnumSound.Null);
                }
            }
            //              mod_littleMaidMob.Debug(String.format("getchest3:%d", maidSearchCount));
            if (++maidSearchCount >= getOwner().inventory.mainInventory.length) {
                // 検索済みの対象をスタック
                //                  serchedChest.add(myChest);
                clearMy();
                lastdistance = 0D;
                littleMaidMob.Debug("endChest.");
                // 空きができたら捜索終了
                if (getOwner().inventory.getFirstEmptyStack() > -1) {
                    littleMaidMob.Debug("Search clear.");
                    fusedTiles.clear();
                }
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(int pMode, Entity pEntity) {
        if (pEntity == myInventory) {
            // チェスト付カートとか
            Entity lentity = (Entity) myInventory;
            if (!lentity.isDead) {
                if (getOwner().getDistanceSqToEntity(lentity) < 5D) {
                    getOwner().getNavigator().clearPathEntity();
                    if (myChest == null) {
                        myChest = (IInventory) lentity;
                        fusedTiles.add(myChest);
                        myChest.openInventory();
                    }
                    if (myChest != null) {
                        getOwner().getLookHelper().setLookPositionWithEntity(lentity, 30F, 40F);
                    }
                    // チェストに収納
                    putChest();
                } else {
                    // チェストまでのパスを作る
                    if (!getOwner().isMaidWaitEx()) {
                        double distance = getOwner().getDistanceSqToEntity(lentity);
                        if (distance == lastdistance) {
                            // TODO: 現状無意味
                            littleMaidMob.Debug("Assert.");
                            getOwner().updateWanderPath();
                        } else {
                            getOwner().getNavigator().tryMoveToXYZ(lentity.posX, lentity.posY, lentity.posZ, 1.0F);
                        }
                        lastdistance = distance;
                        //                          mod_littleMaidMob.Debug(String.format("Rerute:%b", hasPath()));
                        if (myChest != null) {
                            myChest.closeInventory();
                            myChest = null;
                        }
                    }
                }
            } else {
                // Entityの死亡
                clearMy();
            }
            return true;
        } else {
            // ターゲットが変わってる？
            clearMy();
        }
        return true;
    }

    @Override
    public boolean isChangeTartget(Entity pTarget) {
        if (pTarget instanceof IInventory) {
            return false;
        }
        return super.isChangeTartget(pTarget);
    }

    @Override
    public boolean preInteract(EntityPlayer pentityplayer, ItemStack pitemstack) {
        // しゃがみ時は処理無効
        if (pentityplayer.isSneaking()) {
            return false;
        }
        if (getOwner().isContract()) {
            // 契約状態
            if (getOwner().isEntityAlive() && getOwner().isMaidContractOwner(pentityplayer)) {
                if (pitemstack != null) {
                    // 追加分の処理
                    getOwner().setPathToEntity(null);
                    if (getOwner().isRemainsContract()) {
                        if (pitemstack.getItem() instanceof ItemAppleGold) {
                            // ゴールデンアッポー
                            if (!getOwner().worldObj.isRemote) {
                                ((ItemAppleGold) pitemstack.getItem()).onEaten(pitemstack, getOwner().worldObj, getOwner().avatar);
                            }
                            MMM_Helper.decPlayerInventory(pentityplayer, -1, 1);
                            return true;
                        } else if (pitemstack.getItem() instanceof ItemBucketMilk && !getOwner().getActivePotionEffects().isEmpty()) {
                            // 牛乳に相談だ
                            if (!getOwner().worldObj.isRemote) {
                                getOwner().clearActivePotions();
                            }
                            MMM_Helper.decPlayerInventory(pentityplayer, -1, 1);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "basic";
    }

}
