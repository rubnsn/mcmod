package ruby.bamboo.tileentity;

import java.util.Random;

import net.minecraft.tileentity.TileEntity;

public class TileEntityManeki extends TileEntity {
    private float roll;
    private boolean handDir;
    private final static Random rand = new Random();

    public TileEntityManeki() {
        roll = rand.nextInt(90);
        handDir = rand.nextBoolean();
    }

    public float getRoll() {
        return roll;
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) {
            if (handDir) {
                if (!((roll += 0.7) < 45)) {
                    handDir = !handDir;
                }
            } else {
                if (!((roll -= 0.7) > 0)) {
                    handDir = !handDir;
                }
            }
        }
    }
}
