package ruby.bamboo.tileentity;

import java.util.Random;

import ruby.bamboo.block.BlockCampfire;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class TileEntityCampfire extends TileEntityFurnace {
    private int meatroll;

    public TileEntityCampfire() {
        meatroll = new Random().nextInt(360);
    }

    public int getMeatroll() {
        return meatroll;
    }

    @Override
    public void updateEntity() {
        meatroll = meatroll < 360 ? ++meatroll : 0;
        boolean var1 = this.furnaceBurnTime > 0;
        boolean var2 = false;

        if (this.furnaceBurnTime > 0) {
            --this.furnaceBurnTime;
        }

        if (!this.worldObj.isRemote) {
            if (this.furnaceBurnTime == 0 && this.canSmelt()) {
                this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.getStackInSlot(1));

                if (this.furnaceBurnTime > 0) {
                    var2 = true;

                    if (this.getStackInSlot(1) != null) {
                        --this.getStackInSlot(1).stackSize;

                        if (this.getStackInSlot(1).stackSize == 0) {
                            this.setInventorySlotContents(1, this.getStackInSlot(1).getItem().getContainerItemStack(this.getStackInSlot(1)));
                        }
                    }
                }
            }

            if (this.isBurning() && this.canSmelt()) {
                ++this.furnaceCookTime;

                if (this.furnaceCookTime == 200) {
                    this.furnaceCookTime = 0;
                    this.smeltItem();
                    var2 = true;
                }
            } else {
                this.furnaceCookTime = 0;
            }

            if (var1 != this.furnaceBurnTime > 0) {
                var2 = true;
            }
        }

        if (!worldObj.isRemote) {
            byte meta = 0;

            if (getStackInSlot(2) != null) {
                if (getStackInSlot(2).itemID == Item.fishCooked.itemID) {
                    meta = 1;
                } else if (getStackInSlot(2).itemID == Item.porkCooked.itemID || getStackInSlot(2).itemID == Item.beefCooked.itemID) {
                    meta = 2;
                } else {
                    meta = 3;
                }
            }

            // System.out.println(meta!=getBlockMetadata()>>2);
            if (meta != getBlockMetadata() >> 2) {
                this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, (meta << 2) | (getBlockMetadata() & 3), 3);
                BlockCampfire.updateFurnaceBlockState(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (var2) {
            this.onInventoryChanged();
        }
    }

    private boolean canSmelt() {
        if (this.getStackInSlot(0) == null) {
            return false;
        } else {
            ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.getStackInSlot(0));

            if (var1 == null) {
                return false;
            }

            if (this.getStackInSlot(2) == null) {
                return true;
            }

            if (!this.getStackInSlot(2).isItemEqual(var1)) {
                return false;
            }

            int result = this.getStackInSlot(2).stackSize + var1.stackSize;
            return (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
        }
    }
}
