package ruby.bamboo.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruby.bamboo.BambooInit;

public class EntityObon extends Entity {
    private static final byte ITEM_DATA = 17;
    public static final ItemStack EMPTY = new ItemStack(BambooInit.obon, 0, 0);
    public static final byte MAX_SIZE = 5;

    public EntityObon(World par1World) {
        super(par1World);
        setSize(1.0F, 0.25F);
    }

    @Override
    public boolean interactFirst(EntityPlayer par1EntityPlayer) {
        ItemStack is = par1EntityPlayer.getCurrentEquippedItem();

        if (is != null && is.getItem() instanceof Item && !(is.getItem() instanceof ItemBlock) && is.getItem() != BambooInit.obon) {
            ItemStack itemData = is.copy();
            itemData.stackSize = 1;
            if (this.setItemData(itemData)) {
                is.stackSize--;
                return true;
            }
        }

        return false;
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
            for (int i = 0; i < MAX_SIZE; i++) {
                if (getItemData(i) != EMPTY) {
                    this.entityDropItem(this.getItemData(i), 1F);
                }
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
        //if (EMPTY == null) {
        //}
        for (int i = 0; i < MAX_SIZE; i++) {
            dataWatcher.addObject(ITEM_DATA + i, EMPTY);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("displayItemList2")) {
            NBTTagList list = nbt.getTagList("displayItemList2", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound comp = list.getCompoundTagAt(i);
                if (!this.setItemData(ItemStack.loadItemStackFromNBT(comp), i)) {
                    return;
                }
            }
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < MAX_SIZE; i++) {
            NBTTagCompound comp = new NBTTagCompound();
            this.getItemData(i).writeToNBT(comp);
            list.appendTag(comp);
        }
        nbt.setTag("displayItemList2", list);
    }

    public boolean setItemData(ItemStack itemData) {
        for (int i = 0; i < MAX_SIZE; i++) {
            if (this.getItemData(i) == EMPTY) {
                return this.setItemData(itemData, i);
            }
        }
        return false;
    }

    private boolean setItemData(ItemStack itemData, int num) {
        if (num < MAX_SIZE) {
            dataWatcher.updateObject(ITEM_DATA + num, itemData != null ? itemData : EMPTY);
            return true;
        }
        return false;
    }

    public ItemStack getItemData(int num) {
        if (num < MAX_SIZE) {
            ItemStack is = dataWatcher.getWatchableObjectItemStack(ITEM_DATA + num);
            return is != null ? is : EMPTY;
        }
        return EMPTY;
    }

}
