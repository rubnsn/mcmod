package ruby.bamboo.dispenser;

import ruby.bamboo.entity.EntityDirtySnowball;
import ruby.bamboo.item.ItemDirtySnowball;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

public class DispenserBehaviorDirtySnowball extends
        BehaviorProjectileDispenseEX {
    @Override
    protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition, int damage) {
        return new EntityDirtySnowball(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ(), ItemDirtySnowball.getEDS(damage));
    }
}
