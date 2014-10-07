package ruby.bamboo.tileentity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityVillagerBlock extends TileEntity implements IMerchant,
        ISidedInventory {

    private ItemStack[] innerInv = new ItemStack[2];
    private MerchantRecipeList recipeList;
    private int merchantType;
    private EntityPlayer customer;
    private byte exp;
    private int coolTime;
    private float offsetRoteY = 0;
    public static final int MAX_COOL_TIME = 60;
    private int roteSpeed = MAX_COOL_TIME;

    public int getMerchantType() {
        return this.merchantType;
    }

    public void setMerchantType(int type) {
        this.merchantType = type;
    }

    public void addExp(int exp) {
        this.exp += exp;
        if (Integer.bitCount(this.exp) == 1) {
            this.addVanillaVillagerTrade();
            this.addEmotion(1);
            this.coolTime = 20;
            if (128 <= this.exp) {
                this.exp = 64;
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.writeToVillagerNBT(nbt);
    }

    public void writeToVillagerNBT(NBTTagCompound nbt) {
        nbt.setByte("exp", exp);
        nbt.setInteger("merchantType", this.merchantType);
        if (this.recipeList == null) {
            this.setDefaultList();
        }
        nbt.setTag("list", this.recipeList.getRecipiesAsTags());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.readFromVillagerNBT(nbt);
    }

    public void readFromVillagerNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("exp")) {
            this.exp = nbt.getByte("exp");
        }
        if (nbt.hasKey("merchantType")) {
            this.merchantType = nbt.getInteger("merchantType");
        }
        if (nbt.hasKey("list")) {
            this.recipeList = new MerchantRecipeList(nbt.getCompoundTag("list"));
        }
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
    public void setCustomer(EntityPlayer player) {
        this.customer = player;
    }

    @Override
    public EntityPlayer getCustomer() {
        return this.customer;
    }

    @Override
    public MerchantRecipeList getRecipes(EntityPlayer player) {
        if (this.recipeList == null) {
            this.setDefaultList();
        }
        return this.recipeList;
    }

    private void setDefaultList() {
        this.addVanillaVillagerTrade();
    }

    public MerchantRecipe createMerchantRecipeInstance(Item item, Random rand) {
        return new MerchantRecipe(createItemStack(item, rand), Items.emerald);
    }

    private ItemStack createItemStack(Item item, Random rand) {
        return new ItemStack(item, getRandomStackSize(item, rand), 0);
    }

    private int getRandomStackSize(Item item, Random rand) {
        Tuple tuple = (Tuple) EntityVillager.villagersSellingList.get(item);
        return tuple == null ? 1 : (((Integer) tuple.getFirst()).intValue() >= ((Integer) tuple.getSecond()).intValue() ? ((Integer) tuple.getFirst()).intValue() : ((Integer) tuple.getFirst()).intValue() + rand.nextInt(((Integer) tuple.getSecond()).intValue() - ((Integer) tuple.getFirst()).intValue()));
    }

    @Override
    public void setRecipes(MerchantRecipeList list) {
        this.recipeList = list;
    }

    @Override
    public void useRecipe(MerchantRecipe list) {
        list.incrementToolUses();
        this.getWorldObj().playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "mob.villager.yes", 1F, (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
    }

    @Override
    public void func_110297_a_(ItemStack itemStack) {
    }

    public void addVanillaVillagerTrade() {
        EntityVillager dummyVillger = new EntityVillager(worldObj);
        dummyVillger.setProfession(this.merchantType);
        if (this.recipeList == null) {
            this.recipeList = dummyVillger.getRecipes(this.customer);
        } else {
            this.recipeList.addToListWithCheck((MerchantRecipe) dummyVillger.getRecipes(this.customer).get(0));
        }
        dummyVillger.setDead();
        this.markDirty();
    }

    @Override
    public void updateEntity() {
        if (!this.getWorldObj().isRemote) {
            if (coolTime < 1) {
                if (innerInv[0] != null && innerInv[1] == null && recipeList != null) {
                    MerchantRecipe recipe = recipeList.canRecipeBeUsed(innerInv[0], null, 0);
                    if (recipe != null) {
                        if (innerInv[1] == null) {
                            innerInv[0].stackSize -= recipe.getItemToBuy().stackSize;
                            innerInv[1] = recipe.getItemToSell().copy();
                            this.addEmotion(3);
                            coolTime = MAX_COOL_TIME;
                        }
                    }
                }
            }
        } else {
            if ((this.getBlockMetadata() & 8) != 0) {
                offsetRoteY = offsetRoteY < 359 ? offsetRoteY + (360 / (float) roteSpeed) : 0;
            } else {
                offsetRoteY = 0;
            }
        }
        if (coolTime == 1) {
            this.removeEmotion();
            coolTime--;
        } else if (1 < coolTime) {
            coolTime--;
        }

    }

    public void addEmotion(int emNum) {
        if (!this.getWorldObj().isRemote) {
            int meta = this.getBlockMetadata();
            meta |= ((emNum & 3) << 2);
            this.getWorldObj().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta, 3);
        }
    }

    public void removeEmotion() {
        if (!this.getWorldObj().isRemote) {
            this.getWorldObj().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, this.getBlockMetadata() & 3, 3);
        }
        roteSpeed = MAX_COOL_TIME;
    }

    public boolean isEmoted() {
        return (this.getBlockMetadata() & 12) != 0;
    }

    @SideOnly(Side.CLIENT)
    public float getOffsetRoteY() {
        return this.offsetRoteY;
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return innerInv[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int dec) {
        if (this.innerInv[slot] != null) {
            ItemStack itemstack;

            if (this.innerInv[slot].stackSize <= dec) {
                itemstack = this.innerInv[slot];
                this.innerInv[slot] = null;
                return itemstack;
            } else {
                itemstack = this.innerInv[slot].splitStack(dec);

                if (this.innerInv[slot].stackSize == 0) {
                    this.innerInv[slot] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.innerInv[slot] != null) {
            ItemStack itemstack = this.innerInv[slot];
            this.innerInv[slot] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack is) {
        innerInv[slot] = is;
    }

    @Override
    public String getInventoryName() {
        return "VillagerBlockInnner";
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
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return false;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int side, ItemStack is) {
        return side == 0;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[] { side == 1 ? 0 : side == 0 ? 1 : 0 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack is, int num) {
        return innerInv[0] == null || (is.isItemEqual(innerInv[0]) && innerInv[0].stackSize + is.stackSize <= innerInv[0].getMaxStackSize());
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return innerInv[1] != null;
    }

    public void decrRollSpeed() {
        if (5 < roteSpeed) {
            this.roteSpeed -= 2;
            if (roteSpeed <= 5) {
                roteSpeed = 5;
            }
        }
        this.coolTime = 60;
    }

    public ArrayList<ItemStack> getInnerItems() {
        ArrayList<ItemStack> res = new ArrayList<ItemStack>();
        for (int i = 0; i < innerInv.length; i++) {
            res.add(innerInv[i]);
            innerInv[i] = null;
        }
        this.addEmotion(2);
        this.coolTime = 60;
        return res;
    }
}
