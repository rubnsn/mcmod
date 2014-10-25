package ruby.bamboo.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import ruby.bamboo.BambooInit;
import ruby.bamboo.block.BlockSakura;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class GeneraterHandler implements IWorldGenerator {
    private static GeneraterHandler instance = new GeneraterHandler();

    public static void init() {
        GameRegistry.registerWorldGenerator(instance, 1);
    }

    private final static byte BAMBOO = 0;
    private final static byte SAKURA = 1;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        WorldChunkManager worldchunkmanager = world.getWorldChunkManager();

        if (worldchunkmanager != null) {
            BiomeGenBase biome = worldchunkmanager.getBiomeGenAt(chunkX * 16, chunkZ * 16);
            if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.MOUNTAIN) || BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.FOREST)) {
                if (!BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.FROZEN)) {
                    switch (random.nextInt(4)) {
                    case BAMBOO:
                        generateBambooshoot(random, world, chunkX, chunkZ, 60);
                        break;

                    case SAKURA:
                        generateSakura(random, world, chunkX, chunkZ, 60);
                        break;
                    }
                }
            } else if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.BEACH) || BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.WATER) || BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SWAMP)) {
                switch (random.nextInt(4)) {
                case 0:
                    generateSeaweed(random, world, chunkX, chunkZ, 80);
                }
            }
        }
    }

    private void generateSeaweed(Random random, World world, int chunkX, int chunkZ, int maxHeight) {
        for (int l = random.nextInt(20); 0 < l; l--) {
            int x = chunkX * 16 + random.nextInt(16);
            int y = random.nextInt(maxHeight);
            int z = chunkZ * 16 + random.nextInt(16);
            int posX, posY, posZ;
            for (int var6 = 0; var6 < 20; ++var6) {
                posX = x + random.nextInt(8) - random.nextInt(8);
                posY = y + random.nextInt(4) - random.nextInt(4);
                posZ = z + random.nextInt(8) - random.nextInt(8);
                Block blockid = world.getBlock(posX, posY - 1, posZ);

                if (world.isAirBlock(posX, posY, posZ) && BambooInit.seaWeedPlant.canBlockStay(world, posX, posY, posZ)) {
                    world.setBlock(posX, posY, posZ, BambooInit.seaWeedPlant, 0, 0);
                }
            }
        }
    }

    private void generateBambooshoot(Random random, World world, int chunkX, int chunkZ, int maxHeight) {
        for (int l = random.nextInt(10); 0 < l; l--) {
            int x = chunkX * 16 + random.nextInt(16);
            int y = random.nextInt(maxHeight) + 60;
            int z = chunkZ * 16 + random.nextInt(16);
            WorldGenBamboo.instance.generate(world, random, x, y, z);
        }
    }

    private void generateSakura(Random random, World world, int chunkX, int chunkZ, int maxHeight) {
        for (int l = random.nextInt(5); 0 < l; l--) {
            int x = chunkX * 16 + random.nextInt(16);
            int y = random.nextInt(maxHeight) + 60;
            int z = chunkZ * 16 + random.nextInt(16);

            for (int var6 = 0; var6 < 10; ++var6) {
                int var7 = x + random.nextInt(8) - random.nextInt(8);
                int var8 = y + random.nextInt(4) - random.nextInt(4);
                int var9 = z + random.nextInt(8) - random.nextInt(8);

                if (world.isAirBlock(var7, var8, var9) && world.getBlock(var7, var8 - 1, var9) == Blocks.grass && Blocks.pumpkin.canPlaceBlockAt(world, var7, var8, var9)) {
                    ((BlockSakura) BambooInit.sakura).growTree(world, var7, var8, var9, random, 0x0f);
                    return;
                }
            }
        }
    }
}
