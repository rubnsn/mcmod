package ruby.bamboo.world;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import ruby.bamboo.world.biomegen.BiomeGenBaseBamboo;

public class WorldChunkManagerBamboo extends WorldChunkManager {
    public static ArrayList<BiomeGenBase> allowedBiomes = new ArrayList<BiomeGenBase>(Arrays.asList(BiomeGenBaseBamboo.sakuraForest));
    private GenLayer genBiomes;
    private GenLayer biomeIndexLayer;

    private BiomeCache biomeCache;

    public WorldChunkManagerBamboo() {
        super();
        this.biomeCache = new BiomeCache(this);
        this.getBiomesToSpawnIn().clear();
        this.getBiomesToSpawnIn().addAll(allowedBiomes);
    }

    public WorldChunkManagerBamboo(World par1World) {
        super(par1World);
    }

}
