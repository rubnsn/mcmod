package ruby.bamboo.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

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
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return slot0;
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        if (this.slot0 != null) {
            ItemStack itemstack;

            if (this.slot0.stackSize <= par2) {
                itemstack = this.slot0;
                this.slot0 = null;
                return itemstack;
            } else {
                itemstack = this.slot0.splitStack(par2);

                if (this.slot0.stackSize == 0) {
                    this.slot0 = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (this.slot0 != null) {
            ItemStack itemstack = this.slot0;
            this.slot0 = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        this.slot0 = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
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
