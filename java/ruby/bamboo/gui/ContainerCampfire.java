package ruby.bamboo.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import ruby.bamboo.tileentity.TileEntityCampfire;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerCampfire extends Container {
    private TileEntityCampfire tile;
    private int lastFuelAmount;
    private int lastCookAmount;

    public ContainerCampfire(InventoryPlayer inventoryPlayer, TileEntityCampfire tileEntity) {
        this.tile = tileEntity;
        int slotNum = 0;
        int i;
        //素材
        for (i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(tile, slotNum++, 30 + i % 3 * 18, 17 + i / 3 * 18));
        }
        //燃料
        this.addSlotToContainer(new Slot(tileEntity, slotNum++, 8, 53));
        //出来上がり
        this.addSlotToContainer(new SlotCampfire(inventoryPlayer.player, tileEntity, slotNum++, 124, 35));

        // 所持品
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        int ratio;
        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            ratio = tile.getFuelAmount();
            if (lastFuelAmount != ratio) {
                icrafting.sendProgressBarUpdate(this, 0, ratio);
            }
            ratio = tile.getCookAmount();
            if (lastFuelAmount != ratio) {
                icrafting.sendProgressBarUpdate(this, 1, ratio);
            }
        }
        lastFuelAmount = tile.getFuelAmount();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if (par1 == 0) {
            this.tile.fuelRatio = par2;
        }
        if (par1 == 1) {
            this.tile.cookRatio = par2;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return this.tile.isUseableByPlayer(entityplayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 0) {
                if (!this.mergeItemStack(itemstack1, 12, 46, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (par2 >= 11 && par2 < 38) {
                if (!this.mergeItemStack(itemstack1, 38, 46, false)) {
                    return null;
                }
            } else if (par2 >= 38 && par2 < 47) {
                if (!this.mergeItemStack(itemstack1, 11, 37, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 11, 46, false)) {
                return null;
            }

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

}
