package ruby.bamboo.entity.villager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ruby.bamboo.BambooInit;
import ruby.bamboo.entity.EntityBambooSpear;
import ruby.bamboo.item.magatama.ItemMagatama;

public class EntityTrueVillager extends EntityVillager implements
        IBossDisplayData, IRangedAttackMob, IEntitySelector {

    private int timer;
    private double distanceY = 10;
    private double distanceXZ = 150;
    private HashMap<Entity, Integer> hateMap = new HashMap<Entity, Integer>();
    public float wing;

    public EntityTrueVillager(World p_i1602_1_) {
        super(p_i1602_1_);
        this.noClip = true;
        this.setHealth(this.getMaxHealth());
        this.tasks.taskEntries.clear();
        this.isImmuneToFire = true;
        this.getNavigator().setCanSwim(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.2D, 30, 40.0F));
        //this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        List<ItemStack> list = getDropList();
        for (int i = 0; i < 12; i++) {
            if (rand.nextFloat() < 0.01) {
                this.entityDropItem(list.get(rand.nextInt(list.size())), 1);
            } else {
                this.dropItem(Items.emerald, 1);
            }
        }

    }

    private List<ItemStack> getDropList() {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(Items.nether_star, 1, 0));
        for (int i = 0; i < ((ItemMagatama) BambooInit.magatama).getMagatamaMaxValue(); i++) {
            list.add(new ItemStack(BambooInit.magatama, 1, i));
        }
        list.add(new ItemStack(Items.diamond));
        list.add(new ItemStack(Items.gold_ingot));
        list.add(new ItemStack(Items.iron_ingot));
        list.add(new ItemStack(BambooInit.bambooPickaxe));
        list.add(new ItemStack(BambooInit.seaweedSeed, 4, 0));
        list.add(new ItemStack(BambooInit.fireflyBottle, 4, 0));
        return list;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0D);
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (this.getAttackTarget() != null) {
            this.setWatchedTargetId(0, this.getAttackTarget().getEntityId());
        } else {
            this.setWatchedTargetId(0, 0);
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(0));
        this.dataWatcher.addObject(19, new Integer(0));
        this.dataWatcher.addObject(20, new Integer(0));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    protected void fall(float p_70069_1_) {

    }

    private int bombTimer;

    @Override
    public void onLivingUpdate() {

        this.motionY *= 0.6000000238418579D;
        double d1;
        double d3;
        double d5;
        if (!this.worldObj.isRemote) {
            if (this.getAttackTarget() == null || this.getAttackTarget().isDead) {
                this.setDead();
            }
            if (0 < this.getWatchedTargetId(0)) {
                Entity entity = this.worldObj.getEntityByID(this.getWatchedTargetId(0));

                if (entity != null && entity instanceof EntityLivingBase) {
                    if (this.posY < entity.posY + distanceY) {
                        if (this.motionY < 0.0D) {
                            this.motionY = 0.0D;
                        }
                        this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
                    } else if (entity.posY + distanceY + 0.5 < this.posY) {
                        this.motionY -= (0.1D - this.motionY) * 0.6000000238418579D;
                    }

                    double d0 = entity.posX - this.posX;
                    d1 = entity.posZ - this.posZ;
                    d3 = d0 * d0 + d1 * d1;

                    if (d3 > distanceXZ) {
                        d5 = (double) MathHelper.sqrt_double(d3);
                        this.motionX += (d0 / d5 * 0.5D - this.motionX) * 0.1000000238418579D;
                        this.motionZ += (d1 / d5 * 0.5D - this.motionZ) * 0.1000000238418579D;
                    } else if (d3 < distanceXZ + 2) {
                        d5 = (double) MathHelper.sqrt_double(d3);
                        this.motionX -= (d0 / d5 * 0.5D - this.motionX) * 0.1000000238418579D;
                        this.motionZ -= (d1 / d5 * 0.5D - this.motionZ) * 0.1000000238418579D;
                    }
                    if (this.getAttackTarget() == null) {
                        this.setAttackTarget((EntityLivingBase) entity);
                    }
                }
                if (this.getAttackTarget() != null) {
                    //隠れた時の対策
                    if (!this.getEntitySenses().canSee(this.getAttackTarget()) || 40 < this.getDistanceToEntity(this.getAttackTarget())) {
                        if (20 < bombTimer++) {
                            switch (rand.nextInt(10)) {
                            case 0:
                                List<EntityPlayer> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(100, 40, 100), this);
                                for (int i = 0; i < list.size() || i < 10; i++) {
                                    EntityPlayer e = list.get(rand.nextInt(list.size()));
                                    summonThunderBolt(e.posX, e.posY, e.posZ);
                                }
                                break;
                            default:
                                this.summonTNT(this.getAttackTarget().posX, this.getAttackTarget().posY + 1, this.getAttackTarget().posZ);
                            }
                            bombTimer = 0;
                        }
                    } else {
                        bombTimer = 0;
                    }
                    //走って移動してる対策
                    if (this.getAttackTarget().isSprinting() && rand.nextBoolean()) {
                        EntityBambooSpear ebs = new EntityBambooSpear(this.worldObj, this, this.getAttackTarget(), 4F, (float) (14 - this.worldObj.difficultySetting.getDifficultyId() * 4));
                        ebs.canBePickedUp = 2;
                        ebs.setMaxAge(60);
                        this.worldObj.spawnEntityInWorld(ebs);
                    }
                }
            } else {
                if (timer++ % 200 == 0) {
                    for (Object obj : worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(64, 16, 64), this)) {
                        setAttackTarget((EntityLivingBase) obj);
                        setWatchedTargetId(0, ((EntityPlayer) obj).getEntityId());
                        break;
                    }
                }
            }
        }
        super.onLivingUpdate();
    }

    public void summonThunderBolt(double targetPosX, double targetPosY, double targetPosZ) {
        this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, targetPosX, targetPosY, targetPosZ));
        this.worldObj.createExplosion(this, targetPosX, targetPosY, targetPosZ, 5, false);
    }

    public void summonTNT(double targetPosX, double targetPosY, double targetPosZ) {
        EntityTNTPrimed tnt = new EntityTNTPrimed(worldObj, targetPosX, targetPosY, targetPosZ, this);
        this.worldObj.spawnEntityInWorld(tnt);
        worldObj.playSoundAtEntity(tnt, "game.tnt.primed", 1.0F, 1.0F);
    }

    @Override
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
        if (this.isInWater()) {
            this.moveFlying(p_70612_1_, p_70612_2_, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        } else if (this.handleLavaMovement()) {
            this.moveFlying(p_70612_1_, p_70612_2_, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        } else {
            float f2 = 0.91F;

            if (this.onGround) {
                f2 = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)).slipperiness * 0.91F;
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            this.moveFlying(p_70612_1_, p_70612_2_, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;

            if (this.onGround) {
                f2 = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)).slipperiness * 0.91F;
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double) f2;
            this.motionY *= (double) f2;
            this.motionZ *= (double) f2;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount = 0;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

    }

    @Override
    public IChatComponent func_145748_c_() {
        return super.func_145748_c_();
    }

    @Override
    protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }

    public int getWatchedTargetId(int num) {
        return this.dataWatcher.getWatchableObjectInt(17 + num);
    }

    public void setWatchedTargetId(int num, int id) {
        this.dataWatcher.updateObject(17 + num, Integer.valueOf(id));
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {
        for (int i = rand.nextInt(10); 0 < i; i--) {
            this.nomalRangeAttack(60, target.posX + i * rand.nextFloat() - 0.5F, target.posY, target.posZ + i * rand.nextFloat() - 0.5F, false);
        }
    }

    private void func_82216_a(int p_82216_1_, EntityLivingBase target) {
        this.nomalRangeAttack(p_82216_1_, target.posX, target.posY + (double) target.getEyeHeight() * 0.5D, target.posZ, p_82216_1_ == 0 && this.rand.nextFloat() < 0.001F);
    }

    private void nomalRangeAttack(int i1, double p_82209_2_, double p_82209_4_, double p_82209_6_, boolean p_82209_8_) {
        this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1014, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
        double d3 = this.func_82214_u(i1);
        double d4 = this.func_82208_v(i1);
        double d5 = this.func_82213_w(i1);
        double d6 = p_82209_2_ - d3;
        double d7 = p_82209_4_ - d4;
        double d8 = p_82209_6_ - d5;
        EntityVillagerHead entity = new EntityVillagerHead(this.worldObj, this, d6, d7, d8);
        entity.setTarget(this.getAttackTarget());
        if (p_82209_8_) {
            entity.setInvulnerable(true);
        }

        entity.posY = d4 + rand.nextFloat() * 3;
        entity.posX = d3;
        entity.posZ = d5 + rand.nextFloat() * 3;
        this.worldObj.spawnEntityInWorld(entity);
    }

    private double func_82214_u(int p_82214_1_) {
        if (p_82214_1_ <= 0) {
            return this.posX;
        } else {
            float f = (this.renderYawOffset + (float) (180 * (p_82214_1_ - 1))) / 180.0F * (float) Math.PI;
            float f1 = MathHelper.cos(f);
            return this.posX + (double) f1 * 1.3D;
        }
    }

    private double func_82208_v(int p_82208_1_) {
        return p_82208_1_ <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
    }

    private double func_82213_w(int p_82213_1_) {
        if (p_82213_1_ <= 0) {
            return this.posZ;
        } else {
            float f = (this.renderYawOffset + (float) (180 * (p_82213_1_ - 1))) / 180.0F * (float) Math.PI;
            float f1 = MathHelper.sin(f);
            return this.posZ + (double) f1 * 1.3D;
        }
    }

    @Override
    public boolean isEntityApplicable(Entity entity) {
        return entity instanceof EntityPlayer;
    }

    @Override
    public boolean interact(EntityPlayer p_70085_1_) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected float getSoundVolume() {
        return 5.0F;
    }

}
