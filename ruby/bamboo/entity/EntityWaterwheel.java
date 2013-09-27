package ruby.bamboo.entity;

import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityWaterwheel extends EntityMill
{
    public EntityWaterwheel(World par1World)
    {
        super(par1World);
        //setSize(3.5F,3F);
    }
    @Override
    public boolean handleWaterMovement()
    {
        if (this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this))
        {
            this.inWater = true;
        }
        else
        {
            this.inWater = false;
        }

        return this.inWater;
    }
    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.inWater)
        {
            roll = roll < 360 ? roll += 1 : 0;
        }
    }
    @Override
    public ItemStack getItem()
    {
        return new ItemStack(BambooInit.waterWheelIID, 1, 0);
    }
}
