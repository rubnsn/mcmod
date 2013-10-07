package ruby.bamboo.entity;

import ruby.bamboo.BambooInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityWindChime extends Entity
{
    public float rotx;
    public float roty;
    public float rotz;
    private float prevX;
    private float prevY;
    private float prevZ;

    public EntityWindChime(World par1World)
    {
        super(par1World);
        setSize(0.5f, 1.0f);
    }
    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (worldObj.getWorldTime() % 100 == 0)
        {
            prevX = rand.nextInt(20) - 10;
            //prevZ=rand.nextInt(10)-5;
            prevY = rand.nextInt(20) - 10;
        }

        if (Math.abs(rotx) > 10)
        {
            prevX = -rotx / 3;
        }
        else if (Math.abs(roty) > 10)
        {
            prevY = -roty / 3;
        }
        else if (worldObj.getWorldTime() % 20 == 0)
        {
            prevX = -prevX;
            prevY = -prevY;
            //prevZ=-prevZ;
        }

        rotx += prevX / 6000;
        roty += prevY / 6000;

        //rotz+=prevZ/6000;
        if (rand.nextInt(500) == 0)
        {
            this.worldObj.playSoundAtEntity(this, "random.orb", 0.8F, 1.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
        }
    }
    @Override
    protected void entityInit()
    {
    }
    //1.3.2用少し時間が経つとYが1.5ほど上がるバグの対策
    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
    }
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (!isDead && !this.worldObj.isRemote)
        {
            EntityPlayer entityplayer = null;

            if (par1DamageSource.damageType == "player")
            {
                entityplayer = (EntityPlayer)par1DamageSource.getEntity();
                setDead();
            }
            else
            {
                return false;
            }

            if (entityplayer != null && entityplayer.capabilities.isCreativeMode)
            {
                return true;
            }

            this.entityDropItem(new ItemStack(BambooInit.windChimeIID, 1, 0), 1F);
        }

        return true;
    }
    @Override
    public void readEntityFromNBT(NBTTagCompound var1)
    {
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound var1)
    {
    }
    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }
    @Override
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }
    @Override
    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return boundingBox;
    }
    @Override
    public boolean isBurning()
    {
        return false;
    }
}
