package ruby.bamboo.item.magatama;

import java.util.List;

import ruby.bamboo.entity.magatama.EntityShield;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MagatamaOrange implements IMagatama {

    @Override
    public int getColor() {
        return 0xff9933;
    }

    @Override
    public Class getEffectClass() {
        return Dummy.class;
    }

    @Override
    public int getReality() {
        return 1;
    }

    @Override
    public void holdingEffect(Entity entity, int invIndex) {
        if (hasExecution(entity, invIndex) && entity != null && entity.boundingBox != null) {
            for (Entity e : getEntityList(entity.worldObj, entity)) {
                if (e instanceof EntityThrowable) {
                    if (isNotThrower(((EntityThrowable) e).getThrower(), entity)) {
                        spawnShield(e.worldObj, entity, ((EntityThrowable) e).getThrower(), e);
                        useCost(entity);
                    }
                } else if (e instanceof EntityArrow) {
                    if (isNotThrower(((EntityArrow) e).shootingEntity, entity)) {
                        spawnShield(e.worldObj, entity, ((EntityArrow) e).shootingEntity, e);
                        useCost(entity);
                    }
                }
            }
        }
    }

    private void useCost(Entity entity) {
        if (entity instanceof EntityPlayer) {
            ((EntityPlayer) entity).getFoodStats().addExhaustion(50F);
        }
    }

    private boolean isNotThrower(Entity throwe, Entity entity) {
        return throwe != null && throwe != entity;
    }

    private void spawnShield(World world, Entity owner, Entity thrower, Entity throwObject) {
        if (!world.isRemote) {
            EntityShield shield = new EntityShield(world);
            shield.setOwner(owner);
            shield.setThrower(thrower, throwObject);
            shield.setPositionAndRotation(throwObject.posX, throwObject.posY, throwObject.posZ, throwObject.rotationYaw, throwObject.rotationPitch);
            world.spawnEntityInWorld(shield);
        } else {
            throwObject.setDead();
        }
    }

    private List<Entity> getEntityList(World world, Entity entity) {
        return world.getEntitiesWithinAABBExcludingEntity(entity, entity.boundingBox.expand(3, 3, 3));
    }

    private boolean hasExecution(Entity entity, int invIndex) {
        if (entity instanceof EntityPlayer) {
            ItemStack is;
            for (int i = 0; i < invIndex; i++) {
                is = ((EntityPlayer) entity).inventory.getStackInSlot(i);
                if (isItemStackIsThis(is)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isItemStackIsThis(ItemStack is) {
        return is != null && is.getItem() instanceof ItemMagatama && ItemMagatama.getMagatama(is.getItemDamage()) == this;
    }

    @Override
    public void useItem(World world, ItemStack itemStack, EntityPlayer entityPlayer) {

    }

    @Override
    public boolean isDecrease() {
        return false;
    }

}
