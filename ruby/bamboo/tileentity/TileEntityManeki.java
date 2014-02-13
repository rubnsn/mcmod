package ruby.bamboo.tileentity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ruby.bamboo.block.BlockManeki;

public class TileEntityManeki extends TileEntity {
    private boolean isInitialized = false;
    public boolean isDestry = false;
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

        if (!worldObj.isRemote && !isInitialized) {
            if (!((BlockManeki) this.getBlockType()).addManeki(xCoord, zCoord)) {
                isDestry = true;
                worldObj.func_147480_a(xCoord, yCoord, zCoord, true);
            }

            isInitialized = true;
        }
    }

    @Override
    public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z) {
        return oldBlock == newBlock;
    }
}
