package ruby.bamboo.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import ruby.bamboo.BambooInit;
import ruby.bamboo.block.BlockBambooShoot;

public class WorldGenBamboo extends WorldGenerator {
    @Override
    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
        int posX, posY, posZ;
        for (int var6 = 0; var6 < 20; ++var6) {
            posX = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
            posY = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
            posZ = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
            Block blockid = par1World.getBlock(posX, posY - 1, posZ);

            if (par1World.isAirBlock(posX, posY, posZ) && !BlockBambooShoot.canChildGrow(par1World, posX, posY, posZ)) {
                if (blockid == Blocks.grass || blockid == Blocks.dirt) {
                    par1World.setBlock(posX, posY, posZ, BambooInit.bambooShoot, 0, 0);
                }
            }
        }

        return true;
    }
}
