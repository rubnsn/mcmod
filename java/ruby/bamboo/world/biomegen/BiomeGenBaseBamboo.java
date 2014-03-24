package ruby.bamboo.world.biomegen;

import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenBaseBamboo extends BiomeGenBase {

    public static final BiomeGenBase sakuraForest = new BiomeGenSakuraForest(getFreeBiome(), 0).setBiomeName("Sakura Forest");
    public static final BiomeGenBase sakuraForest2 = new BiomeGenSakuraForest2(getFreeBiome(), 0).setBiomeName("Sakura Forest2");

    public BiomeGenBaseBamboo(int par1) {
        super(par1);
    }

    public BiomeGenBaseBamboo(int par1, boolean bool) {
        super(par1, bool);
    }

    private static int getFreeBiome() {
        for (int i = 128; i < BiomeGenBase.getBiomeGenArray().length; i++) {
            if (BiomeGenBase.getBiomeGenArray()[i] == null) {
                return i;
            }
        }
        return -1;
    }
}
