package ruby.bamboo.entity;

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
import ruby.bamboo.BambooInit;

public class EntityObon extends Entity {
    private static final byte ITEMID = 17;
    private static final byte ITEMDMG = 18;
    private static final String NOTHING = "";
    private EntityItem entityItem;

    public EntityObon(World par1World) {
        super(par1World);
        setSize(1.0F, 0.25F);
    }

    @Override
    public boolean interactFirst(EntityPlayer par1EntityPlayer) {
        ItemStack is = par1EntityPlayer.getCurrentEquippedItem();

        if (is != null && is.getItem() instanceof ItemFood) {
            if (getItemName() == NOTHING) {
                setDisplayItem(is);
            } else {
                changeItem(is);
                setDisplayItem(is);
            }

            return true;
        }

        return false;
    }

    private void setDisplayItem(ItemStack is) {
        setItemName(Item.itemRegistry.getNameForObject(is.getItem()));
        setItemDmg(is.getItemDamage());
        is.stackSize--;
    }

    private void changeItem(ItemStack is) {
        if (!worldObj.isRemote) {
            this.entityDropItem(new ItemStack((Item) Item.itemRegistry.getObject(getItemName()), 1, getItemDmg()), 1F);
            setItemName(NOTHING);
        }

        entityItem = null;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
        if (!worldObj.isRemote) {
            EntityPlayer entityplayer = null;

            if (par1DamageSource.damageType == "player") {
                entityplayer = (EntityPlayer) par1DamageSource.getEntity();
                setDead();
            } else {
                return false;
            }

            if (entityplayer != null && entityplayer.capabilities.isCreativeMode) {
                return true;
            }

            if (getItemName() != NOTHING) {
                this.entityDropItem(new ItemStack((Item) Item.itemRegistry.getObject(getItemName()), 1, getItemDmg()), 1F);
            }

            this.entityDropItem(new ItemStack(BambooInit.obon, 1, 0), 1F);
        }

        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !isDead;
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObject(ITEMID, 0);
        dataWatcher.addObject(ITEMDMG, 0);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("displayItemName")) {
            setItemName(nbttagcompound.getString("displayItemName"));
        }
        setItemDmg(nbttagcompound.getInteger("displayItemDmg"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("displayItemName", getItemName());
        nbttagcompound.setInteger("displayItemDmg", getItemDmg());
    }

    private void setItemName(String itemID) {
        dataWatcher.updateObject(ITEMID, itemID);
    }

    private String getItemName() {
        return dataWatcher.getWatchableObjectString(ITEMID);
    }

    private void setItemDmg(int itemDmg) {
        dataWatcher.updateObject(ITEMDMG, itemDmg);
    }

    private int getItemDmg() {
        return dataWatcher.getWatchableObjectInt(ITEMDMG);
    }

    public EntityItem getEntityItem() {
        if (entityItem == null) {
            if (getItemName() != NOTHING) {
                entityItem = new EntityItem(worldObj);
                entityItem.hoverStart = 0;
                entityItem.setEntityItemStack(new ItemStack((Item) Item.itemRegistry.getObject(getItemName()), 1, getItemDmg()));
            }
        }

        return entityItem;
    }
}
