package mmm.littleMaidMob.entity;

import static mmm.littleMaidMob.Statics.*;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import mmm.lib.multiModel.MultiModelManager;
import mmm.lib.multiModel.model.IModelCaps;
import mmm.lib.multiModel.texture.IMultiModelEntity;
import mmm.lib.multiModel.texture.MultiModelData;
import mmm.littleMaidMob.Counter;
import mmm.littleMaidMob.MaidCaps;
import mmm.littleMaidMob.SwingController;
import mmm.littleMaidMob.SwingController.SwingStatus;
import mmm.littleMaidMob.TileContainer;
import mmm.littleMaidMob.littleMaidMob;
import mmm.littleMaidMob.inventory.InventoryLittleMaid;
import mmm.littleMaidMob.mode.EntityModeBase;
import mmm.littleMaidMob.mode.ModeController;
import mmm.littleMaidMob.sound.EnumSound;
import mmm.util.MMM_Helper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//public class EntityLittleMaidMob extends EntityCreature implements IAnimals, IEntityOwnable {
public class EntityLittleMaidBase extends EntityTameable implements
        IMultiModelEntity {

    //	protected static final UUID maidUUID = UUID.nameUUIDFromBytes("net.minecraft.src.littleMaidMob".getBytes());
    protected static final UUID maidUUID = UUID.fromString("e2361272-644a-3028-8416-8536667f0efb");
    //	protected static final UUID maidUUIDSneak = UUID.nameUUIDFromBytes("net.minecraft.src.littleMaidMob.sneak".getBytes());
    protected static final UUID maidUUIDSneak = UUID.fromString("5649cf91-29bb-3a0c-8c31-b170a1045560");
    //protected static AttributeModifier attCombatSpeed = (new AttributeModifier(maidUUID, "Combat speed boost", 0.07D, 0)).setSaved(false);
    //protected static AttributeModifier attAxeAmp = (new AttributeModifier(maidUUID, "Axe Attack boost", 0.5D, 1)).setSaved(false);
    //protected static AttributeModifier attSneakingSpeed = (new AttributeModifier(maidUUIDSneak, "Sneking speed ampd", -0.4D, 2)).setSaved(false);

    public EntityLittleMaidAvatar avatar;
    public InventoryLittleMaid inventory;
    //	public MultiModelContainer multiModel;
    //	public int color;
    public MultiModelData multiModel;

    /** 契約限界時間 */
    public int maidContractLimit;
    /** 上司の識別 */
    public EntityLivingBase keeperEntity;
    /** 待機状態 */
    protected boolean maidWait;
    protected int mstatWaitCount;
    /** 動作状態 */
    public short maidMode;

    /** 文字しているモードの管理用クラス */
    public ModeController modeController;
    public IModelCaps modelCaps;

    /** 処理対象となるブロック群 */
    private TileContainer tiles;
    /** ほーむせかい */
    public int homeWorld;
    /** 腕振り関係 */
    public SwingController swingController;
    /** おしごとかうんたー */
    private Counter mstatWorkingCount;
    // 首周り
    private boolean looksWithInterest;
    private boolean looksWithInterestAXIS;
    private float rotateAngleHead; // Angle
    private float prevRotateAngleHead; // prevAngle
    /** あれは砂糖 */
    protected boolean mstatLookSuger;
    /** ベース移動速度 */
    double baseMoveSpeed = 0.3D;
    /** 動的なもの */
    public EntityPlayer mstatMasterEntity; // 主
    public double mstatMasterDistanceSq; // 主との距離、計算軽量化用
    protected boolean mstatBloodsuck;
    protected boolean mstatClockMaid;
    // マスク判定
    protected int mstatMaskSelect;
    // 追加の頭部装備
    protected boolean mstatCamouflage;
    protected boolean mstatPlanter;

    public EntityLittleMaidBase(World par1World) {
        super(par1World);
        this.setSize(0.6F, 2.8F);

        inventory = new InventoryLittleMaid(this);
        if (par1World instanceof WorldServer) {
            avatar = new EntityLittleMaidAvatar((WorldServer) par1World, new GameProfile(null, "littleMaidMob"));
            avatar.setOwner(this);
            avatar.inventory = inventory;
        }
        swingController = new SwingController(this);
        registerExtendedProperties("swingController", swingController);
        modeController = new ModeController(this);
        registerExtendedProperties("modeController", modeController);
        tiles = new TileContainer(this);
        registerExtendedProperties("maidTiles", tiles);
        mstatWorkingCount = new Counter(11, 10, -10);
        //		multiModel = MultiModelManager.instance.getMultiModel("MMM_SR2");
        //		setModel("MMM_Aug");

        initMultiModel();
        // TODO 付けないと無限落下する・・・意味解らん
        //		setSize(width, height);
        //		setScale(1.0F);	// ダメ
        //		moveEntity(posX, posY, posZ);	// ダメ
    }

    /** HPや速度みたいなデータみたいな？ */
    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        //ここで設定しても勝手に上書きされて機能してない、糞仕様死ね
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(baseMoveSpeed);
    }

    // 初期化関数群

    @Override
    protected void entityInit() {
        super.entityInit();
        /*
         * DataWatcherはクライアントからサーバーへは値を渡さない、渡せない。
         */

        // 使用中リスト
        // 0:Flags
        // 1:Air
        // 2, 3, 4, 5,
        // 6: HP
        // 7, 8:PotionMap
        // 9: ArrowCount
        // 10: 固有名称
        // 11: 名付判定
        // 12: GrowingAge
        // 16: Tame(4), Sit(1) 
        // 17: ownerName

        // maidAvater用EntityPlayer互換変数
        // 17 -> 18
        // 18 : Absoption効果をクライアント側へ転送するのに使う（拡張HP）
        dataWatcher.addObject(dataWatch_Absoption, Float.valueOf(0.0F));

        // 独自分
        // 19:maidColor
        dataWatcher.addObject(dataWatch_Color, Byte.valueOf((byte) 0));
        // 20:選択テクスチャインデックス
        dataWatcher.addObject(dataWatch_Texture, Integer.valueOf(0));
        // 21:モデルパーツの表示フラグ
        dataWatcher.addObject(dataWatch_Parts, Integer.valueOf(0));
        // 22:状態遷移フラグ群(32Bit)、詳細はStatics参照
        dataWatcher.addObject(dataWatch_Flags, Integer.valueOf(0));
        // 23:GotchaID
        dataWatcher.addObject(dataWatch_Gotcha, Integer.valueOf(0));
        // 24:メイドモード
        dataWatcher.addObject(dataWatch_Mode, Short.valueOf((short) 0));
        // 25:利き腕
        dataWatcher.addObject(dataWatch_DominamtArm, Byte.valueOf((byte) 0));
        // 26:アイテムの使用判定
        dataWatcher.addObject(dataWatch_ItemUse, Integer.valueOf(0));
        // 27:保持経験値
        dataWatcher.addObject(dataWatch_ExpValue, Integer.valueOf(0));

        // TODO:test
        // 31:自由変数、EntityMode等で使用可能な変数。
        dataWatcher.addObject(dataWatch_Free, new Integer(0));
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
        // 別に通常のスポーンでも呼ばれる。
        // 個体値は持たせないのでsuperしない。
        //		multiModel = MultiModelManager.instance.getMultiModel("MMM_SR2");
        multiModel.setColor(0x08);
        setModel("MMM_Aug");
        //		multiModel.forceChanged(true);
        return par1EntityLivingData;
    }

    // 固有値関数群

    @Override
    protected Item getDropItem() {
        // ドロップは砂糖
        return Items.sugar;
    }

    @Override
    public boolean getCanSpawnHere() {
        // TODO Auto-generated method stub
        return super.getCanSpawnHere();
    }

    @Override
    public EntityAgeable createChild(EntityAgeable var1) {
        // TODO Auto-generated method stub
        return null;
    }

    // 形態形成場

    public boolean setModel(String pName) {
        multiModel.setModelFromName(pName);
        //		AbstractModelBase lamb = multiModel.model.getModelClass(multiModel.getColor())[0];
        //		setScale(0.1F);
        //		setSize(lamb.getWidth(modelCaps), lamb.getHeight(modelCaps));
        //		setScale(1.0F);
        //		littleMaidMob.Debug("setSize:%f,  %f - %s", width, height, isClientWorld() ? "server" : "client");
        return MultiModelManager.instance.isMultiModel(pName);
    }

    private void setColor(int i) {
        multiModel.setColor(i);
    }

    // 契約関係

    @Override
    public String getOwnerName() {
        return getOwner() != null ? getOwner().getCommandSenderName() : null;
    }

    /** ますたーの設定 */
    public void setOwner(String uuid) {
        super.func_152115_b(uuid);
    }

    @Override
    public boolean isTamed() {
        return isContract();
    }

    /**
     * 契約済みか？
     * 
     * @return
     */
    public boolean isContract() {
        return super.isTamed();
    }

    public boolean isContractEX() {
        return isContract() && isRemainsContract();
    }

    @Override
    public void setTamed(boolean par1) {
        setContract(par1);
    }

    //	@Override
    public void setContract(boolean flag) {
        super.setTamed(flag);
    }

    @Override
    public void onUpdate() {
        int litemuse = 0;
        //ここでベース速度を変えると問題なく動作する、糞仕様死ね
        if (this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() != baseMoveSpeed) {
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(baseMoveSpeed);
        }

        //ますたーきょり
        mstatMasterEntity = getMaidMasterEntity();
        if (mstatMasterEntity != null) {
            mstatMasterDistanceSq = getDistanceSqToEntity(mstatMasterEntity);
        }

        if (getAttackTarget() != null || getEntityToAttack() != null) {
            setWorking(true);
        }

        if (worldObj.isRemote) {
            // クライアント側
            /*            boolean lupd = false;
                        lupd |= updateMaidContract();
                        lupd |= updateMaidColor();
            //          lupd |= updateTexturePack();
                        updateTexturePack();
                        if (lupd) {
                            setTextureNames();
                        }*/
            modeController.setMaidMode(dataWatcher.getWatchableObjectShort(dataWatch_Mode));
            swingController.setDominantArm(dataWatcher.getWatchableObjectByte(dataWatch_DominamtArm));

            updateMaidFlagsClient();

            // 腕の挙動関連
            litemuse = dataWatcher.getWatchableObjectInt(dataWatch_ItemUse);
            for (int li = 0; li < swingController.mstatSwingStatus.length; li++) {
                ItemStack lis = swingController.mstatSwingStatus[li].getItemStack(this);
                if ((litemuse & (1 << li)) > 0 && lis != null) {
                    swingController.mstatSwingStatus[li].setItemInUse(lis, lis.getMaxItemUseDuration(), this);
                } else {
                    swingController.mstatSwingStatus[li].stopUsingItem(this);
                }
            }
        } else {
            boolean lf;
            // サーバー側
            this.updateRemainsContract();

            // Working!
            lf = mstatWorkingCount.isEnable();
            if (getMaidFlags(dataWatch_Flags_Working) != lf) {
                setMaidFlags(lf, dataWatch_Flags_Working);
            }
            // 拗ねる
            if (!isContractEX() && !modeController.isFreedom()) {
                modeController.setFreedom(true);
                setMaidWait(false);
            }
        }
        modeController.callOnUpdate();
        super.onUpdate();
        // カウンタ系
        if (mstatWaitCount > 0) {
            if (hasPath()) {
                mstatWaitCount = 0;
            } else {
                mstatWaitCount--;
            }
        }

        // くびかしげ    
        prevRotateAngleHead = rotateAngleHead;
        if (getLooksWithInterest()) {
            rotateAngleHead = rotateAngleHead + (1.0F - rotateAngleHead) * 0.4F;
            numTicksToChaseTarget = 10;
        } else {
            rotateAngleHead = rotateAngleHead + (0.0F - rotateAngleHead) * 0.4F;
            if (numTicksToChaseTarget > 0)
                numTicksToChaseTarget--;
        }
        // SwingUpdate
        SwingStatus lmss1 = swingController.getSwingStatusDominant();
        prevSwingProgress = lmss1.prevSwingProgress;
        swingProgress = lmss1.swingProgress;
        //field_110158_av = avatar.field_110158_av = lmss1.swingProgressInt;
        swingProgressInt = lmss1.swingProgressInt;
        isSwingInProgress = lmss1.isSwingInProgress;
        // 腕の挙動に関する処理
        litemuse = 0;
        for (int li = 0; li < swingController.mstatSwingStatus.length; li++) {
            swingController.mstatSwingStatus[li].onUpdate(this);
            if (swingController.mstatSwingStatus[li].isUsingItem()) {
                litemuse |= (1 << li);
            }
        }
        //System.out.println(height + ":" + (worldObj.isRemote ? "C" : "S"));

    }

    /**
     * 埋葬対策コピー
     */
    private boolean isBlockTranslucent(int par1, int par2, int par3) {
        return this.worldObj.getBlock(par1, par2, par3).isNormalCube();
    }

    /**
     * 埋葬対策コピー
     */
    private boolean isHeadspaceFree(int x, int y, int z, int height) {
        for (int i1 = 0; i1 < height; i1++) {
            if (isBlockTranslucent(x, y + i1, z + 1))
                return false;
        }
        return true;
    }

    /**
     * 埋葬対策コピー
     */
    @Override
    //protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
    protected boolean func_145771_j(double p_145771_1_, double p_145771_3_, double p_145771_5_) {
        // EntityPlayerSPのを引っ張ってきた
        if (this.noClip) {
            return false;
        }
        int i = MathHelper.floor_double(p_145771_1_);
        int j = MathHelper.floor_double(p_145771_3_);
        int k = MathHelper.floor_double(p_145771_5_);
        double d3 = p_145771_1_ - (double) i;
        double d4 = p_145771_5_ - (double) k;
        int entHeight = Math.max(Math.round(this.height), 1);
        boolean inTranslucentBlock = true;
        for (int i1 = 0; i1 < entHeight; i1++) {
            if (!this.isBlockTranslucent(i, j + i1, k)) {
                inTranslucentBlock = false;
            }
        }
        if (inTranslucentBlock) {
            boolean flag = !isHeadspaceFree(i - 1, j, k, entHeight);
            boolean flag1 = !isHeadspaceFree(i + 1, j, k, entHeight);
            boolean flag2 = !isHeadspaceFree(i, j, k - 1, entHeight);
            boolean flag3 = !isHeadspaceFree(i, j, k + 1, entHeight);
            byte b0 = -1;
            double d5 = 9999.0D;
            if (flag && d3 < d5) {
                d5 = d3;
                b0 = 0;
            }
            if (flag1 && 1.0D - d3 < d5) {
                d5 = 1.0D - d3;
                b0 = 1;
            }
            if (flag2 && d4 < d5) {
                d5 = d4;
                b0 = 4;
            }
            if (flag3 && 1.0D - d4 < d5) {
                d5 = 1.0D - d4;
                b0 = 5;
            }
            float f = 0.1F;
            if (b0 == 0) {
                this.motionX = (double) (-f);
            }
            if (b0 == 1) {
                this.motionX = (double) f;
            }
            if (b0 == 4) {
                this.motionZ = (double) (-f);
            }
            if (b0 == 5) {
                this.motionZ = (double) f;
            }
        }
        return false;
    }

    public void onLivingUpdate() {
        float lhealth = getHealth();
        if (lhealth > 0) {
            if (!worldObj.isRemote) {
                if (swingController.getSwingStatusDominant().canAttack()) {
                    if (!isBloodsuck()) {
                        // 通常時は回復優先
                        if (lhealth < getMaxHealth()) {
                            if (inventory.consumeInventoryItem(Items.sugar)) {
                                eatSugar(true, false);
                            }
                        }
                    }
                }
            }
        }

        super.onLivingUpdate();
        inventory.decrementAnimations();
        // 埋葬対策
        boolean grave = true;
        grave &= func_145771_j(posX - (double) width * 0.34999999999999998D, boundingBox.minY, posZ + (double) width * 0.34999999999999998D);
        grave &= func_145771_j(posX - (double) width * 0.34999999999999998D, boundingBox.minY, posZ - (double) width * 0.34999999999999998D);
        grave &= func_145771_j(posX + (double) width * 0.34999999999999998D, boundingBox.minY, posZ - (double) width * 0.34999999999999998D);
        grave &= func_145771_j(posX + (double) width * 0.34999999999999998D, boundingBox.minY, posZ + (double) width * 0.34999999999999998D);
        if (grave && onGround) {
            jump();
        }
        if (lhealth > 0) {
            // 近接監視の追加はここ
            // アイテムの回収
            if (!worldObj.isRemote) {
                List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(1.0D, 0.0D, 1.0D));
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Entity entity = (Entity) list.get(i);
                        if (!entity.isDead) {
                            if (entity instanceof EntityArrow) {
                                // 特殊回収
                                ((EntityArrow) entity).canBePickedUp = 1;
                            }
                            entity.onCollideWithPlayer(avatar);
                        }
                    }
                    // アイテムが一杯になっていてアイテムにタゲをとっている場合はタゲをクリア
                    if (entityToAttack instanceof EntityItem && inventory.getFirstEmptyStack() == -1) {
                        setTarget(null);
                    }
                }
            }
        }
        if (!worldObj.isRemote) {
            if (swingController.getSwingStatusDominant().canAttack()) {
                //              mod_LMM_littleMaidMob.Debug("isRemort:" + worldObj.isRemote);
                // 回復
                if (getHealth() < getMaxHealth()) {
                    if (inventory.consumeInventoryItem(Items.sugar)) {
                        eatSugar(true, false);
                    }
                }
                // つまみ食い
                if (rand.nextInt(50000) == 0 && inventory.consumeInventoryItem(Items.sugar)) {
                    eatSugar(true, false);
                }
                // 契約更新
                if (isContractEX()) {
                    float f = getContractLimitDays();
                    if (f <= 6 && inventory.consumeInventoryItem(Items.sugar)) {
                        // 契約更新
                        eatSugar(true, true);
                    }
                }
            }
        }
    }

    /**
     * 契約期間の残りがあるかを確認
     */
    protected void updateRemainsContract() {
        boolean lflag = false;
        if (maidContractLimit > 0) {
            maidContractLimit--;
            lflag = true;
        }
        if (getMaidFlags(dataWatch_Flags_remainsContract) != lflag) {
            setMaidFlags(lflag, dataWatch_Flags_remainsContract);
        }
    }

    /**
     * ストライキに入っていないか判定
     * 
     * @return
     */
    public boolean isRemainsContract() {
        return getMaidFlags(dataWatch_Flags_remainsContract);
    }

    public float getContractLimitDays() {
        return maidContractLimit > 0 ? ((float) maidContractLimit / 24000F) : -1F;
    }

    public boolean updateMaidContract() {
        // TODO 同一性のチェック
        //		boolean lf = isContract();
        //		if (textureData.isContract() != lf) {
        //			textureData.setContract(lf);
        //			return true;
        //		}
        return false;
    }

    @Override
    public EntityLivingBase getOwner() {
        return getMaidMasterEntity();
    }

    public EntityPlayer getMaidMasterEntity() {
        // 主を獲得
        if (isContract()) {
            EntityPlayer entityplayer = mstatMasterEntity;
            if (mstatMasterEntity == null || mstatMasterEntity.isDead) {
                entityplayer = (EntityPlayer) super.getOwner();

                // クリエイティブモードの状態を主とあわせる
                if (entityplayer != null && avatar != null) {
                    avatar.capabilities.isCreativeMode = entityplayer.capabilities.isCreativeMode;
                }
            }
            return entityplayer;
        } else {
            return null;
        }
    }

    /*
    public boolean isMaidContractOwner(String pname) {
        return pname.equalsIgnoreCase(getOwnerName());
    }
    */
    public boolean isMaidContractOwner(EntityPlayer pentity) {
        return pentity == getMaidMasterEntity();

        //		return pentity == mstatMasterEntity;
    }

    // AI関連

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    public boolean interact(EntityPlayer par1EntityPlayer) {
        float lhealth = getHealth();
        ItemStack itemstack1 = par1EntityPlayer.getCurrentEquippedItem();

        // プラグインでの処理を先に行う
        if (modeController.callPreInteract(par1EntityPlayer, itemstack1)) {
            return true;
        }
        // しゃがみ時は処理無効
        if (par1EntityPlayer.isSneaking()) {
            return false;
        }
        // ナデリ判定
        if (lhealth > 0F && par1EntityPlayer.riddenByEntity != null && !(par1EntityPlayer.riddenByEntity instanceof EntityLittleMaidBase)) {
            // 載せ替え
            par1EntityPlayer.riddenByEntity.mountEntity(this);
            return true;
        }

        if (par1EntityPlayer.fishEntity == null) {

            if (isContract()) {
                // 契約状態
                if (lhealth > 0F && isMaidContractOwner(par1EntityPlayer)) {
                    if (itemstack1 != null) {
                        // 追加分の処理
                        setPathToEntity(null);
                        // プラグインでの処理を先に行う

                        if (modeController.callInteract(par1EntityPlayer, itemstack1)) {
                            return true;
                        }
                        if (isRemainsContract()) {
                            // 通常
                            if (itemstack1.getItem() == Items.sugar) {
                                // モード切替
                                MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
                                eatSugar(false, true);
                                worldObj.setEntityState(this, (byte) 11);

                                littleMaidMob.Debug("give suger." + worldObj.isRemote);
                                if (!worldObj.isRemote) {
                                    modeController.setFreedom(modeController.isFreedom());
                                    if (isMaidWait()) {
                                        // 動作モードの切替
                                        boolean lflag = false;
                                        modeController.setActiveModeClass(null);
                                        EntityModeBase emb = modeController.callChangeMode(par1EntityPlayer);
                                        lflag = emb != null;
                                        if (lflag) {
                                            modeController.setActiveModeClass(emb);
                                        }
                                        if (!lflag) {
                                            modeController.setMaidMode("Escorter");
                                            setEquipItem(-1);
                                            //                                          maidInventory.currentItem = -1;
                                        }
                                        setMaidWait(false);
                                        getNextEquipItem();
                                    } else {
                                        // 待機
                                        setMaidWait(true);
                                    }
                                    littleMaidMob.Debug("now mode:%s", modeController.getDisplayModeName());
                                }
                                return true;
                            } else if (itemstack1.getItem() == Items.dye) {
                                // カラーメイド
                                if (!worldObj.isRemote) {
                                    setColor(15 - itemstack1.getItemDamage());
                                }
                                MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
                                return true;
                            } else if (itemstack1.getItem() == Items.feather) {
                                // 自由行動
                                MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
                                modeController.setFreedom(!modeController.isFreedom());
                                worldObj.setEntityState(this, modeController.isFreedom() ? (byte) 12 : (byte) 13);
                                return true;
                            } else if (itemstack1.getItem() == Items.saddle) {
                                // 肩車
                                if (!worldObj.isRemote) {
                                    if (ridingEntity == par1EntityPlayer) {
                                        this.mountEntity(null);
                                    } else {
                                        this.mountEntity(par1EntityPlayer);
                                    }
                                    return true;
                                }
                            } else if (itemstack1.getItem() == Items.gunpowder) {
                                // test TNT-D
                                //                              playSound(LMM_EnumSound.eatGunpowder, false);
                                //TODO:そのうち
                                //maidOverDriveTime.setValue(itemstack1.stackSize * 10);
                                //MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, itemstack1.stackSize);
                                return true;
                            } else if (itemstack1.getItem() == Items.book) {
                                // IFFのオープン
                                //TODO:そのうち
                                //MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
                                //                              ModLoader.openGUI(par1EntityPlayer, new LMM_GuiIFF(worldObj, this));
                                //if (worldObj.isRemote) {

                                //}
                                return true;
                            } else if ((itemstack1.getItem() == Items.glass_bottle) && (experienceValue >= 5)) {
                                // Expボトル
                                MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
                                if (!worldObj.isRemote) {
                                    entityDropItem(new ItemStack(Items.experience_bottle), 0.5F);
                                    experienceValue -= 5;
                                    if (avatar != null) {
                                        avatar.experienceTotal -= 5;
                                    }
                                }
                                return true;
                            } else if (itemstack1.getItem() instanceof ItemPotion) {
                                // ポーション
                                if (!worldObj.isRemote) {
                                    List list = ((ItemPotion) itemstack1.getItem()).getEffects(itemstack1);
                                    if (list != null) {
                                        PotionEffect potioneffect;
                                        for (Iterator iterator = list.iterator(); iterator.hasNext(); addPotionEffect(new PotionEffect(potioneffect))) {
                                            potioneffect = (PotionEffect) iterator.next();
                                        }
                                    }
                                }
                                MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
                                return true;
                            } else if (modeController.isFreedom() && itemstack1.getItem() == Items.redstone) {
                                // Tracer
                                /*TODO:そのうち
                                MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
                                setPathToEntity(null);
                                setMaidWait(false);
                                setTracer(!isTracer());
                                if (isTracer()) {
                                    worldObj.setEntityState(this, (byte) 14);
                                } else {
                                    worldObj.setEntityState(this, (byte) 12);
                                }*/

                                return true;
                            }
                        } else {
                            // ストライキ
                            if (itemstack1.getItem() == Items.sugar) {
                                // 受取拒否
                                worldObj.setEntityState(this, (byte) 10);
                                return true;
                            } else if (itemstack1.getItem() == Items.cake) {
                                // 再契約
                                MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
                                maidContractLimit = (24000 * 7);
                                modeController.setFreedom(false);
                                //setTracer(false);
                                setMaidWait(false);
                                modeController.setMaidMode("Escorter");
                                worldObj.setEntityState(this, (byte) 11);
                                playSound(EnumSound.Recontract, true);
                                return true;
                            }
                        }
                    }
                    // メイドインベントリ
                    setOwner(par1EntityPlayer.getUniqueID().toString());
                    getNavigator().clearPathEntity();
                    isJumping = false;
                    displayGUIInventory(par1EntityPlayer);
                    //                      ModLoader.openGUI(par1EntityPlayer, new LMM_GuiInventory(this, par1EntityPlayer.inventory, maidInventory));
                    //                  serchedChest.clear();
                    return true;
                }
            } else {
                // 未契約
                if (itemstack1 != null) {
                    if (itemstack1.getItem() == Items.cake) {
                        // 契約
                        MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);

                        deathTime = 0;
                        if (!worldObj.isRemote) {
                            /*TODO:アチーブメン
                            if (littleMaidMob.ac_Contract != null) {
                                par1EntityPlayer.triggerAchievement(mod_LMM_littleMaidMob.ac_Contract);
                            }
                            */
                            setContract(true);
                            setOwner(par1EntityPlayer.getUniqueID().toString());
                            setHealth(20);
                            modeController.setMaidMode("Escorter");
                            setMaidWait(false);
                            modeController.setFreedom(false);
                            setModel("default");
                            setColor(0x0c);
                            playSound(EnumSound.getCake, true);
                            //                          playLittleMaidSound(LMM_EnumSound.getCake, true);
                            //                          playTameEffect(true);
                            worldObj.setEntityState(this, (byte) 7);
                            // 契約記念日と、初期契約期間
                            maidContractLimit = (24000 * 7);
                            //maidAnniversary = worldObj.getTotalWorldTime();
                            // テクスチャのアップデート:いらん？
                            //                          LMM_Net.sendToAllEClient(this, new byte[] {LMM_Net.LMN_Client_UpdateTexture, 0, 0, 0, 0});

                        }
                        return true;
                    } else {
                        //                      worldObj.setEntityState(this, (byte)6);
                    }
                }
            }
        }

        return false;
    }

    // 状態識別変数郡

    /**
     * 血に飢えているか？
     * 
     * @return
     */
    public boolean isBloodsuck() {
        return false;
    }

    /**
     * 待機状態であるか？
     * 
     * @return
     */
    public boolean isWait() {
        return maidWait;
    }

    // 首周り
    public void setLooksWithInterest(boolean f) {
        if (looksWithInterest != f) {
            looksWithInterest = f;
            if (numTicksToChaseTarget <= 0) {
                looksWithInterestAXIS = rand.nextBoolean();
            }
            int li = dataWatcher.getWatchableObjectInt(dataWatch_Flags);
            li = looksWithInterest ? (li | dataWatch_Flags_looksWithInterest) : (li & ~dataWatch_Flags_looksWithInterest);
            li = looksWithInterestAXIS ? (li | dataWatch_Flags_looksWithInterestAXIS) : (li & ~dataWatch_Flags_looksWithInterestAXIS);
            dataWatcher.updateObject(dataWatch_Flags, Integer.valueOf(li));
        }
    }

    public boolean getLooksWithInterest() {
        looksWithInterest = (dataWatcher.getWatchableObjectInt(dataWatch_Flags) & dataWatch_Flags_looksWithInterest) > 0;
        looksWithInterestAXIS = (dataWatcher.getWatchableObjectInt(dataWatch_Flags) & dataWatch_Flags_looksWithInterestAXIS) > 0;

        return looksWithInterest;
    }

    public float getInterestedAngle(float f) {
        return (prevRotateAngleHead + (rotateAngleHead - prevRotateAngleHead) * f) * ((looksWithInterestAXIS ? 0.08F : -0.08F) * (float) Math.PI);
    }

    @Override
    public boolean isBreedingItem(ItemStack par1ItemStack) {
        // お好みは何？
        if (par1ItemStack != null) {
            if (isContractEX()) {
                return par1ItemStack.getItem() == Items.sugar;
            } else {
                return par1ItemStack.getItem() == Items.cake;
            }
        }
        return false;
    }

    // GUI関連

    /**
     * インベントリの表示
     * 
     * @param pPlayer
     */
    @SideOnly(Side.CLIENT)
    public void displayGUIInventory(EntityPlayer pPlayer) {
        pPlayer.openGui(littleMaidMob.instance, 0, worldObj, getEntityId(), 0, 0);
        //		FMLClientHandler.instance().displayGuiScreen(pPlayer, new GuiLittleMaidInventory(this, pPlayer));
    }

    /**
     * IFF設定の表示
     * 
     * @param pPlayer
     */
    @SideOnly(Side.CLIENT)
    public void displayGUIIFF(EntityPlayer pPlayer) {
        pPlayer.openGui(littleMaidMob.instance, 1, worldObj, getEntityId(), 0, 0);
        //		FMLClientHandler.instance().displayGuiScreen(pPlayer, new GuiLittleMaidInventory(this, pPlayer));
    }

    // イベント関連

    /**
     * 周囲のプレーヤーにパケットを送信する
     * 
     * @param pRange 射程距離
     * @param pPacket
     */
    public void sendToAllNear(double pRange, Packet pPacket) {
        MinecraftServer lms = FMLCommonHandler.instance().getMinecraftServerInstance();
        lms.getConfigurationManager().sendToAllNear(posX, posY, posZ, pRange, dimension, pPacket);
    }

    public void sendToMaster(Packet pPacket) {
        if (mstatMasterEntity instanceof EntityPlayerMP) {
            ((EntityPlayerMP) mstatMasterEntity).playerNetServerHandler.sendPacket(pPacket);
        }
    }

    // ポーションエフェクト

    @Override
    protected void onNewPotionEffect(PotionEffect par1PotionEffect) {
        super.onNewPotionEffect(par1PotionEffect);
        //		if (isContract()) {
        sendToAllNear(64D, new S1DPacketEntityEffect(getEntityId(), par1PotionEffect));
        //		}
    }

    @Override
    protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2) {
        super.onChangedPotionEffect(par1PotionEffect, par2);
        // なんかエンドレスで再設定されるので更新なし。
        //		if (isContract()) {
        //			sendToAllNear(64D, new S1DPacketEntityEffect(getEntityId(), par1PotionEffect));
        //		}
    }

    @Override
    protected void onFinishedPotionEffect(PotionEffect par1PotionEffect) {
        super.onFinishedPotionEffect(par1PotionEffect);
        //		if (isContract()) {
        sendToAllNear(64D, new S1EPacketRemoveEntityEffect(getEntityId(), par1PotionEffect));
        //		}
    }

    /**
     * フラグ群に値をセット。
     * 
     * @param pCheck： 対象値。
     * @param pFlags： 対象フラグ。
     */
    public void setMaidFlags(boolean pFlag, int pFlagvalue) {
        int li = dataWatcher.getWatchableObjectInt(dataWatch_Flags);
        li = pFlag ? (li | pFlagvalue) : (li & ~pFlagvalue);
        dataWatcher.updateObject(dataWatch_Flags, Integer.valueOf(li));
    }

    /**
     * 指定されたフラグを獲得
     */
    public boolean getMaidFlags(int pFlagvalue) {
        return (dataWatcher.getWatchableObjectInt(dataWatch_Flags) & pFlagvalue) > 0;
    }

    // メイドの待機設定
    public boolean isMaidWait() {
        return maidWait;
    }

    public boolean isMaidWaitEx() {
        return isMaidWait() | (mstatWaitCount > 0) | isOpenInventory();
    }

    public void setMaidWait(boolean pflag) {
        // 待機常態の設定、 isMaidWait系でtrueを返すならAIが勝手に移動を停止させる。
        maidWait = pflag;
        setMaidFlags(pflag, dataWatch_Flags_Wait);

        aiSit.setSitting(pflag);
        maidWait = pflag;
        isJumping = false;
        setAttackTarget(null);
        setRevengeTarget(null);
        setPathToEntity(null);
        getNavigator().clearPathEntity();
        velocityChanged = true;
    }

    public void setMaidWaitCount(int count) {
        mstatWaitCount = count;
    }

    // インベントリの表示関係
    // まさぐれるのは一人だけ
    public boolean isOpenInventory() {
        return inventory.isOpen;
    }

    /**
     * GUIを開いた時にサーバー側で呼ばれる。
     */
    public void onGuiOpened() {
    }

    /**
     * GUIを閉めた時にサーバー側で呼ばれる。
     */
    public void onGuiClosed() {
        setMaidWaitCount(modeController.getActiveModeClass().getWaitDelayTime());
    }

    public void setAbsorptionAmount(float par1) {
        if (par1 < 0.0F) {
            par1 = 0.0F;
        }

        dataWatcher.updateObject(dataWatch_Absoption, Float.valueOf(par1));
    }

    public float getAbsorptionAmount() {
        return dataWatcher.getWatchableObjectFloat(dataWatch_Absoption);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
        // TODO Auto-generated method stub
        super.readEntityFromNBT(par1nbtTagCompound);
        inventory.readFromNBT(par1nbtTagCompound.getTagList("Inventory", 10));
        maidContractLimit = par1nbtTagCompound.getInteger("ContractLimit");
        setMaidWait(par1nbtTagCompound.getBoolean("Wait"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
        // TODO Auto-generated method stub
        super.writeEntityToNBT(par1nbtTagCompound);
        par1nbtTagCompound.setTag("Inventory", inventory.writeToNBT(new NBTTagList()));
        par1nbtTagCompound.setInteger("ContractLimit", maidContractLimit);
        par1nbtTagCompound.setBoolean("Wait", isMaidWait());
    }

    // MultiModel関連

    @Override
    public MultiModelData getMultiModel() {
        return multiModel;
    }

    @Override
    public void setMultiModelData(MultiModelData pMultiModelData) {
        multiModel = pMultiModelData;
    }

    @Override
    public void initMultiModel() {
        // 値の初期化
        multiModel.modelCaps = new MaidCaps(this);
        multiModel.setColor(0x0c);
        setModel("MMM_Aug");
        //		multiModel.setModelFromName("MMM_Aug");
        multiModel.forceChanged(false);
    }

    @Override
    public void onMultiModelChange() {
        // TODO Auto-generated method stub

    }

    public InventoryPlayer getInventory() {
        return avatar.inventory;
    }

    public boolean isBlocking() {
        return false;
    }

    public boolean getIFF(Entity pTarget) {
        return false;
    }

    /**
     * 血にうえているがたんとか
     */
    public void setBloodsuck(boolean b) {
    }

    public boolean isPlaying() {
        return false;
    }

    public Random getRand() {
        return rand;
    }

    public int getMaximumHomeDistance() {
        return 20;
    }

    //Tile絡み
    public TileContainer getTileContainer() {
        return tiles;
    }

    //装備とかアイテム

    public boolean getNextEquipItem() {
        if (worldObj.isRemote) {
            // クライアント側は処理しない
            return false;
        }

        int li;
        if (modeController.isActiveModeClass()) {
            li = modeController.getActiveModeClass().getNextEquipItem(maidMode);
        } else {
            li = -1;
        }
        setEquipItem(swingController.getDominantArm(), li);
        return li > -1;
    }

    public void setEquipItem(int pArm, int pIndex) {
        if (pArm == swingController.getDominantArm()) {
            inventory.currentItem = pIndex;
        }
        int li = swingController.mstatSwingStatus[pArm].index;
        if (li != pIndex) {
            if (li > -1) {
                inventory.setChanged(li);
            }
            if (pIndex > -1) {
                inventory.setChanged(pIndex);
            }
            swingController.mstatSwingStatus[pArm].setSlotIndex(pIndex);
        }
    }

    public void setEquipItem(int pIndex) {
        setEquipItem(swingController.getDominantArm(), pIndex);
    }

    //音
    public void playSound(String string) {
        worldObj.playSoundAtEntity(this, string, 1F, 1F);
    }

    public void playSound(EnumSound enumSound, boolean b) {
    }

    public SwingController getSwingStatus() {
        return this.swingController;
    }

    // お仕事チュ
    /**
     * 仕事中かどうかの設定
     */
    public void setWorking(boolean pFlag) {
        mstatWorkingCount.setEnable(pFlag);
    }

    /**
     * 仕事中かどうかを返す
     */
    public boolean isWorking() {
        return mstatWorkingCount.isEnable();
    }

    /**
     * 仕事が終了しても余韻を含めて返す
     */
    public boolean isWorkingDelay() {
        return mstatWorkingCount.isDelay();
    }

    //よくわからないけど可視化
    @Override
    public void updateWanderPath() {
        super.updateWanderPath();
    }

    /**
     * 音声再生用。
     * 通常の再生ではネットワーク越しになるのでその対策。
     */
    public void playLittleMaidSound(EnumSound enumsound, boolean force) {
        /*
        // 音声の再生
        if ((maidSoundInterval > 0 && !force) || enumsound == EnumSound.Null)
            return;
        maidSoundInterval = 20;
        if (worldObj.isRemote) {
            // Client
            String s = SoundManager.instance.getSoundResource(this,enumsound);
            float lpitch = 0;//littleMaidMob.cfg_VoiceDistortion ? (rand.nextFloat() * 0.2F) + 0.95F : 1.0F;
            FMLClientHandler.instance().
            this.worldObj.playSoundAtEntity(s, getSoundVolume(), lpitch, false);
        }*/
    }

    @Override
    public String toString() {
        return "Owner:" + getOwner();
    }

    // 砂糖関連
    public void setLookSuger(boolean pFlag) {
        mstatLookSuger = pFlag;
        setMaidFlags(pFlag, dataWatch_Flags_LooksSugar);
    }

    public boolean isLookSuger() {
        return mstatLookSuger;
    }

    /**
     * ペロッ・・・これは・・・砂糖ッ！！
     * motion : 腕を振るか？
     * recontract : 契約延長効果アリ？
     */
    public void eatSugar(boolean motion, boolean recontract) {
        if (motion) {
            swingController.setSwing(2, (getMaxHealth() - getHealth() <= 1F) ? EnumSound.eatSugar_MaxPower : EnumSound.eatSugar);
        }
        int h = hurtResistantTime;
        heal(1);
        hurtResistantTime = h;
        playSound("random.pop");
        littleMaidMob.Debug(("eat Suger." + worldObj.isRemote));

        if (recontract) {
            // 契約期間の延長
            maidContractLimit += 24000;
            if (maidContractLimit > 168000) {
                maidContractLimit = 168000; // 24000 * 7
            }
        }

        // 暫定処理
        if (avatar != null) {
            avatar.getFoodStats().addStats(20, 20F);
        }
    }

    /** クライアント用各種フラグアップデート */
    public void updateMaidFlagsClient() {
        int li = dataWatcher.getWatchableObjectInt(dataWatch_Flags);
        modeController.setMaidFreedom((li & dataWatch_Flags_Freedom) > 0);
        //maidTracer = (li & dataWatch_Flags_Tracer) > 0;
        maidWait = (li & dataWatch_Flags_Wait) > 0;
        //mstatAimeBow = (li & dataWatch_Flags_Aimebow) > 0;
        mstatLookSuger = (li & dataWatch_Flags_LooksSugar) > 0;
        //mstatBloodsuck = (li & dataWatch_Flags_Bloodsuck) > 0;
        looksWithInterest = (li & dataWatch_Flags_looksWithInterest) > 0;
        looksWithInterestAXIS = (li & dataWatch_Flags_looksWithInterestAXIS) > 0;
        //maidOverDriveTime.updateClient((li & dataWatch_Flags_OverDrive) > 0);
        mstatWorkingCount.updateClient((li & dataWatch_Flags_Working) > 0);
    }

    // 保持アイテム関連

    /**
     * 現在の装備品
     */
    public ItemStack getCurrentEquippedItem() {
        return inventory.getCurrentItem();
    }

    @Override
    public ItemStack getHeldItem() {
        return inventory.getCurrentItem();
    }

    @Override
    public ItemStack getEquipmentInSlot(int par1) {
        if (par1 == 0) {
            return getHeldItem();
        } else if (par1 < 5) {
            return inventory.armorItemInSlot(par1 - 1);
        } else {
            return inventory.getStackInSlot(par1 - 5);
        }
    }

    @Override
    public ItemStack func_130225_q(int par1) {
        return inventory.armorItemInSlot(par1);
    }

    @Override
    public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack) {
        par1 &= 0x0000ffff;
        if (par1 == 0) {
            inventory.setInventoryCurrentSlotContents(par2ItemStack);
        } else if (par1 > 0 && par1 < 4) {
            inventory.armorInventory[par1 - 1] = par2ItemStack;
            //setTextureNames();
        } else if (par1 == 4) {
            //          maidInventory.mainInventory[mstatMaskSelect] = mstatMaskSelect > -1 ? par2ItemStack : null;
            if (mstatMaskSelect > -1) {
                inventory.mainInventory[mstatMaskSelect] = par2ItemStack;
            }
            //setTextureNames();
        } else {
            par1 -= 5;
            // 持ち物のアップデート
            // 独自拡張:普通にスロット番号の通り、上位８ビットは装備スロット
            // par1はShortで渡されるのでそのように。
            int lslotindex = par1 & 0x7f;
            int lequip = (par1 >>> 8) & 0xff;
            inventory.setInventorySlotContents(lslotindex, par2ItemStack);
            inventory.inventoryChanged = true;
            //          if (par1 >= maidInventory.mainInventory.length) {
            //              LMM_Client.setArmorTextureValue(this);
            //          }

            for (SwingStatus lss : swingController.mstatSwingStatus) {
                if (lslotindex == lss.index) {
                    lss.index = -1;
                }
            }
            if (lequip != 0xff) {
                setEquipItem(lequip, lslotindex);
                //              mstatSwingStatus[lequip].index = lslotindex;
            }
            if (lslotindex >= inventory.getInitInvSize()) {
                //setTextureNames();
            }
            String s = par2ItemStack == null ? null : par2ItemStack.getItem().getUnlocalizedName();
            littleMaidMob.Debug(String.format("ID:%s Slot(%2d:%d):%s", this, lslotindex, lequip, s == null ? "NoItem" : s));
        }
    }

    @Override
    public ItemStack[] getLastActiveItems() {
        return inventory.armorInventory;
    }

    protected void checkClockMaid() {
        // 時計を持っているか？
        mstatClockMaid = inventory.getInventorySlotContainItem(Items.clock) > -1;
    }

    /**
     * 時計を持っているか?
     */
    public boolean isClockMaid() {
        return mstatClockMaid;
    }

    protected void checkMaskedMaid() {
        // インベントリにヘルムがあるか？
        for (int i = inventory.mainInventory.length - 1; i >= 0; i--) {
            ItemStack is = inventory.getStackInSlot(i);
            if (is != null && is.getItem() instanceof ItemArmor && ((ItemArmor) is.getItem()).armorType == 0) {
                // ヘルムを持ってる
                mstatMaskSelect = i;
                inventory.armorInventory[3] = is;
                if (worldObj.isRemote) {
                    //setTextureNames();
                }
                return;
            }
        }

        mstatMaskSelect = -1;
        inventory.armorInventory[3] = null;
        return;
    }

    /**
     * メットを被ってるか
     */
    public boolean isMaskedMaid() {
        return mstatMaskSelect > -1;
    }

    protected void checkHeadMount() {
        // 追加の頭部装備の判定
        ItemStack lis = inventory.getHeadMount();
        mstatPlanter = false;
        mstatCamouflage = false;
        if (lis != null) {
            if (lis.getItem() instanceof ItemBlock) {
                Block lblock = Block.getBlockFromItem(lis.getItem());
                mstatPlanter = (lblock instanceof BlockFlower) && lblock.getRenderType() == 1;
                mstatCamouflage = (lblock instanceof BlockLeaves) || (lblock instanceof BlockPumpkin);
            } else if (lis.getItem() instanceof ItemSkull) {
                mstatCamouflage = true;
            }
        }
    }

    /**
     * カモフラージュ！
     */
    public boolean isCamouflage() {
        return mstatCamouflage;
    }

    /**
     * 鉢植え状態
     */
    public boolean isPlanter() {
        return mstatPlanter;
    }

}
