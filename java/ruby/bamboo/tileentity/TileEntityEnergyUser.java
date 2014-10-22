package ruby.bamboo.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;

@InterfaceList({ @Interface(iface = "cofh.api.energy.IEnergyHandler",
        modid = "CoFHAPI"), @Interface(
        iface = "shift.sextiarysector.api.machine.energy.IEnergyHandler",
        modid = "SextiarySector") })
public abstract class TileEntityEnergyUser extends TileEntity implements
        IEnergyHandler, shift.sextiarysector.api.machine.energy.IEnergyHandler {
    //RF基準
    int innerEnerygy = 0;

    /**
     * @param energy RF
     */
    public void setInnerEnergy(int energy) {
        this.innerEnerygy = energy;
    }

    abstract int getMinUseEnergy();

    int getMaxUseEnergy() {
        return getMinUseEnergy();
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("innerEnergy", this.innerEnerygy);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("innerEnergy")) {
            this.innerEnerygy = nbt.getInteger("innerEnergy");
        }
    }

    //CoFHAPI
    @Method(modid = "CoFHAPI")
    @Override
    public boolean canConnectEnergy(ForgeDirection arg0) {
        return true;
    }

    @Method(modid = "CoFHAPI")
    @Override
    public int extractEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
        return 0;
    }

    @Method(modid = "CoFHAPI")
    @Override
    public int getEnergyStored(ForgeDirection arg0) {
        return 0;
    }

    @Method(modid = "CoFHAPI")
    @Override
    public int getMaxEnergyStored(ForgeDirection arg0) {
        return 0;
    }

    @Method(modid = "CoFHAPI")
    @Override
    public int receiveEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
        int recive = 0;
        if (this.innerEnerygy < this.getMaxUseEnergy()) {
            if ((this.getMaxUseEnergy() - this.innerEnerygy) < arg1) {
                recive = this.getMaxUseEnergy() - this.innerEnerygy;
                if (!arg2) {
                    innerEnerygy = this.getMaxUseEnergy();
                }
            } else {
                recive = arg1;
                if (!arg2) {
                    innerEnerygy += arg1;
                }
            }
        }
        return recive;
    }

    @Method(modid = "SextiarySector")
    @Override
    public int addEnergy(ForgeDirection from, int power, int speed, boolean simulate) {
        return 0;
    }

    //1p4s=2p1s 2p4s=3p1s
    @Method(modid = "SextiarySector")
    @Override
    public int drawEnergy(ForgeDirection from, int power, int speed, boolean simulate) {
        int recive = 0;
        int rf = (int) (Math.pow(4, (power - 1)) * speed) / 16;
        if (this.innerEnerygy < this.getMaxUseEnergy()) {
            if ((this.getMaxUseEnergy() - this.innerEnerygy) < rf) {
                recive = this.getMaxUseEnergy() - this.innerEnerygy;
                if (!simulate) {
                    innerEnerygy = this.getMaxUseEnergy();
                }
                return (int) (Math.pow(4, (power - 1)) / (recive * 16));
            } else {
                if (!simulate) {
                    innerEnerygy += rf;
                }
                return speed;
            }
        }
        return recive;
    }

    @Method(modid = "SextiarySector")
    @Override
    public boolean canInterface(ForgeDirection from) {
        return true;
    }

    @Method(modid = "SextiarySector")
    @Override
    public int getPowerStored(ForgeDirection from) {
        return 0;
    }

    @Method(modid = "SextiarySector")
    @Override
    public long getSpeedStored(ForgeDirection from) {
        return 0;
    }

    @Method(modid = "SextiarySector")
    @Override
    public int getMaxPowerStored(ForgeDirection from) {
        return 0;
    }

    @Method(modid = "SextiarySector")
    @Override
    public long getMaxSpeedStored(ForgeDirection from) {
        return 0;
    }

}
