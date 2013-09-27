package ruby.bamboo.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BehaviorProjectileDispenseEX extends BehaviorProjectileDispense
{
    @Override
    public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        World world = par1IBlockSource.getWorld();
        IPosition iposition = BlockDispenser.getIPositionFromBlockSource(par1IBlockSource);
        EnumFacing enumfacing = BlockDispenser.getFacing(par1IBlockSource.getBlockMetadata());
        IProjectile iprojectile = this.getProjectileEntity(world, iposition, par2ItemStack.getItemDamage());
        iprojectile.setThrowableHeading((double)enumfacing.getFrontOffsetX(), (double)((float)enumfacing.getFrontOffsetY() + 0.1F), (double)enumfacing.getFrontOffsetZ(), this.func_82500_b(), this.func_82498_a());
        world.spawnEntityInWorld((Entity)iprojectile);
        par2ItemStack.splitStack(1);
        return par2ItemStack;
    }
    protected abstract IProjectile getProjectileEntity(World par1World, IPosition par2IPosition, int damage);
    @Override
    protected IProjectile getProjectileEntity(World world, IPosition iposition)
    {
        return null;
    }
}
