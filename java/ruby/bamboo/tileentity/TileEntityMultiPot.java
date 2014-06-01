package ruby.bamboo.tileentity;

import java.util.Arrays;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ruby.bamboo.BambooInit;
import ruby.bamboo.block.BlockMultiPot;

public class TileEntityMultiPot extends TileEntity {

    public static final int MAX_LENGTH = 9;

    private boolean[] matrix = new boolean[MAX_LENGTH];
    private ItemStack[] slots = new ItemStack[MAX_LENGTH];
    public static final int SQ = (int) Math.sqrt(MAX_LENGTH);

    public TileEntityMultiPot() {
        Arrays.fill(matrix, false);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        ;
        if (!nbt.hasKey("slotsNBT", 9)) {
            nbt.setTag("slotsNBT", new NBTTagList());
        }
        NBTTagList list = nbt.getTagList("slotsNBT", 10);
        for (int i = 0; i < MAX_LENGTH; i++) {
            NBTTagCompound slotNBT = new NBTTagCompound();
            slotNBT.setBoolean("matrix", matrix[i]);
            if (matrix[i] && slots[i] != null) {
                NBTTagCompound itemNBT = new NBTTagCompound();
                slots[i].writeToNBT(itemNBT);
                slotNBT.setTag("itemNBT", itemNBT);
            }
            list.appendTag(slotNBT);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (!nbt.hasKey("slotsNBT", 9)) {
            nbt.setTag("slotsNBT", new NBTTagList());
        }
        NBTTagList list = nbt.getTagList("slotsNBT", 10);
        for (int i = 0; i < MAX_LENGTH && i < list.tagCount(); i++) {
            NBTTagCompound slotNBT = list.getCompoundTagAt(i);
            matrix[i] = slotNBT.getBoolean("matrix");
            if (matrix[i] && slotNBT.hasKey("itemNBT")) {
                NBTTagCompound itemNBT = (NBTTagCompound) slotNBT.getTag("itemNBT");
                slots[i] = ItemStack.loadItemStackFromNBT(itemNBT);
            }
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

    public static int getSlotPositionNumber(float x, float z) {
        int res = ((int) (SQ * x) + (int) ((SQ) * z) * SQ);
        return res < MAX_LENGTH ? res : MAX_LENGTH - 1;
    }

    public void addSlot(int num) {
        matrix[num] = true;
        this.markDirty();
    }

    public ItemStack removeSlot(int num) {
        ItemStack res = null;
        if (matrix[num]) {
            matrix[num] = false;
            res = slots[num];
            slots[num] = null;
            this.markDirty();
        }
        return res;
    }

    public ItemStack setItemOnSlotNumber(int slotNum, Item item, int meta) {
        ItemStack res = null;
        if (matrix[slotNum]) {
            if (slots[slotNum] != null) {
                res = slots[slotNum];
            }
            slots[slotNum] = new ItemStack(item, 1, meta);
        }
        this.markDirty();
        return res;
    }

    public Item getFlowerPotItem(int slotNum) {
        Item item = null;
        if (slotNum < MAX_LENGTH && slots[slotNum] != null) {
            item = slots[slotNum].getItem();
        }
        return item;
    }

    public int getFlowerPotData(int slotNum) {
        int meta = 0;
        if (slotNum < MAX_LENGTH && slots[slotNum] != null) {
            meta = slots[slotNum].getItemDamage();
        }
        return meta;
    }

    public boolean isEnable(int slotNum) {
        return matrix[slotNum];
    }

    public void dropAllItems(World world) {
        if (!world.isRemote) {
            for (int i = 0; i < MAX_LENGTH; i++) {
                if (matrix[i]) {
                    ((BlockMultiPot) this.blockType).dropBlockAsItem(world, this.xCoord, this.yCoord, this.zCoord, new ItemStack(BambooInit.multiPot));
                    if (slots[i] != null) {
                        ((BlockMultiPot) this.blockType).dropBlockAsItem(world, this.xCoord, this.yCoord, this.zCoord, slots[i]);
                    }
                }
            }
        }
    }

    public boolean isEmpty() {
        for (int i = 0; i < MAX_LENGTH; i++) {
            if (matrix[i]) {
                return false;
            }
        }
        return true;
    }
}
