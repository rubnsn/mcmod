package ruby.bamboo.world.biomegen;

import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.common.FMLLog;

public class BiomeGenBaseBamboo extends BiomeGenBase {
    private static final BiomeGenBase[] biomeList = new BiomeGenBase[8];

    public static final BiomeGenBase sakuraForest = new BiomeGenSakuraForest(0, 0).setBiomeName("Sakura Forest");

    public BiomeGenBaseBamboo(int par1) {
        super(par1);
    }

    public BiomeGenBaseBamboo(int par1, boolean register) {
        super(par1, register);
        if (register)
            biomeList[par1] = this;
    }

    public static BiomeGenBase[] getBiomeGenArray() {
        return biomeList;
    }

    public static BiomeGenBase getBiome(int p_150568_0_) {
        if (p_150568_0_ >= 0 && p_150568_0_ <= biomeList.length) {
            return biomeList[p_150568_0_];
        } else {
            FMLLog.warning("Biome ID is out of bounds: " + p_150568_0_ + ", defaulting to 0 (Ocean)");
            return ocean;
        }
    }
}
