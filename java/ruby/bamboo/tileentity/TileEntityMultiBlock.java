package ruby.bamboo.tileentity;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityMultiBlock extends TileEntity {
    private byte slotLength;
    private ItemStack[][][] slots;
    private float[] posPartition;

    public TileEntityMultiBlock() {
        this.setSlotLength((byte) 3);
    }

    public void setSlotLength(byte len) {
        if (len < 1 || 64 < len) {
            len = 3;
        }
        this.slotLength = len;
        this.setPosPatition();
        slots = new ItemStack[this.slotLength][this.slotLength][this.slotLength];

    }

    private void setPosPatition() {
        float[] tmp = new float[this.slotLength];
        for (int i = 0; i < this.slotLength; i++) {
            tmp[i] = ((int) ((1 / (float) (this.slotLength)) * (i + 1) * 1000)) / 1000F;
        }
        posPartition = tmp;
    }

    public byte getFieldSize() {
        return this.slotLength;
    }

    public float[] getRenderOffset() {
        float[] o = new float[this.slotLength];
        int count = 0;
        for (int i = 0; i < this.slotLength; i++) {
            o[i] = i * (1 / (float) (this.slotLength));
        }
        return o;
    }

    public Block getInnerBlock(int x, int y, int z) {
        Block res = Blocks.air;
        if (isInnerRange(x, y, z)) {
            if (slots[x][y][z] != null) {
                res = Block.getBlockFromItem(slots[x][y][z].getItem());
            }
        }
        return res;
    }

    public int getInnerMeta(int x, int y, int z) {
        int res = 0;
        if (isInnerRange(x, y, z)) {
            if (slots[x][y][z] != null) {
                res = slots[x][y][z].getItemDamage();
            }
        }
        return res;
    }

    public ItemStack getInnerItemStack(int x, int y, int z) {
        return isInnerRange(x, y, z) ? slots[x][y][z] : null;
    }

    public boolean isExist(int x, int y, int z) {
        if (isInnerRange(x, y, z)) {
            return slots[x][y][z] != null;
        }
        return false;
    }

    public Block getOffsetedInnerBlock(int x, int y, int z, ForgeDirection fd) {
        return getInnerBlock(x + fd.offsetX, y + fd.offsetY, z + fd.offsetZ);
    }

    public boolean isEmpty() {
        for (int x = 0; x < this.slotLength; x++) {
            for (int y = 0; y < this.slotLength; y++) {
                for (int z = 0; z < this.slotLength; z++) {
                    if (slots[x][y][z] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean setInnerBlock(float hitX, float hitY, float hitZ, int side, ItemStack is) {
        byte[] innerPos = getInnerPos(hitX, hitY, hitZ, side);
        if (isInnerRange(innerPos[0], innerPos[1], innerPos[2])) {
            if (getInnerBlock(innerPos[0], innerPos[1], innerPos[2]) == Blocks.air) {
                is.stackSize = 1;
                this.setInnerSlot(innerPos[0], innerPos[1], innerPos[2], is);
                return true;
            }
        }
        return false;
    }

    public ItemStack removeInnerBlock(float hitX, float hitY, float hitZ, int side) {
        byte[] innerPos = getInnerPos(hitX, hitY, hitZ, ForgeDirection.OPPOSITES[side]);
        ItemStack res = getInnerItemStack(innerPos[0], innerPos[1], innerPos[2]);
        if (res != null) {
            this.removeInnerSlot(innerPos[0], innerPos[1], innerPos[2]);
        }
        return res;
    }

    public void setInnerSlot(byte innerX, byte innerY, byte innerZ, ItemStack is) {
        if (isInnerRange(innerX, innerY, innerZ)) {
            slots[innerX][innerY][innerZ] = is;
            this.markDirty();
        }
    }

    public void removeInnerSlot(byte innerX, byte innerY, byte innerZ) {
        if (isInnerRange(innerX, innerY, innerZ)) {
            slots[innerX][innerY][innerZ] = null;
            this.markDirty();
        }
    }

    private byte[] getInnerPos(float hitX, float hitY, float hitZ, int side) {
        byte innerX = this.slotLength;
        byte innerY = this.slotLength;
        byte innerZ = this.slotLength;
        hitX = (int) (hitX * 1000) / 1000F;
        hitY = (int) (hitY * 1000) / 1000F;
        hitZ = (int) (hitZ * 1000) / 1000F;
        for (byte i = 0; i < posPartition.length; i++) {
            if (hitX == posPartition[i]) {
                hitX += ForgeDirection.VALID_DIRECTIONS[side].offsetX * 0.01F;
            }
            if (hitY == posPartition[i]) {
                hitY += ForgeDirection.VALID_DIRECTIONS[side].offsetY * 0.01F;
            }
            if (hitZ == posPartition[i]) {
                hitZ += ForgeDirection.VALID_DIRECTIONS[side].offsetZ * 0.01F;
            }
            if (innerX == this.slotLength && hitX < posPartition[i]) {
                innerX = i;
            }
            if (innerY == this.slotLength && hitY < posPartition[i]) {
                innerY = i;
            }
            if (innerZ == this.slotLength && hitZ < posPartition[i]) {
                innerZ = i;
            }
        }
        return new byte[] { innerX, innerY, innerZ };
    }

    private boolean isInnerRange(int x, int y, int z) {
        return 0 <= x && 0 <= y && 0 <= z && x < this.slotLength && y < this.slotLength && z < this.slotLength;
    }

    public byte[][][] getVisibleFlg() {
        byte[][][] flgs = new byte[this.slotLength][this.slotLength][this.slotLength];
        boolean[] outerOpaqueCubeFlgs = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
        //外世界
        for (ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS) {
            outerOpaqueCubeFlgs[fd.ordinal()] = this.getWorldObj().getBlock(xCoord + fd.offsetX, yCoord + fd.offsetY, zCoord + fd.offsetZ).isOpaqueCube();
        }
        for (int x = 0; x < this.slotLength; x++) {
            for (int y = 0; y < this.slotLength; y++) {
                for (int z = 0; z < this.slotLength; z++) {
                    for (ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS) {
                        //外の世界のブロック
                        switch (fd) {
                        case DOWN:
                            if (y == 0) {
                                if (outerOpaqueCubeFlgs[fd.ordinal()]) {
                                    continue;
                                }
                            }
                            break;
                        case EAST:
                            if (x == this.slotLength - 1) {
                                if (outerOpaqueCubeFlgs[fd.ordinal()]) {
                                    continue;
                                }
                            }
                            break;
                        case NORTH:
                            if (z == 0) {
                                if (outerOpaqueCubeFlgs[fd.ordinal()]) {
                                    continue;
                                }
                            }
                            break;
                        case SOUTH:
                            if (z == this.slotLength - 1) {
                                if (outerOpaqueCubeFlgs[fd.ordinal()]) {
                                    continue;
                                }
                            }
                            break;
                        case UP:
                            if (y == this.slotLength - 1) {
                                if (outerOpaqueCubeFlgs[fd.ordinal()]) {
                                    continue;
                                }
                            }
                            break;
                        case WEST:
                            if (x == 0) {
                                if (outerOpaqueCubeFlgs[fd.ordinal()]) {
                                    continue;
                                }
                            }
                            break;
                        }
                        //内部ブロック
                        if (getOffsetedInnerBlock(x, y, z, fd).isOpaqueCube()) {
                            continue;
                        } else if (getOffsetedInnerBlock(x, y, z, fd) == getInnerBlock(x, y, z)) {
                            continue;
                        }
                        flgs[x][y][z] |= 1 << fd.ordinal();
                    }
                }
            }
        }
        return flgs;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (!nbt.hasKey("slotsNBT", 9)) {
            nbt.setTag("slotsNBT", new NBTTagList());
        }
        NBTTagList list = nbt.getTagList("slotsNBT", 10);
        NBTTagCompound parser = new NBTTagCompound();
        for (int i = 0; i < this.slotLength; i++) {
            for (int j = 0; j < this.slotLength; j++) {
                for (int k = 0; k < this.slotLength; k++) {
                    NBTTagCompound slotNBT;
                    if (slots[i][j][k] != null) {
                        slotNBT = new NBTTagCompound();
                        NBTTagCompound itemNBT = new NBTTagCompound();
                        slots[i][j][k].writeToNBT(itemNBT);
                        slotNBT.setTag("itemNBT", itemNBT);
                    } else {
                        slotNBT = parser;
                    }
                    list.appendTag(slotNBT);
                }
            }
        }
        nbt.setByte("this.slotLength", this.slotLength);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("this.slotLength")) {
            this.setSlotLength(nbt.getByte("this.slotLength"));
        }
        if (!nbt.hasKey("slotsNBT", 9)) {
            nbt.setTag("slotsNBT", new NBTTagList());
        }
        NBTTagList list = nbt.getTagList("slotsNBT", 10);
        int count = 0;
        for (int i = 0; i < this.slotLength && i < list.tagCount(); i++) {
            for (int j = 0; j < this.slotLength && j < list.tagCount(); j++) {
                for (int k = 0; k < this.slotLength; k++) {
                    NBTTagCompound slotNBT = list.getCompoundTagAt(count++);
                    if (slotNBT.hasKey("itemNBT")) {
                        NBTTagCompound itemNBT = (NBTTagCompound) slotNBT.getTag("itemNBT");
                        slots[i][j][k] = ItemStack.loadItemStackFromNBT(itemNBT);
                    }
                }
            }
        }
    }

    @Override
    public boolean canUpdate() {
        return false;
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
}
