package ruby.bamboo.entity.villager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityVillagerHead extends EntityWitherSkull {

    public EntityVillagerHead(World world) {
        super(world);
    }

    public EntityVillagerHead(World worldObj, EntityTrueVillager entityTrueVillager, double d6, double d7, double d8) {
        super(worldObj, entityTrueVillager, d6, d7, d8);
    }

    private float damage = 5F;
    private Entity target;
    private int timer;

    public EntityVillagerHead setDamage(float dmg) {
        this.damage = dmg;
        return this;
    }

    public EntityVillagerHead setTarget(Entity tgt) {
        this.target = tgt;
        return this;
    }

    @Override
    protected void onImpact(MovingObjectPosition p_70227_1_) {
        if (!this.worldObj.isRemote) {
            if (p_70227_1_.entityHit != null) {
                if (this.shootingEntity != null) {
                    if (p_70227_1_.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), damage) && !p_70227_1_.entityHit.isEntityAlive()) {
                        this.shootingEntity.heal(damage);
                    }
                } else {
                    p_70227_1_.entityHit.attackEntityFrom(DamageSource.magic, damage);
                }

                if (p_70227_1_.entityHit instanceof EntityLivingBase) {
                    byte b0 = 0;

                    if (this.worldObj.difficultySetting == EnumDifficulty.NORMAL || this.worldObj.difficultySetting == EnumDifficulty.EASY) {
                        b0 = 10;
                    } else if (this.worldObj.difficultySetting == EnumDifficulty.HARD) {
                        b0 = 40;
                    }

                    if (b0 > 0) {
                        Potion[] badPotions = new Potion[] { Potion.wither, Potion.poison, Potion.digSlowdown, Potion.harm, Potion.weakness, Potion.blindness, Potion.hunger };
                        ((EntityLivingBase) p_70227_1_.entityHit).addPotionEffect(new PotionEffect(badPotions[this.rand.nextInt(badPotions.length)].id, 20 * b0, 1));
                    }
                }
            }

            this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0F, false, false);
            this.setDead();
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
