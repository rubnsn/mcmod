package ruby.bamboo.entity;

import java.util.Iterator;
import java.util.List;

import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import ruby.bamboo.BambooUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntitySlideDoor extends Entity
{
    private byte closeTimer;

    private static final byte DIRECTION = 17;
    private static final byte ISMIRROR = 18;
    private static final byte ISMOVE = 19;
    private static final byte DOORID = 20;
    private static final byte ISSTOP = 22;
    private static final byte MOVEDIR = 23;
    private static EnumSlideDoor[] tex = EnumSlideDoor.values();
    public EntitySlideDoor(World world)
    {
        super(world);
        yOffset = 0F;
        setSize(1F, 2F);
    }
    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return isBlend() ? pass == 1 : pass == 0;
    }

    @Override
    public boolean func_130002_c(EntityPlayer par1EntityPlayer)
    {
        if (par1EntityPlayer.isSneaking())
        {
            setDataisStop(!getDataisStop());
            par1EntityPlayer.swingItem();
            return true;
        }

        if (getDataisStop())
        {
            if (!getDataMoveflg())
            {
                doorOpen(par1EntityPlayer);
            }
            else
            {
                doorClose();
            }

            par1EntityPlayer.swingItem();
            return true;
        }

        return false;
    }
    @Override
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }
    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }
    //1.3.2用少し時間が経つとYが1.5ほど上がるバグの対策
    //ついでにブロックやEntityの衝突時yが変わるのを見た目だけ対策
    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }
    @Override
    protected boolean pushOutOfBlocks(double par1, double par3, double par5)
    {
        return false;
    }
    @Override
    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return null;
    }

    public String getTex()
    {
        return getEnumSlideDoor(getDataDoorId()).getTex();
    }
    public void setMirror(boolean b)
    {
        setDataMirror(b);
    }
    public boolean isMirror()
    {
        return getDataMirror();
    }

    public byte getDir()
    {
        return getDataDir();
    }
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        setDataDir(nbttagcompound.getByte("dir"));
        setDataMirror(nbttagcompound.getBoolean("mirror"));
        setDataMoveflg(nbttagcompound.getBoolean("moveflg"));
        setDataisStop(nbttagcompound.getBoolean("isStop"));

        if (getDataDir() == 0 || getDataDir() == 3)
        {
            setDataMovedir((byte) - 1);
        }
        else
        {
            setDataMovedir((byte)1);
        }

        if (nbttagcompound.hasKey("doorId"))
        {
            setDataDoorId(nbttagcompound.getShort("doorId"));
        }
        else
        {
            if (nbttagcompound.getString("toptex").indexOf("husuma") != -1)
            {
                setDataDoorId(EnumSlideDoor.HUSUMA.getId());
            }
            else
            {
                setDataDoorId(EnumSlideDoor.SHOZI.getId());
            }
        }
    }
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setByte("dir", getDataDir());
        nbttagcompound.setBoolean("mirror", getDataMirror());
        nbttagcompound.setBoolean("moveflg", getDataMoveflg());
        nbttagcompound.setBoolean("isStop", getDataisStop());
        nbttagcompound.setShort("doorId", getDataDoorId());
    }
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (!worldObj.isRemote && !getDataisStop() && !isDead)
        {
            EntityPlayer entityplayer = null;

            if (par1DamageSource.damageType == "player")
            {
                entityplayer = (EntityPlayer)par1DamageSource.getEntity();
                setDead();
            }
            else
            {
                return false;
            }

            if (entityplayer != null && entityplayer.capabilities.isCreativeMode)
            {
                return true;
            }

            dropItem(getDataDoorId());
        }
        if (getDataisStop() && par1DamageSource.getEntity() != null)
        {
        	double offsetPosX=posX;
        	double offsetPosZ=posZ;
            if (BambooUtil.getPlayerDir(par1DamageSource.getEntity()) == 0)
            {
            	offsetPosZ += 0.05;
                //boundingBox.offset(0, 0, 0.1);
            }

            if (BambooUtil.getPlayerDir(par1DamageSource.getEntity()) == 2)
            {
            	offsetPosZ -= 0.05;
               //boundingBox.offset(0, 0, -0.1);
            }

            if (BambooUtil.getPlayerDir(par1DamageSource.getEntity()) == 1)
            {
            	offsetPosX -= 0.05;
                //boundingBox.offset(-0.1, 0, 0);
            }

            if (BambooUtil.getPlayerDir(par1DamageSource.getEntity()) == 3)
            {
            	offsetPosX += 0.05;
                //boundingBox.offset(0.1, 0, 0);
            }
            setPosition(offsetPosX, posY, offsetPosZ);
        }
        return true;
    }
    private void dropItem(short damage)
    {
        this.entityDropItem(new ItemStack(BambooInit.slideDoorsIID, 1, damage), 1F);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (!getDataisStop() && !getDataMoveflg())
        {
            doorOpen(par1EntityPlayer);
        }

        closeTimer = 10;
    }
    @Override
    public void onUpdate()
    {
        //プレイヤー以外の衝突チェック用
        if (!worldObj.isRemote)
        {
            List collideEntity = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0D, 0.0D, 1.0D));

            if (collideEntity != null)
            {
                Iterator ite = collideEntity.iterator();

                while (ite.hasNext())
                {
                    Entity entity = (Entity)ite.next();

                    if (!entity.isDead && entity instanceof EntityTameable)
                    {
                        if (!getDataisStop() && !getDataMoveflg())
                        {
                            doorOpen(entity);
                        }

                        closeTimer = 30;
                    }
                }
            }
        }

        if (!getDataisStop())
        {
            if (closeTimer < 0 && getDataMoveflg())
            {
                doorClose();
            }
            else
            {
                closeTimer--;
            }
        }
    }
    private void doorOpen(Entity entity)
    {
        if (!getDataMoveflg() && isOpendir(0, 2, entity))
        {
            posX -= getDataMovedir();
            boundingBox.offset(-getDataMovedir(), 0, 0);
            setDataMoveflg(true);
        }

        if (!getDataMoveflg() && isOpendir(1, 3, entity))
        {
            posZ -= getDataMovedir();
            boundingBox.offset(0, 0, -getDataMovedir());
            setDataMoveflg(true);
        }
    }
    private void doorClose()
    {
        if (getDataMoveflg() && (getDataDir() == 0 || getDataDir() == 2))
        {
            posX += getDataMovedir();
            boundingBox.offset(getDataMovedir(), 0, 0);
            setDataMoveflg(false);
        }

        if (getDataMoveflg() && (getDataDir() == 1 || getDataDir() == 3))
        {
            posZ += getDataMovedir();
            boundingBox.offset(0, 0, getDataMovedir());
            setDataMoveflg(false);
        }
    }
    private boolean isOpendir(int i, int i2, Entity entity)
    {
        byte pdir = BambooUtil.getPlayerDir(entity);

        //entityの向きによる開閉チェック
        if (!(getDataDir() == i ? pdir == i || pdir == i2 : getDataDir() == i2 ? pdir == i || pdir == i2 : false))
        {
            return false;
        }

        //entityから引き戸までの距離による開閉チェック
        switch (getDataDir())
        {
                //x軸
            case 1:
            case 3:
                if (Math.abs(this.posZ - entity.posZ) < 0.75)
                {
                    return true;
                }

                break;

                //z軸
            case 0:
            case 2:
                if (Math.abs(this.posX - entity.posX) < 0.75)
                {
                    return true;
                }

                break;
        }

        return false;
    }

    /*
     *  nbttagcompound.setByte("dir", direction);
        nbttagcompound.setBoolean("mirror", mirror);
        nbttagcompound.setBoolean("moveflg", moveflg);
        nbttagcompound.setString("toptex", toptex);
        nbttagcompound.setString("bottomtex", bottomtex);
        nbttagcompound.setInteger("itemid", itemID);
        nbttagcompound.setBoolean("isStop",isStop);(非 Javadoc)
     * @see net.minecraft.src.Entity#entityInit()
     */

    @Override
    protected void entityInit()
    {
        dataWatcher.addObject(DIRECTION, (byte)0);
        dataWatcher.addObject(ISMIRROR, (byte)0);
        dataWatcher.addObject(ISMOVE, (byte)0);
        dataWatcher.addObject(ISSTOP, (byte)0);
        dataWatcher.addObject(MOVEDIR, (byte)0);
        dataWatcher.addObject(DOORID, (short)0);
    }

    //data getter
    private byte getDataDir()
    {
        return dataWatcher.getWatchableObjectByte(DIRECTION);
    }
    private boolean getDataMirror()
    {
        return dataWatcher.getWatchableObjectByte(ISMIRROR) == 1;
    }
    private boolean getDataMoveflg()
    {
        return dataWatcher.getWatchableObjectByte(ISMOVE) == 1;
    }
    private byte getDataMovedir()
    {
        return dataWatcher.getWatchableObjectByte(MOVEDIR);
    }
    private boolean getDataisStop()
    {
        return dataWatcher.getWatchableObjectByte(ISSTOP) == 1;
    }
    private short getDataDoorId()
    {
        return dataWatcher.getWatchableObjectShort(DOORID);
    }
    //data setter
    public EntitySlideDoor setDataDir(byte b)
    {
        dataWatcher.updateObject(DIRECTION, b);
        return this;
    }
    public void setDataMirror(boolean flg)
    {
        dataWatcher.updateObject(ISMIRROR, flg ? (byte)1 : (byte)0);
    }
    public void setDataMoveflg(boolean flg)
    {
        dataWatcher.updateObject(ISMOVE, flg ? (byte)1 : (byte)0);
    }
    public void setDataMovedir(byte dir)
    {
        dataWatcher.updateObject(MOVEDIR, dir);
    }
    public EntitySlideDoor setDataisStop(boolean flg)
    {
        dataWatcher.updateObject(ISSTOP, flg ? (byte)1 : (byte)0);
        return this;
    }
    public EntitySlideDoor setDataDoorId(short id)
    {
        dataWatcher.updateObject(DOORID, id);
        return this;
    }
    @Override
    public boolean isBurning()
    {
        return false;
    }
    public boolean isBlend()
    {
        return getEnumSlideDoor(getDataDoorId()).isBlend();
    }
    private EnumSlideDoor getEnumSlideDoor(int id)
    {
        if (id < tex.length)
        {
            return tex[id];
        }
        else
        {
            setDataDoorId(EnumSlideDoor.HUSUMA.getId());
            return EnumSlideDoor.HUSUMA;
        }
    }
}
