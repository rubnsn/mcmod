package ruby.bamboo.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityMultiBlock extends TileEntity implements IBlockAccess {
    private byte slotLength;
    private ItemStack[][][] slots;
    private float[] posPartition;
    private byte[][][] sideRenderCache;
    private static HashMap<Byte, float[]> renderOffsetCache;

    public TileEntityMultiBlock() {
        this.setSlotLength((byte) 1);
    }

    public void setSlotLength(byte len) {
        if (len < 1 || 16 < len) {
            len = 3;
        }
        this.slotLength = len;
        this.setPosPatition();
        slots = new ItemStack[this.slotLength][this.slotLength][this.slotLength];

    }

    private void setPosPatition() {
        float[] tmp = new float[this.slotLength];
        for (int i = 0; i < this.slotLength; i++) {
            tmp[i] = (1 / (float) (this.slotLength)) * (i + 1);
        }
        posPartition = tmp;
    }

    public byte getFieldSize() {
        return this.slotLength;
    }

    public float[] getRenderOffset() {
        if (renderOffsetCache == null) {
            renderOffsetCache = new HashMap<Byte, float[]>();
        }
        if (renderOffsetCache.containsKey(slotLength)) {
            return renderOffsetCache.get(slotLength);
        } else {
            float[] o = new float[this.slotLength];
            float offset = (1 / (float) (this.slotLength));
            for (int i = 0; i < this.slotLength; i++) {
                o[i] = i * offset;
            }
            renderOffsetCache.put(slotLength, o);
            return o;
        }
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

    public boolean setInnerBlock(World world, float hitX, float hitY, float hitZ, int side, ItemStack is) {
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
            res.setItemDamage(Block.getBlockFromItem(res.getItem()).damageDropped(res.getItemDamage()));
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
        ForgeDirection fd = ForgeDirection.VALID_DIRECTIONS[side];
        float offset = 1 / (float) this.getFieldSize() / 2F;
        hitX += fd.offsetX * offset;
        hitY += fd.offsetY * offset;
        hitZ += fd.offsetZ * offset;
        for (byte i = 0; i < posPartition.length; i++) {
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

    @SideOnly(Side.CLIENT)
    public byte[][][] getVisibleFlg() {
        if (sideRenderCache != null) {
            return sideRenderCache;
        }
        return sideRenderCache = getSideRender();
    }

    public byte[][][] getSideRender() {
        byte[][][] flgs = new byte[this.slotLength][this.slotLength][this.slotLength];
        boolean[] outerOpaqueCubeFlgs = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
        //外世界
        for (ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS) {
            outerOpaqueCubeFlgs[fd.ordinal()] = this.getWorldObj().getBlock(xCoord + fd.offsetX, yCoord + fd.offsetY, zCoord + fd.offsetZ).isOpaqueCube();
        }
        for (int x = 0; x < this.slotLength; x++) {
            for (int y = 0; y < this.slotLength; y++) {
                for (int z = 0; z < this.slotLength; z++) {
                    if (getInnerBlock(x, y, z) != Blocks.air) {
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
                            default:
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
        }
        return flgs;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.writeToSlotNBT(nbt);
    }

    public void writeToSlotNBT(NBTTagCompound nbt) {
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
                        slotNBT = slots[i][j][k].writeToNBT(new NBTTagCompound());
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
        this.readFromSlotNBT(nbt);
    }

    public void readFromSlotNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("this.slotLength")) {
            this.setSlotLength(nbt.getByte("this.slotLength"));
        }
        if (!nbt.hasKey("slotsNBT", 9)) {
            nbt.setTag("slotsNBT", new NBTTagList());
        }
        NBTTagList list = nbt.getTagList("slotsNBT", 10);
        int count = 0;
        for (int i = 0; i < this.slotLength; i++) {
            for (int j = 0; j < this.slotLength; j++) {
                for (int k = 0; k < this.slotLength; k++) {
                    NBTTagCompound slotNBT = list.getCompoundTagAt(count++);
                    if (slotNBT.hasKey("id")) {
                        if (slots[i][j][k] != null) {
                            slots[i][j][k].readFromNBT(slotNBT);
                        } else {
                            slots[i][j][k] = ItemStack.loadItemStackFromNBT(slotNBT);
                        }
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
        this.writeToSlotNBT(var1);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 5, var1);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromSlotNBT(pkt.func_148857_g());
        this.sideRenderCache = null;
        this.worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
    }

    public List<ItemStack> getInnnerBlocks() {
        HashMap<Item, HashMap<Integer, Integer>> temp = new HashMap<Item, HashMap<Integer, Integer>>();
        ArrayList<ItemStack> res;
        for (int i = 0; i < this.slotLength; i++) {
            for (int j = 0; j < this.slotLength; j++) {
                for (int k = 0; k < this.slotLength; k++) {
                    if (slots[i][j][k] != null) {
                        if (!temp.containsKey(slots[i][j][k].getItem())) {
                            temp.put(slots[i][j][k].getItem(), new HashMap<Integer, Integer>());
                        }
                        HashMap<Integer, Integer> innerMap = temp.get(slots[i][j][k].getItem());
                        if (innerMap.containsKey(slots[i][j][k].getItemDamage())) {
                            innerMap.put(slots[i][j][k].getItemDamage(), innerMap.get(slots[i][j][k].getItemDamage()) + 1);
                        } else {
                            innerMap.put(slots[i][j][k].getItemDamage(), 1);
                        }
                    }
                }
            }
        }
        res = new ArrayList<ItemStack>(temp.size());
        for (Item item : temp.keySet()) {
            for (int dmg : temp.get(item).keySet()) {
                res.add(new ItemStack(item, temp.get(item).get(dmg), Block.getBlockFromItem(item).damageDropped(dmg)));
            }
        }
        return res;
    }

    @Override
    public Block getBlock(int var1, int var2, int var3) {
        return getInnerBlock(var1, var2, var3);
    }

    @Override
    public TileEntity getTileEntity(int var1, int var2, int var3) {
        return this;
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int var1, int var2, int var3, int var4) {
        return this.getWorldObj().getLightBrightnessForSkyBlocks(this.xCoord, this.yCoord, this.zCoord, var4);
    }

    @Override
    public int getBlockMetadata(int var1, int var2, int var3) {
        return this.getInnerMeta(var1, var2, var3);
    }

    @Override
    public boolean isAirBlock(int var1, int var2, int var3) {
        return this.getInnerBlock(var1, var2, var3) == Blocks.air;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int var1, int var2) {
        return this.worldObj.getBiomeGenForCoords(this.xCoord, this.zCoord);
    }

    @Override
    public int getHeight() {
        return this.worldObj.getHeight();
    }

    @Override
    public boolean extendedLevelsInChunkCache() {
        return this.worldObj.extendedLevelsInChunkCache();
    }

    @Override
    public int isBlockProvidingPowerTo(int var1, int var2, int var3, int var4) {
        return this.worldObj.isBlockProvidingPowerTo(var1, var2, var3, var4);
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
        return this.worldObj.isSideSolid(x, y, z, side, _default);
    }

}
