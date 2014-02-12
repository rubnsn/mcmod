package ruby.bamboo.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import ruby.bamboo.BambooInit;

public class WorldGenBamboo extends WorldGenerator {
    @Override
    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
        for (int var6 = 0; var6 < 20; ++var6) {
            int var7 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
            int var8 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
            int var9 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
            Block blockid = par1World.getBlock(var7, var8 - 1, var9);

            if (par1World.isAirBlock(var7, var8, var9)) {
                if (blockid == Blocks.grass || blockid == Blocks.dirt) {
                    par1World.setBlock(var7, var8, var9, BambooInit.bamboo, 15, 0);
                }
            }
        }

        return true;
    }
}
