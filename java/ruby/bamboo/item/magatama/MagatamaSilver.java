package ruby.bamboo.item.magatama;

import java.lang.reflect.Field;
import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import ruby.bamboo.entity.magatama.EntityClock;
import ruby.bamboo.entity.magatama.EntityMagatama;

public class MagatamaSilver implements IMagatama {

    @Override
    public Class getEffectClass() {
        return EntityClock.class;
    }

    private static Field duration = null;
    static {
        try {
            duration = PotionEffect.class.getDeclaredField("duration");
        } catch (NoSuchFieldException e) {
            duration = PotionEffect.class.getDeclaredFields()[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void holdingEffect(Entity entity, int invIndex) {
        if (duration == null) {
            return;
        }
        if (entity instanceof EntityPlayer) {
            ItemStack is;
            for (int i = 0; i < invIndex; i++) {
                is = ((EntityPlayer) entity).inventory.getStackInSlot(i);
                if (isItemStackIsThis(is)) {
                    return;
                }
            }
            Iterator<PotionEffect> potions = ((EntityLivingBase) entity).getActivePotionEffects().iterator();
            PotionEffect p;
            while (potions.hasNext()) {
                p = potions.next();
                try {
                    duration.set(p, duration.getInt(p) + (entity.worldObj.rand.nextBoolean() ? 1 : 0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((EntityPlayer) entity).getFoodStats().addExhaustion(0.05F);
            }

        }
    }

    private boolean isItemStackIsThis(ItemStack is) {
        return is != null && is.getItem() instanceof ItemMagatama && ItemMagatama.getMagatama(is.getItemDamage()) == this;
    }

    @Override
    public int getReality() {
        return 1;
    }

    @Override
    public int getColor() {
        return 0xEEEEEE;
    }

    @Override
    public void useItem(World world, ItemStack itemStack, EntityPlayer entityPlayer) {
        if (!world.isRemote) {
            world.spawnEntityInWorld(new EntityMagatama(world, entityPlayer, itemStack));
        }
        world.playSoundAtEntity(entityPlayer, "random.bow", 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
        entityPlayer.swingItem();
    }

    @Override
    public boolean isDecrease() {
        return true;
    }
}
