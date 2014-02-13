package ruby.bamboo.item.magatama;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.entity.magatama.EntityFlareEffect;
import ruby.bamboo.entity.magatama.EntityMagatama;

public class MagatamaRed implements IMagatama {

    @Override
    public Class getEffectClass() {
        return EntityFlareEffect.class;
    }

    @Override
    public void holdingEffect(Entity entity, int invIndex) {
        entity.extinguish();
    }

    @Override
    public int getReality() {
        return 1;
    }

    @Override
    public int getColor() {
        return 0xDD2222;
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
