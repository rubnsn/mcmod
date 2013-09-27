package ruby.bamboo.entity;

import ruby.bamboo.BambooInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityObon extends Entity
{
    private static final byte ITEMID = 17;
    private static final byte ITEMDMG = 18;
    private EntityItem entityItem;
    public EntityObon(World par1World)
    {
        super(par1World);
        setSize(1.0F, 0.25F);
    }
    @Override
    public boolean func_130002_c(EntityPlayer par1EntityPlayer)
    {
        ItemStack is = par1EntityPlayer.getCurrentEquippedItem();

        if (is != null && is.getItem() instanceof ItemFood)
        {
            if (getItemID() == 0)
            {
                setDisplayItem(is);
            }
            else
            {
                changeItem(is);
                setDisplayItem(is);
            }

            return true;
        }

        return false;
    }
    private void setDisplayItem(ItemStack is)
    {
        setItemID(is.getItem().itemID);
        setItemDmg(is.getItemDamage());
        is.stackSize--;
    }
    private void changeItem(ItemStack is)
    {
        if (!worldObj.isRemote)
        {
            this.entityDropItem(new ItemStack(getItemID(), 1, getItemDmg()), 1F);
            setItemID(0);
        }

        entityItem = null;
    }
    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }
    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (!worldObj.isRemote)
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

            if (getItemID() != 0)
            {
                this.entityDropItem(new ItemStack(getItemID(), 1, getItemDmg()), 1F);
            }

            this.entityDropItem(new ItemStack(BambooInit.obonIID, 1, 0), 1F);
        }

        return false;
    }
    @Override
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }
    @Override
    protected boolean pushOutOfBlocks(double par1, double par3, double par5)
    {
        return false;
    }
    @Override
    protected void entityInit()
    {
        dataWatcher.addObject(ITEMID, 0);
        dataWatcher.addObject(ITEMDMG, 0);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        setItemID(nbttagcompound.getInteger("displayItemID"));
        setItemDmg(nbttagcompound.getInteger("displayItemDmg"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setInteger("displayItemID", getItemID());
        nbttagcompound.setInteger("displayItemDmg", getItemDmg());
    }

    private void setItemID(int itemID)
    {
        dataWatcher.updateObject(ITEMID, itemID);
    }
    private int getItemID()
    {
        return dataWatcher.getWatchableObjectInt(ITEMID);
    }
    private void setItemDmg(int itemDmg)
    {
        dataWatcher.updateObject(ITEMDMG, itemDmg);
    }
    private int getItemDmg()
    {
        return dataWatcher.getWatchableObjectInt(ITEMDMG);
    }
    public EntityItem getEntityItem()
    {
        if (entityItem == null)
        {
            if (getItemID() != 0)
            {
                entityItem = new EntityItem(worldObj);
                entityItem.hoverStart = 0;
                entityItem.setEntityItemStack(new ItemStack(getItemID(), 1, getItemDmg()));
            }
        }

        return entityItem;
    }
}
