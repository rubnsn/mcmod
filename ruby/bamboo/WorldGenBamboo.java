package ruby.bamboo;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBamboo extends WorldGenerator {
    @Override
    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
        for (int var6 = 0; var6 < 20; ++var6) {
            int var7 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
            int var8 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
            int var9 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
            int blockid = par1World.getBlockId(var7, var8 - 1, var9);

            if (par1World.isAirBlock(var7, var8, var9)) {
                if (blockid == Block.grass.blockID || blockid == Block.dirt.blockID) {
                    par1World.setBlock(var7, var8, var9, BambooInit.bambooBID, 15, 0);
                }
            }
        }

        return true;
    }
}
