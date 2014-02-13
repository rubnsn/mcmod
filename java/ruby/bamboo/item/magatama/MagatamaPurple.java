package ruby.bamboo.item.magatama;

import ruby.bamboo.entity.magatama.EntityGravityHole;
import ruby.bamboo.entity.magatama.EntityMagatama;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MagatamaPurple implements IMagatama {

    @Override
    public Class getEffectClass() {
        return EntityGravityHole.class;
    }

    @Override
    public void holdingEffect(Entity entity, int invIndex) {
        if (entity instanceof EntityLivingBase) {
            // 弱体化
            if (((EntityLivingBase) entity).isPotionActive(18)) {
                ((EntityLivingBase) entity).removePotionEffect(18);
                // ぽいずん
            } else if (((EntityLivingBase) entity).isPotionActive(19)) {
                ((EntityLivingBase) entity).removePotionEffect(19);
            }
        }
    }

    @Override
    public int getReality() {
        return 1;
    }

    @Override
    public int getColor() {
        return 0x6600FF;
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
