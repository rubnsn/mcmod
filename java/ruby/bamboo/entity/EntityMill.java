package ruby.bamboo.entity;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMill extends Entity {
    public static final int MAX_WORKING_TIME = 50;
    float roll;
    private static int DIR = 16;
    private boolean isChangeDir;

    public EntityMill(World par1World) {
        super(par1World);
    }

    public float getRoll() {
        return roll;
    }

    public byte getDir() {
        return dataWatcher != null ? dataWatcher.getWatchableObjectByte(DIR) : 0;
    }

    public void setDir(int dir) {
        dataWatcher.updateObject(DIR, (byte) dir);
    }

    public ItemStack getItem() {
        return null;
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
        if (par1DamageSource.getDamageType() == "player") {
            if (!isDead && !this.worldObj.isRemote) {
                setDead();

                if (getItem() != null) {
                    EntityPlayer entityplayer = null;

                    if (par1DamageSource.getEntity() instanceof EntityPlayer) {
                        entityplayer = (EntityPlayer) par1DamageSource.getEntity();
                    }

                    if (entityplayer != null && entityplayer.capabilities.isCreativeMode) {
                        return true;
                    }

                    this.entityDropItem(getItem(), 1F);
                }
            }
        }

        return false;
    }

    @Override
    public void setPosition(double par1, double par3, double par5) {
        this.posX = par1;
        this.posY = par3;
        this.posZ = par5;
        setBounds(par1, par3, par5);
    }

    public void setBounds(double par1, double par3, double par5) {
        if (getDir() == 0 || getDir() == 2) {
            boundingBox.setBounds(Math.floor(this.posX) - 1.5, Math.floor(this.posY) - 1.5, Math.floor(this.posZ), Math.floor(this.posX) + 2.5, Math.floor(this.posY) + 2, Math.floor(this.posZ) + 0.5);
        } else if (getDir() == 1 || getDir() == 3) {
            boundingBox.setBounds(Math.floor(this.posX), Math.floor(this.posY) - 1.5, Math.floor(this.posZ) - 1.5, Math.floor(this.posX) + 0.5, Math.floor(this.posY) + 1.5, Math.floor(this.posZ) + 2.5);
        }
    }

    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }

    @Override
    public boolean canBeCollidedWith() {
        return !isDead;
    }

    private int targetX;
    private int targetY;
    private int targetZ;
    private short workingTime;

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (++workingTime > MAX_WORKING_TIME) {
            targetX = (int) Math.floor(posX);
            targetY = (int) Math.floor(posY);
            targetZ = (int) Math.floor(posZ);

            switch (getDir()) {
            case 0:
                targetZ = (int) Math.floor(posZ + 1);
                break;

            case 1:
                targetX = (int) Math.floor(posX - 1);
                break;

            case 2:
                targetZ = (int) Math.floor(posZ - 1);
                break;

            case 3:
                targetX = (int) Math.floor(posX + 1);
                break;
            }

            // System.out.println(targetX+" "+targetY+" "+targetZ+" ");
            // TileEntity entity=worldObj.getBlockTileEntity(targetX, targetY,
            // targetZ);
            /*
             * if(entity instanceof TileEntityChest){ int
             * size=((TileEntityChest) entity).getSizeInventory();
             * ArrayList<ItemStack> list=new ArrayList(); for(int
             * i=0;i<size;i++){ ItemStack is=((TileEntityChest)
             * entity).getStackInSlot(i); if(is!=null){ if(is.getItem().itemID
             * == Item.wheat.itemID){ ((TileEntityChest)
             * entity).decrStackSize(i, 1); list.add(new
             * ItemStack(mod_Bamboo.wheatMaterial,1,0)); list.add(new
             * ItemStack(mod_Bamboo.wheatMaterial,1,1)); break; }else
             * if(is.getItem() instanceof ItemBlock){
             * if(((ItemBlock)is.getItem()
             * ).getBlockID()==Block.cobblestone.blockID){ ((TileEntityChest)
             * entity).decrStackSize(i, 1); list.add(new
             * ItemStack(Block.gravel,1,0)); break; }else
             * if(((ItemBlock)is.getItem()).getBlockID()==Block.gravel.blockID){
             * ((TileEntityChest) entity).decrStackSize(i, 1); list.add(new
             * ItemStack(Block.sand,1,0)); break; } } } }
             * tryAddItem((TileEntityChest) entity,list); }
             */
            workingTime = 0;
        }
    }

    // チェストへの格納(空き探索・同一アイテムまとめ・溢れ)
    private void tryAddItem(TileEntityChest entity, ArrayList<ItemStack> items) {
        int invSize = entity.getSizeInventory();

        for (ItemStack is : items) {
            // 一致
            for (int i = 0; i < invSize; i++) {
                if (entity.getStackInSlot(i) != null && entity.getStackInSlot(i).getItem() == is.getItem()) {
                    if (entity.getStackInSlot(i).getItemDamage() == is.getItemDamage()) {
                        if ((entity.getStackInSlot(i).stackSize + is.stackSize) <= entity.getStackInSlot(i).getMaxStackSize()) {
                            entity.getStackInSlot(i).stackSize += is.stackSize;
                            is.stackSize = 0;
                        } else {
                            is.stackSize = is.stackSize - (entity.getStackInSlot(i).getMaxStackSize() - entity.getStackInSlot(i).stackSize);
                            entity.getStackInSlot(i).stackSize = entity.getStackInSlot(i).getMaxStackSize();
                        }
                    }
                }

                if (is.stackSize == 0) {
                    break;
                }
            }

            if (is.stackSize == 0) {
                continue;
            }

            for (int i = 0; i < invSize; i++) {
                if (entity.getStackInSlot(i) == null) {
                    entity.setInventorySlotContents(i, is.copy());
                    is.stackSize = 0;
                }

                if (is.stackSize == 0) {
                    break;
                }
            }

            if (is.stackSize != 0) {
                if (!worldObj.isRemote) {
                    worldObj.spawnEntityInWorld(new EntityItem(worldObj, targetX, targetY + 1, targetZ, is));
                }
            }
        }
    }

    @Override
    protected void entityInit() {
        roll = this.rand.nextInt(360);
        dataWatcher.addObject(DIR, (byte) 0);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {
        dataWatcher.updateObject(DIR, var1.getByte("dir"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {
        var1.setByte("dir", dataWatcher.getWatchableObjectByte(DIR));
    }
}
