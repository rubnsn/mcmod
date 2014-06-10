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
    private byte MAX_LENGTH = 3;
    private ItemStack[][][] slots = new ItemStack[MAX_LENGTH][MAX_LENGTH][MAX_LENGTH];
    private final float[] posPartition;

    public TileEntityMultiBlock() {
        MAX_LENGTH = 3;
        float[] tmp = new float[MAX_LENGTH];
        for (int i = 0; i < MAX_LENGTH; i++) {
            tmp[i] = ((int) ((1 / (float) (MAX_LENGTH)) * (i + 1) * 1000)) / 1000F;
        }
        posPartition = tmp;
    }

    public byte getFieldSize() {
        return MAX_LENGTH;
    }

    public float[] getRenderOffset() {
        float[] o = new float[MAX_LENGTH];
        int count = 0;
        for (int i = 0; i < MAX_LENGTH; i++) {
            o[i] = i * (1 / (float) (MAX_LENGTH));
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
        for (int x = 0; x < MAX_LENGTH; x++) {
            for (int y = 0; y < MAX_LENGTH; y++) {
                for (int z = 0; z < MAX_LENGTH; z++) {
                    if (slots[x][y][z] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean setInnerBlock(float hitX, float hitY, float hitZ, int side, ItemStack is) {
        byte innerX = MAX_LENGTH;
        byte innerY = MAX_LENGTH;
        byte innerZ = MAX_LENGTH;
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
            if (innerX == MAX_LENGTH && hitX < posPartition[i]) {
                innerX = i;
            }
            if (innerY == MAX_LENGTH && hitY < posPartition[i]) {
                innerY = i;
            }
            if (innerZ == MAX_LENGTH && hitZ < posPartition[i]) {
                innerZ = i;
            }
        }
        if (isInnerRange(innerX, innerY, innerZ)) {
            if (getInnerBlock(innerX, innerY, innerZ) == Blocks.air) {
                slots[innerX][innerY][innerZ] = is;
                this.markDirty();
                return true;
            }
        }
        return false;
    }

    private boolean isInnerRange(int x, int y, int z) {
        return 0 <= x && 0 <= y && 0 <= z && x < MAX_LENGTH && y < MAX_LENGTH && z < MAX_LENGTH;
    }

    public byte[][][] getVisibleFlg() {
        byte[][][] flgs = new byte[MAX_LENGTH][MAX_LENGTH][MAX_LENGTH];
        boolean[] outerOpaqueCubeFlgs = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
        //外世界
        for (ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS) {
            outerOpaqueCubeFlgs[fd.ordinal()] = this.getWorldObj().getBlock(xCoord + fd.offsetX, yCoord + fd.offsetY, zCoord + fd.offsetZ).isOpaqueCube();
        }
        for (int x = 0; x < MAX_LENGTH; x++) {
            for (int y = 0; y < MAX_LENGTH; y++) {
                for (int z = 0; z < MAX_LENGTH; z++) {
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
                            if (x == MAX_LENGTH - 1) {
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
                            if (z == MAX_LENGTH - 1) {
                                if (outerOpaqueCubeFlgs[fd.ordinal()]) {
                                    continue;
                                }
                            }
                            break;
                        case UP:
                            if (y == MAX_LENGTH - 1) {
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
        for (int i = 0; i < MAX_LENGTH; i++) {
            for (int j = 0; j < MAX_LENGTH; j++) {
                for (int k = 0; k < MAX_LENGTH; k++) {
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
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (!nbt.hasKey("slotsNBT", 9)) {
            nbt.setTag("slotsNBT", new NBTTagList());
        }
        NBTTagList list = nbt.getTagList("slotsNBT", 10);
        int count = 0;
        for (int i = 0; i < MAX_LENGTH && i < list.tagCount(); i++) {
            for (int j = 0; j < MAX_LENGTH && j < list.tagCount(); j++) {
                for (int k = 0; k < MAX_LENGTH; k++) {
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
