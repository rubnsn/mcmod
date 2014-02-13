package ruby.bamboo.dispenser;

import ruby.bamboo.entity.EntityFirecracker;
import ruby.bamboo.item.ItemFirecracker;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

public class DispenserBehaviorFireCracker extends BehaviorProjectileDispenseEX {
    @Override
    protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition, int damage) {
        return new EntityFirecracker(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ(), ItemFirecracker.getExplodeLv(damage));
    }
}
