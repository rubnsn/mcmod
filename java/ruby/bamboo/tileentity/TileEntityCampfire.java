package ruby.bamboo.tileentity;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import ruby.bamboo.block.BlockCampfire;
import ruby.bamboo.item.crafting.CookingManager;

public class TileEntityCampfire extends TileEntity implements ISidedInventory {
    private int meatroll;
    private static final int[] slotsTop = new int[] { 0 };
    private static final int[] slotsBottom = new int[] { 9, 10 };
    private static final int[] slotsSides = new int[] { 9 };
    private ItemStack[] slots = new ItemStack[11];
    private ItemStack[] copyMatrix = new ItemStack[9];
    private static final byte SLOT_FUEL = 9;
    private static final byte SLOT_RESULT = 10;
    private static final int MAX_FUEL = 102400;
    public int fuel;
    public int cookTime;
    private boolean isBurn = false;
    public ItemStack nowCookingResult;
    //クライアント側GUI
    public int fuelRatio;
    public int cookRatio;

    public TileEntityCampfire() {
        meatroll = new Random().nextInt(360);
    }

    public int getMeatroll() {
        return meatroll;
    }

    @Override
    public void updateEntity() {
        updateFuel();
        updateCooking();
        updateRender();
    }

    void updateCooking() {
        if (!worldObj.isRemote) {
            if (199 < fuel && !isBurn && !isEmpty() && this.canCooking()) {
                if (slots[SLOT_RESULT] == null) {
                    isBurn = true;
                    cookTime = 200;
                    this.setMatrix();
                } else if (nowCookingResult.isItemEqual(slots[SLOT_RESULT]) && slots[SLOT_RESULT].stackSize < slots[SLOT_RESULT].getMaxStackSize()) {
                    isBurn = true;
                    cookTime = 200;
                    this.setMatrix();
                }
            } else {
                updateBurn();
            }
        }

    }

