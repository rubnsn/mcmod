package ruby.bamboo.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import ruby.bamboo.block.BlockMillStone;
import ruby.bamboo.grinder.GrindRecipe;
import ruby.bamboo.grinder.GrindRegistory;

public class TileEntityMillStone extends TileEntity implements ISidedInventory {
    private static final int[] slots_top = new int[] { 0 };
    private static final int[] slots_bottom = new int[] { 2, 1 };
    private static final int[] slots_sides = new int[] { 0 };
    private String nowGrindItemName = Block.blockRegistry.getNameForObject(Blocks.air);
    private int nowGrindItemDmg;
    private float roll;
    private ItemStack[] slot = new ItemStack[3];
    private final static int MAX_PROGRESS = 3;
    public int grindTime;
    public int progress;
    public int grindMotion;
    public int isGrind;
    private static final int MAX_GRINDTIME = 360;

    public int getRoll() {
        return (int) roll;
    }

    public int isGrind() {
        return grindTime > 0 ? 1 : 0;
    }

    public int getProgress() {
        return getRatio(grindTime, MAX_GRINDTIME, MAX_PROGRESS);
    }

    @Override
    public void updateEntity() {
        boolean flag1 = false;

        if (!worldObj.isRemote) {
            if (grindTime == 0 && canGrind()) {
                decrementSlot0();
                ++grindTime;
            } else if (grindTime > 0) {
                flag1 = true;
                ++grindTime;

                if (grindTime > MAX_GRINDTIME) {
                    grindTime = 0;
                    this.grindItem();
                }
            }

            if (grindTime > 0) {
                if (grindTime % 10 == 0) {
                    grindMotion = grindMotion++ < 3 ? grindMotion : 0;
                }
            } else {
                roll = 0;
            }

            if (flag1 || grindTime == 0) {
                BlockMillStone.updateBlockState(grindTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        } else {
            if (getBlockMetadata() == 1) {
                roll = ++roll < 360 ? roll : 0;
                Object nowgi = Item.itemRegistry.getObject(nowGrindItemName);

                if (nowgi != null) {
                    if (nowgi instanceof ItemBlock) {
                        Block block = (Block) Block.blockRegistry.getObject(nowGrindItemName);
                        float xRand = 0;
                        float zRand = 0;
                        if (((int) roll & 1) == 1) {
                            xRand = this.worldObj.rand.nextFloat();
                            zRand = this.worldObj.rand.nextInt(2);
                        } else {
                            xRand = this.worldObj.rand.nextInt(2);
                            zRand = this.worldObj.rand.nextFloat();
                        }
                        Minecraft.getMinecraft().effectRenderer.addEffect((new EntityDiggingFX(this.worldObj, xCoord + xRand, yCoord + 0.5F, zCoord + zRand, 0.0D, 0.0D, 0.0D, block, nowGrindItemDmg)).applyRenderColor(block.getRenderColor(nowGrindItemDmg)).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
                    } else {
                        itemCrackParticle("iconcrack_" + Item.itemRegistry.getIDForObject(nowgi) + "_" + nowGrindItemDmg);
                    }
                }
            } else {
                roll = 0;
            }
        }

        if (flag1) {
            this.markDirty();
        }
    }

    private void itemCrackParticle(String particleName) {
        if (particleName != null) {
            float pitch = 0;
            float yaw = this.worldObj.rand.nextFloat() * 2 - 1;
            for (int j = 0; j < 1; ++j) {
                Vec3 vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool((this.worldObj.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
                vec3.rotateAroundX(-pitch * (float) Math.PI / 180.0F);
                vec3.rotateAroundY(-yaw * (float) Math.PI);
                Vec3 vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool((this.worldObj.rand.nextFloat() - 0.5D) * 0.3D, (-this.worldObj.rand.nextFloat()) * 0.6D - 0.3D, 0.6D);
                vec31.rotateAroundX(-pitch * (float) Math.PI / 180.0F);
                vec31.rotateAroundY(-yaw * (float) Math.PI);
                vec31 = vec31.addVector(this.xCoord + 0.5F, this.yCoord + 1F, this.zCoord + 0.5F);
                this.worldObj.spawnParticle(particleName, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord - 0.15D, vec3.zCoord);

            }
        }
    }

    private int getRatio(float par0, float par1, int par3) {
        return Math.round(par0 / par1 * par3);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
        this.slot = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.slot.length) {
                this.slot[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        grindTime = par1NBTTagCompound.getInteger("grindTime");
        nowGrindItemDmg = par1NBTTagCompound.getInteger("grindItemDmg");
        if (par1NBTTagCompound.hasKey("grindItemName")) {
            nowGrindItemName = par1NBTTagCompound.getString("grindItemName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("grindTime", grindTime);
        par1NBTTagCompound.setString("grindItemName", nowGrindItemName == null ? Block.blockRegistry.getNameForObject(Blocks.air) : nowGrindItemName);
        par1NBTTagCompound.setInteger("grindItemDmg", nowGrindItemDmg);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.slot.length; ++i) {
            if (this.slot[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.slot[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, var1);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Slot数
     * 
     * @return
     */
    @Override
    public int getSizeInventory() {
        return this.slot.length;
    }

    /**
     * 指定したスロットから指定した数のアイテムを取得する(不足分は全て)
     * 
     * @param スロットID
     * @param 取得数
     * @return ItemStack
     */
    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        if (this.slot[par1] != null) {
            ItemStack itemstack;

            if (this.slot[par1].stackSize <= par2) {
                itemstack = this.slot[par1];
                this.slot[par1] = null;
                return itemstack;
            } else {
                itemstack = this.slot[par1].splitStack(par2);

                if (this.slot[par1].stackSize == 0) {
                    this.slot[par1] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    /**
     * 指定したスロットを空にし、ItemStackを取得する
     * 
     * @param スロットID
     * @return スロットに入っていたItemStack
     */
    @Override
    public ItemStack getStackInSlotOnClosing(int par1) {
        if (this.slot[par1] != null) {
            ItemStack itemstack = this.slot[par1];
            this.slot[par1] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    /**
     * 指定したスロットに引数のItemStackを投入する、但し上限を超えたstackはトリミングされる
     * 
     * @param スロットID
     * @param 格納物
     */
    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        this.slot[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * grind実行可能か(出力欄に格納可能か)
     * 
     * @return
     */
    private boolean canGrind() {
        if (this.slot[0] == null) {
            return false;
        } else {
            GrindRecipe gr = GrindRegistory.getOutput(slot[0]);

            if (gr == null) {
                return false;
            }

            ItemStack output = gr.getOutput();
            ItemStack bonus = gr.getBonus();

            if (output == null) {
                return false;
            }

            // 出力先両方共存在しない
            if (this.slot[1] == null && this.slot[2] == null) {
                return true;
            }
            //slot2にボーナスが存在するが、ボーナスは存在しない
            if (this.slot[2] != null && bonus == null) {
                return false;
            }

            // 出力先いずれかが同一のものではない
            if ((this.slot[1] != null && !this.slot[1].isItemEqual(output)) || (this.slot[2] != null && !this.slot[2].isItemEqual(bonus))) {
                return false;
            }

            boolean isInRangeStackSize;
            int outResult = slot[1] != null ? slot[1].stackSize + output.stackSize : output.stackSize;
            isInRangeStackSize = outResult <= getInventoryStackLimit() && outResult <= output.getMaxStackSize();

            if (isInRangeStackSize && bonus != null) {
                int bonusResult = slot[2] != null ? slot[2].stackSize + bonus.stackSize : bonus.stackSize;
                isInRangeStackSize = bonusResult <= getInventoryStackLimit() && bonusResult <= bonus.getMaxStackSize();
            }

            return isInRangeStackSize;
        }
    }

    private void decrementSlot0() {
        nowGrindItemName = Item.itemRegistry.getNameForObject(slot[0].getItem());
        if (nowGrindItemName == null) {
            nowGrindItemName = Block.blockRegistry.getNameForObject(Blocks.air);
        }
        nowGrindItemDmg = slot[0].getItemDamage();
        this.slot[0].stackSize -= GrindRegistory.getOutput(slot[0]).getInput().stackSize;

        if (this.slot[0].stackSize <= 0) {
            this.slot[0] = null;
        }
    }

    /**
     * 完了後、slotへ格納する
     */
    private void grindItem() {
        if (Item.itemRegistry.getObject(nowGrindItemName) != Item.getItemFromBlock(Blocks.air)) {
            GrindRecipe gr = GrindRegistory.getOutput(new ItemStack((Item) Item.itemRegistry.getObject(nowGrindItemName), 64, nowGrindItemDmg));
            ItemStack output = gr.getOutput();
            ItemStack bonus = worldObj.rand.nextInt(gr.getBonusWeight() + 1) == 0 ? gr.getBonus() : null;

            if (this.slot[1] == null) {
                this.slot[1] = output.copy();
            } else if (this.slot[1].isItemEqual(output)) {
                slot[1].stackSize += output.stackSize;
            }

            if (bonus != null) {
                if (this.slot[2] == null) {
                    this.slot[2] = bonus.copy();
                } else if (this.slot[2].isItemEqual(bonus)) {
                    slot[2].stackSize += bonus.stackSize;
                }
            }

            nowGrindItemName = Block.blockRegistry.getNameForObject(Blocks.air);
        }
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.slot[i];
    }

    @Override
    public String getInventoryName() {
        return "MillStone";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return i == 1 || i == 2 ? false : true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return var1 == 0 ? slots_bottom : (var1 == 1 ? slots_top : slots_sides);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return this.isItemValidForSlot(i, itemstack);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j != 0 || i != 0;
    }
}
