package ruby.bamboo.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

public class TileEntityJPChest extends TileEntity implements IInventory {
    public TileEntityJPChest() {
        chestContents = new ItemStack[54];
        adjacentChestChecked = false;
    }

    @Override
    public int getSizeInventory() {
        return 54;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return chestContents[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (chestContents[i] != null) {
            if (chestContents[i].stackSize <= j) {
                ItemStack itemstack = chestContents[i];
                chestContents[i] = null;
                markDirty();
                return itemstack;
            }

            ItemStack itemstack1 = chestContents[i].splitStack(j);

            if (chestContents[i].stackSize == 0) {
                chestContents[i] = null;
            }

            markDirty();
            return itemstack1;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        chestContents[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }

        markDirty();
    }

    @Override
    public String getInventoryName() {
        return "Chest";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
        chestContents = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;

            if (j >= 0 && j < chestContents.length) {
                chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < chestContents.length; i++) {
            if (chestContents[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                chestContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean canInteractWith(EntityPlayer entityplayer) {
        if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this) {
            return false;
        }

        return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
    }

    public void func_35144_b() {
        super.updateContainingBlockInfo();
        adjacentChestChecked = false;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        // func_35147_g();
        if ((++field_35154_q % 20) * 4 == 0) {
            worldObj.playAuxSFX(xCoord, yCoord, zCoord, 1, field_35156_h);
        }

        field_35149_g = field_35148_f;
        float f = 0.1F;

        if (field_35156_h > 0 && field_35148_f == 0.0F && adjacentChestZNeg == null && adjacentChestXNeg == null) {
            double d = xCoord + 0.5D;
            double d2 = zCoord + 0.5D;

            if (adjacentChestZPos != null) {
                d2 += 0.5D;
            }

            if (adjacentChestXPos != null) {
                d += 0.5D;
            }

            worldObj.playSoundEffect(d, yCoord + 0.5D, d2, "tile.piston.out", 0.4F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (field_35156_h == 0 && field_35148_f > 0.0F || field_35156_h > 0 && field_35148_f < 1.0F) {
            if (field_35156_h > 0) {
                field_35148_f += f;
            } else {
                field_35148_f -= f;
            }

            if (field_35148_f > 1.0F) {
                field_35148_f = 1.0F;
            }

            if (field_35148_f < 0.0F) {
                field_35148_f = 0.0F;

                if (adjacentChestZNeg == null && adjacentChestXNeg == null) {
                    double d1 = xCoord + 0.5D;
                    double d3 = zCoord + 0.5D;

                    if (adjacentChestZPos != null) {
                        d3 += 0.5D;
                    }

                    if (adjacentChestXPos != null) {
                        d1 += 0.5D;
                    }

                    worldObj.playSoundEffect(d1, yCoord + 0.5D, d3, "tile.piston.in", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
                }
            }
        }
    }

    public void func_35143_b(int i, int j) {
        if (i == 1) {
            field_35156_h = j;
        }
    }

    public void func_35142_x_() {
        field_35156_h++;
        worldObj.playAuxSFX(xCoord, yCoord, zCoord, 1, field_35156_h);
    }

    public void func_35141_y_() {
        field_35156_h--;
        worldObj.playAuxSFX(xCoord, yCoord, zCoord, 1, field_35156_h);
    }

    @Override
    public void invalidate() {
        func_35144_b();
        super.invalidate();
    }

    public ItemStack chestContents[];
    public boolean adjacentChestChecked;
    public TileEntityChest adjacentChestZNeg;
    public TileEntityChest adjacentChestXPos;
    public TileEntityChest adjacentChestXNeg;
    public TileEntityChest adjacentChestZPos;
    /*
     * public TileEntityChest adjacentChestZNeg; public TileEntityChest
     * adjacentChestXPos; public TileEntityChest adjacentChestXNeg; public
     * TileEntityChest adjacentChestZPos;
     */
    public float field_35148_f;
    public float field_35149_g;
    public int field_35156_h;
    private int field_35154_q;

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this) {
            return false;
        }

        return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
    }

    @Override
    public void openInventory() {
        field_35156_h++;
        worldObj.playAuxSFX(xCoord, yCoord, zCoord, 1, field_35156_h);
    }

    @Override
    public void closeInventory() {
        field_35156_h--;
        worldObj.playAuxSFX(xCoord, yCoord, zCoord, 1, field_35156_h);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        return this.chestContents[var1];
    }

    @Override
    public boolean hasCustomInventoryName() {
        // TODO 自動生成されたメソッド・スタブ
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

}