    private boolean isEmpty() {
        for (int i = 0; i < 9; i++) {
            if (slots[i] != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    private boolean canCooking() {
        return (nowCookingResult = CookingManager.getInstance().findMatchingRecipe(slots, this.getWorldObj())) != null;
    }

    private void updateBurn() {
        if (isBurn) {
            if (--cookTime <= 0) {
                if (nowCookingResult != null) {
                    ItemStack nowMatrix = CookingManager.getInstance().findMatchingRecipe(slots, this.getWorldObj());
                    if (nowMatrix != null && nowMatrix.isItemEqual(nowCookingResult)) {
                        if (slots[SLOT_RESULT] == null) {
                            materialConsumption();
                            slots[SLOT_RESULT] = nowCookingResult.copy();
                            slots[SLOT_RESULT].stackSize = 1;
                        } else if (nowCookingResult.isItemEqual(slots[SLOT_RESULT]) && slots[SLOT_RESULT].stackSize < slots[SLOT_RESULT].getMaxStackSize()) {
                            materialConsumption();
                            slots[SLOT_RESULT].stackSize++;
                        }
                    }
                }
                nowCookingResult = null;
                isBurn = false;
                cookTime = 200;
            }
            if ((cookTime & 1) == 0) {
                if (!this.chkMtrix()) {
                    nowCookingResult = null;
                    isBurn = false;
                    cookTime = 200;
                }

            }
        }
    }

    private void setMatrix() {
        for (int i = 0; i < 9; i++) {
            copyMatrix[i] = getStackInSlot(i);
        }
    }

    private boolean chkMtrix() {
        for (int i = 0; i < 9; i++) {
            if (copyMatrix[i] != slots[i]) {
                return false;
            }
        }
        return true;
    }

    private void materialConsumption() {
        for (int i = 0; i < 9; i++) {
            if (slots[i] != null) {
                if (--slots[i].stackSize == 0) {
                    slots[i] = null;
                }
            }
        }
        fuel -= 200;
    }

    void updateFuel() {
        if (!worldObj.isRemote) {
            if (TileEntityFurnace.isItemFuel(slots[SLOT_FUEL])) {
                int slotFuel = TileEntityFurnace.getItemBurnTime(slots[SLOT_FUEL]);
                if (fuel + slotFuel <= MAX_FUEL) {
                    fuel += slotFuel;
                    if (--slots[SLOT_FUEL].stackSize == 0) {
                        slots[SLOT_FUEL] = slots[SLOT_FUEL].getItem().getContainerItem(slots[SLOT_FUEL]);
                    }

                }
            }
        }
    }

    public ItemStack[] getSlots() {
        return slots;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        fuel = nbt.getInteger("fuel");
        cookTime = nbt.getInteger("cookTime");
        if (nbt.hasKey("nowItem")) {
            nowCookingResult = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbt.getTag("nowItem"));
        }
        NBTTagList nbtList = new NBTTagList();
        if (!nbt.hasKey("slotsNBT", 9)) {
            nbt.setTag("slotsNBT", new NBTTagList());
        }
        NBTTagList list = nbt.getTagList("slotsNBT", 10);
        for (int i = 0; i < slots.length && i < list.tagCount(); i++) {
            NBTTagCompound nbtCompound = list.getCompoundTagAt(i);
            if (nbtCompound.hasKey("itemNBT")) {
                slots[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbtCompound.getTag("itemNBT"));
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("fuel", fuel);
        nbt.setInteger("cookTime", cookTime);
        if (nowCookingResult != null) {
            nbt.setTag("nowItem", nowCookingResult.writeToNBT(new NBTTagCompound()));
        }
        if (!nbt.hasKey("slotsNBT", 9)) {
            nbt.setTag("slotsNBT", new NBTTagList());
        }
        NBTTagList list = nbt.getTagList("slotsNBT", 10);
        NBTTagCompound nbtCompound;
        for (int i = 0; i < slots.length; i++) {
            nbtCompound = new NBTTagCompound();
            if (slots[i] != null) {
                NBTTagCompound itemCompound = new NBTTagCompound();
                slots[i].writeToNBT(itemCompound);
                nbtCompound.setTag("itemNBT", itemCompound);
            }
            list.appendTag(nbtCompound);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 5, var1);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    void updateRender() {
        meatroll = meatroll < 360 ? ++meatroll : 0;
        if (!worldObj.isRemote) {
            byte meta = 0;

            if (getStackInSlot(SLOT_RESULT) != null) {
                if (getStackInSlot(SLOT_RESULT).getItem() == Items.cooked_fished) {
                    meta = 1;
                } else if (getStackInSlot(SLOT_RESULT).getItem() == Items.cooked_porkchop || getStackInSlot(SLOT_RESULT).getItem() == Items.cooked_beef) {
                    meta = 2;
                } else {
                    meta = 3;
                }
            }

            if (meta != getBlockMetadata() >> 2) {
                this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, (meta << 2) | (getBlockMetadata() & 3), 3);
                BlockCampfire.updateFurnaceBlockState(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return this.slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return this.slots[var1];
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        if (this.slots[var1] != null) {
            ItemStack itemstack;

            if (this.slots[var1].stackSize <= var2) {
                itemstack = this.slots[var1];
                this.slots[var1] = null;
                return itemstack;
            } else {
                itemstack = this.slots[var1].splitStack(var2);

                if (this.slots[var1].stackSize == 0) {
                    this.slots[var1] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        if (this.slots[var1] != null) {
            ItemStack itemstack = this.slots[var1];
            this.slots[var1] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        this.slots[var1] = var2;
        if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
            var2.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return "bamboo.container.campfire";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return var1 == 2 ? false : (var1 == 9 ? TileEntityFurnace.isItemFuel(var2) : true);

    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return var1 == 0 ? slotsBottom : (var1 == 1 ? slotsTop : slotsSides);
    }

    @Override
    public boolean canInsertItem(int var1, ItemStack var2, int var3) {
        return this.isItemValidForSlot(var1, var2);
    }

    @Override
    public boolean canExtractItem(int var1, ItemStack var2, int var3) {
        return var3 != 0 || var1 != 1 || var2.getItem() == Items.bucket;
    }

    public int getCookAmount() {
        return this.getRatio(cookTime, 200, 100);
    }

    public int getFuelAmount() {
        return this.getRatio(fuel, MAX_FUEL, 100);
    }

    private int getRatio(float par0, float par1, int par3) {
        return Math.round(par0 / par1 * par3);
    }

}
