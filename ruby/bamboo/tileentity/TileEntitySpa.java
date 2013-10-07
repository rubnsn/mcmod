package ruby.bamboo.tileentity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntitySpa extends TileEntity
{
    private int parentX, parentY, parentZ;
    private EntityLivingBase bathingEntity = null;
    private short bathingTime;
    private boolean stay;
    private int color = 0xffffff;
    public void addColor(int deycolor)
    {
        int i = (deycolor & 0xff0000) >> 16;
        int j = (deycolor & 0x00ff00) >> 8;
        int k = (deycolor & 0x0000ff);
        i &= 0xff;
        j &= 0xff;
        k &= 0xff;

        if (((color & 0xff0000) - (i << 16)) >= 0)
        {
            color -= i << 16;
        }
        else if (i > 0)
        {
            color &= 0x00ffff;
        }

        if (((color & 0x00ff00) - (j << 8)) >= 0)
        {
            color -= j << 8;
        }
        else if (j < 0)
        {
            color &= 0xff00ff;
        }

        if (((color & 0x0000ff) - k) >= 0)
        {
            color -= k;
        }
        else if (j < 0)
        {
            color &= 0xffff00;
        }
    }
    public int getColor()
    {
        return color;
    }
    public boolean isStay()
    {
        return stay;
    }
    public void setStay(boolean flg)
    {
        stay = flg;
    }
    public int getX()
    {
        return parentX;
    }
    public int getY()
    {
        return parentY;
    }
    public int getZ()
    {
        return parentZ;
    }
    @Override
    public void updateEntity()
    {
        if (bathingEntity != null)
        {
            //入浴キャンセル判定
            if ((Math.floor(bathingEntity.posX) - this.xCoord + Math.floor(bathingEntity.posY) - this.yCoord + Math.floor(bathingEntity.posZ) - this.zCoord) != 0)
            {
                bathingEntity = null;
            }
        }
    }
    //ブロックからの温泉の接触呼び出し(1tick毎？)
    public void onCollisionEntity(EntityLivingBase entity)
    {
        if (bathingEntity == null)
        {
            bathingEntity = entity;
            bathingTime = 0;
        }
        else
        {
            //20tick1秒で10秒計算
            if (++bathingTime > 400)
            {
                int effectNum = getEffectNum(worldObj, getX(), getY(), getZ());

                if (effectNum == 0)
                {
                    entity.heal(1);
                    bathingTime = 380;
                }
                else if (effectNum == 1)
                {
                    onFoodEaten(new ItemStack(373, 1, 8289), entity.worldObj, entity);
                    bathingTime = 0;
                }
                else if (effectNum == 2)
                {
                    onFoodEaten(new ItemStack(373, 1, 8297), entity.worldObj, entity);
                    bathingTime = 0;
                }
                else if (effectNum == 4)
                {
                    onFoodEaten(new ItemStack(373, 1, 8195), entity.worldObj, entity);
                    bathingTime = 0;
                }
                else if (effectNum == 8)
                {
                    onFoodEaten(new ItemStack(373, 1, 8290), entity.worldObj, entity);
                    bathingTime = 0;
                }
                else if (effectNum == 15)
                {
                    onFoodEaten(new ItemStack(373, 1, 8292), entity.worldObj, entity);
                }
            }
        }
    }
    private void onFoodEaten(ItemStack par1ItemStack, World par2World, EntityLivingBase par3EntityPlayer)
    {
        List var4 = Item.potion.getEffects(par1ItemStack);

        if (var4 != null)
        {
            Iterator var5 = var4.iterator();

            while (var5.hasNext())
            {
                PotionEffect var6 = (PotionEffect)var5.next();
                par3EntityPlayer.addPotionEffect(new PotionEffect(var6));
            }
        }
    }
    private int getEffectNum(World world, int i, int j, int k)
    {
        TileEntitySpa tes = ((TileEntitySpa)world.getBlockTileEntity(getX(), getY(), getZ()));
        int result = 0;

        if (tes != null)
        {
            int wc = tes.getColor();
            int r = (wc & 0xff0000) >> 16;
            int g = (wc & 0x00ff00) >> 8;
            int b = (wc & 0x0000ff);

            if (r < 0xaf && g < 0xaf && b < 0xaf)
            {
                result = 1;
            }

            if (g < 0x7f && b < 0x7f)
            {
                result += 2;
            }

            if (r < 0x7f && b < 0x7f)
            {
                result += 4;
            }

            if (r < 0x7f && g < 0x7f)
            {
                result += 8;
            }
        }

        return result;
    }
    public void setLocation(int i, int j, int k)
    {
        this.parentX = i;
        this.parentY = j;
        this.parentZ = k;
    }
    public int getOffset(int i, int j, int k)
    {
        return Math.abs((parentX - i)) + Math.abs((parentY - j)) + Math.abs((parentZ - k));
    }/*
    @Override
    public void onInventoryChanged()
    {
        if (this.worldObj != null)
        {
            this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
            this.worldObj.updateTileEntityChunkAndDoNothing(this.xCoord, this.yCoord, this.zCoord, this);
        }
    }*/
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        writeToParentNBT(var1);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, var1);
    }
    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        this.readFromNBT(pkt.data);
    }
    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        parentX = nbttagcompound.getInteger("xx");
        parentY = nbttagcompound.getInteger("yy");
        parentZ = nbttagcompound.getInteger("zz");
        stay = nbttagcompound.getBoolean("stay");
        color = nbttagcompound.getInteger("color");
    }
    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        writeToParentNBT(nbttagcompound);
        nbttagcompound.setBoolean("stay", stay);
    }
    //クライアント送信用NBT
    private void writeToParentNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setInteger("xx", parentX);
        nbttagcompound.setInteger("yy", parentY);
        nbttagcompound.setInteger("zz", parentZ);
        nbttagcompound.setInteger("color", color);
    }
}
