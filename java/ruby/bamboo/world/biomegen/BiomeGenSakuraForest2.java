package ruby.bamboo.world.biomegen;

import java.util.Random;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import ruby.bamboo.world.WorldGenSakuraForest;

public class BiomeGenSakuraForest2 extends BiomeGenSakuraForest {

    protected static final WorldGenSakuraForest field_150629_aC = new WorldGenSakuraForest(false, true) {
        @Override
        protected int getMeta(Random rand) {
            return rand.nextInt(10) != 0 ? 1 : rand.nextBoolean() ? 11 : 14;
        }
    };
    protected static final WorldGenSakuraForest field_150630_aD = new WorldGenSakuraForest(false, false) {
        @Override
        protected int getMeta(Random rand) {
            return rand.nextInt(10) != 0 ? 1 : rand.nextBoolean() ? 11 : 14;
        }
    };

    public BiomeGenSakuraForest2(int p_i45377_1_, int p_i45377_2_) {
        super(p_i45377_1_, p_i45377_2_);
        setTemperatureRainfall(0.2F, 0.6F);
        if (p_i45377_1_ == 2) {
            this.field_150609_ah = 353825;
            setTemperatureRainfall(0.1F, 0.4F);
        }
    }

    @Override
    public WorldGenAbstractTree getTree() {
        return this.field_150630_aD;
    }

    @Override
    public WorldGenAbstractTree getBigTree() {
        return this.field_150629_aC;
    }

    @Override
    public int getModdedBiomeGrassColor(int original) {
        return 0xcccc33;
    }
}
