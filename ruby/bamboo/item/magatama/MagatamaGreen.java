package ruby.bamboo.item.magatama;

import ruby.bamboo.entity.magatama.EntityMagatama;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MagatamaGreen implements IMagatama {

    @Override
    public int getColor() {
        return 0x33FF33;
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
            EntityPlayer player = (EntityPlayer) entity;
            if (player.getFoodStats().getFoodLevel() > 0 && player.fallDistance > 2) {
                player.fallDistance *= 0.9;
                player.getFoodStats().addExhaustion(5F);
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
