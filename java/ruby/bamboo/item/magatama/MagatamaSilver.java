package ruby.bamboo.item.magatama;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.entity.magatama.EntityClock;
import ruby.bamboo.entity.magatama.EntityMagatama;

public class MagatamaSilver implements IMagatama {

    @Override
    public Class getEffectClass() {
        return EntityClock.class;
    }

    @Override
    public void holdingEffect(Entity entity, int invIndex) {
        /* durationがprivateになってるじゃねーか死ね
        if (entity instanceof EntityPlayer) {
            ItemStack is;
            for (int i = 0; i < invIndex; i++) {
                is = ((EntityPlayer) entity).inventory.getStackInSlot(i);
                if (isItemStackIsThis(is)) {
                    return;
                }
            }
            Iterator<PotionEffect> potions = ((EntityLivingBase) entity).getActivePotionEffects().iterator();
            while (potions.hasNext()) {
                potions.next().duration += entity.worldObj.rand.nextBoolean() ? 1 : 0;
                ((EntityPlayer) entity).getFoodStats().addExhaustion(0.05F);
            }

        }*/
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
