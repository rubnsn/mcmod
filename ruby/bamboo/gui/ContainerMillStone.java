package ruby.bamboo.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.GrindRegistory;
import ruby.bamboo.tileentity.TileEntityMillStone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMillStone extends Container
{
    private TileEntityMillStone tileEntity;
    private int lastGrindMotion;
    private int lastProgressTime;
    private int lastisGraind;
    public  ContainerMillStone(InventoryPlayer par1InventoryPlayer, TileEntityMillStone tileEntity)
    {
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new Slot(tileEntity, 0, 80, 9));
        this.addSlotToContainer(new SlotMillStone(tileEntity, 1, 58, 57));
        this.addSlotToContainer(new SlotMillStone(tileEntity, 2, 102, 57));
        //所持品
        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
        }
    }
    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.tileEntity.grindMotion);
        par1ICrafting.sendProgressBarUpdate(this, 1, this.tileEntity.getProgress());
    }
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (lastGrindMotion != tileEntity.grindMotion)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.grindMotion);
            }

            if (lastProgressTime != tileEntity.getProgress())
            {
                icrafting.sendProgressBarUpdate(this, 1, tileEntity.getProgress());
            }

            if (lastisGraind != (tileEntity.isGrind()))
            {
                icrafting.sendProgressBarUpdate(this, 2, tileEntity.isGrind());
            }
        }

        this.lastGrindMotion = tileEntity.grindMotion;
        this.lastProgressTime = tileEntity.getProgress();
        this.lastisGraind = tileEntity.grindTime;
    }
    @Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.tileEntity.grindMotion = par2;
        }

        if (par1 == 1)
        {
            this.tileEntity.progress = par2;
        }

        if (par1 == 2)
        {
            this.tileEntity.isGrind = par2;
        }
    }
    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return this.tileEntity.isUseableByPlayer(entityplayer);
    }
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 1 || par2 == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 != 0)
            {
                if (GrindRegistory.getOutput(itemstack1) != null)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 3 && par2 < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 30 && par2 < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }
}
