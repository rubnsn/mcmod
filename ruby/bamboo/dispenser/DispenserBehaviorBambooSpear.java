package ruby.bamboo.dispenser;

import ruby.bamboo.entity.EntityBambooSpear;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

public class DispenserBehaviorBambooSpear extends BehaviorProjectileDispense
{
    @Override
    protected IProjectile getProjectileEntity(World world, IPosition iposition)
    {
        // TODO 自動生成されたメソッド・スタブ
        return new EntityBambooSpear(world, iposition.getX(), iposition.getY(), iposition.getZ());
    }
}
