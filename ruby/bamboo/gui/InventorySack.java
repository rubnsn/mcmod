package ruby.bamboo.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventorySack implements IInventory {
    private ItemStack sack;
    private short count;
    private short type;
    private short meta;
    public ItemStack slot0;

    public InventorySack(ItemStack itamStack) {
        this.sack = itamStack;
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return slot0;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (slot0 != null) {
            ItemStack is = slot0.copy();
            slot0 = null;
            return is;
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (sack != null && slot0 != null) {
            sack.setTagCompound(new NBTTagCompound());
            NBTTagCompound var4 = sack.getTagCompound();
            var4.setString("type", Item.itemRegistry.getNameForObject(slot0.getItem()));
            var4.setShort("count", (short) slot0.stackSize);
            var4.setShort("meta", (short) slot0.getItemDamage());
            sack.setItemDamage(sack.getMaxDamage() - 1);
        }

        return sack;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        if (itemstack != null) {
            slot0 = itemstack;
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }
}
