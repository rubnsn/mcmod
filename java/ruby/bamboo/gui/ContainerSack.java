package ruby.bamboo.gui;

import ruby.bamboo.BambooInit;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;

public class ContainerSack extends Container {
    private ItemStack itemStack;
    private InventorySack inventry;

    public ContainerSack(InventoryPlayer par1InventoryPlayer, ItemStack par2ItemStack) {
        itemStack = par2ItemStack;
        inventry = new InventorySack(par2ItemStack);
        this.addSlotToContainer(new Slot(inventry, 0, 80, 33));
        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == BambooInit.itemSackIID;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer) {
        if (par1EntityPlayer.getCurrentEquippedItem() != null && par1EntityPlayer.getCurrentEquippedItem().itemID == BambooInit.itemSackIID) {
            ItemStack item = inventry.getStackInSlotOnClosing(0);

            if (item != null && item.getTagCompound() != null) {
                if (isStorage(Item.itemsList[item.getTagCompound().getShort("type")])) {
                    par1EntityPlayer.getCurrentEquippedItem().setTagCompound(item.getTagCompound());
                } else {
                    if (!par1EntityPlayer.worldObj.isRemote) {
                        par1EntityPlayer.worldObj.spawnEntityInWorld(new EntityItem(par1EntityPlayer.worldObj, par1EntityPlayer.posX, par1EntityPlayer.posY + 0.5, par1EntityPlayer.posZ, inventry.slot0));
                    }
                }
            }
        } else {
            if (inventry.slot0 != null) {
                if (!par1EntityPlayer.worldObj.isRemote) {
                    par1EntityPlayer.worldObj.spawnEntityInWorld(new EntityItem(par1EntityPlayer.worldObj, par1EntityPlayer.posX, par1EntityPlayer.posY + 0.5, par1EntityPlayer.posZ, inventry.slot0));
                }
            }
        }

        super.onContainerClosed(par1EntityPlayer);
    }

    private boolean isStorage(Item item) {
        return item instanceof ItemBlock ? true : item instanceof ItemSeeds ? true : item instanceof ItemSeedFood ? true : false;
    }
}
