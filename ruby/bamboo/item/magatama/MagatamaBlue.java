package ruby.bamboo.item.magatama;

import ruby.bamboo.entity.magatama.EntityMagatama;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MagatamaBlue implements IMagatama {
    // 水属性？
    @Override
    public int getColor() {
        return 0x3333FF;
    }

    @Override
    public Class getEffectClass() {
        return null;
    }

    @Override
    public int getReality() {
        return 1;
    }

    @Override
    public void holdingEffect(Entity entity, int invIndex) {
        if (entity instanceof EntityPlayer) {
            if (!((EntityPlayer) entity).capabilities.isCreativeMode) {
                if (entity.isInWater()) {
                    entity.motionX *= 1.1;
                    entity.motionZ *= 1.1;
                    // ((EntityPlayer)entity).capabilities.isFlying=true;
                } else {
                    // ((EntityPlayer)entity).capabilities.isFlying=false;
                }
            }
        }
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
