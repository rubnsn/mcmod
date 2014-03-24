package ruby.bamboo.world.biomegen;

import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenForest;
import net.minecraft.world.biome.BiomeGenMutated;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import ruby.bamboo.world.WorldGenSakuraForest;

public class BiomeGenSakuraForest extends BiomeGenBaseBamboo {

    int forestMode;
    protected static final WorldGenSakuraForest field_150629_aC = new WorldGenSakuraForest(false, true);
    protected static final WorldGenSakuraForest field_150630_aD = new WorldGenSakuraForest(false, false);
    private static final String __OBFID = "CL_00000170";

    public BiomeGenSakuraForest(int p_i45377_1_, int p_i45377_2_) {
        super(p_i45377_1_);

        this.forestMode = p_i45377_2_;
        this.theBiomeDecorator.treesPerChunk = 10;
        this.theBiomeDecorator.grassPerChunk = 2;

        if (this.forestMode == 1) {
            this.theBiomeDecorator.treesPerChunk = 6;
            this.theBiomeDecorator.flowersPerChunk = 100;
            this.theBiomeDecorator.grassPerChunk = 1;
        }
        this.heightVariation = 0.3F;
        this.func_76733_a(5159473);
        this.setTemperatureRainfall(0.7F, 0.8F);

        if (this.forestMode == 2) {
            this.field_150609_ah = 353825;
            this.setTemperatureRainfall(0.6F, 0.6F);
        }

        if (this.forestMode == 0) {
            this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 5, 4, 4));
        }

        if (this.forestMode == 3) {
            this.theBiomeDecorator.treesPerChunk = -999;
        }

        if (this.forestMode == 1) {
            this.flowers.clear();
            for (int x = 0; x < BlockFlower.field_149859_a.length; x++) {
                this.addFlower(Blocks.red_flower, x, 10);
            }
        }
    }

    @Override
    public BiomeGenBase func_150557_a(int p_150557_1_, boolean p_150557_2_) {
        if (this.forestMode == 2) {
            this.field_150609_ah = 353825;
            this.color = p_150557_1_;

            if (p_150557_2_) {
                this.field_150609_ah = (this.field_150609_ah & 16711422) >> 1;
            }

            return this;
        } else {
            return super.func_150557_a(p_150557_1_, p_150557_2_);
        }
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random rand) {
        return (WorldGenAbstractTree) (rand.nextInt(50) != 0 ? getTree() : getBigTree());
    }

    public WorldGenAbstractTree getTree() {
        return field_150630_aD;
    }

    public WorldGenAbstractTree getBigTree() {
        return field_150629_aC;
    }

    @Override
    public String func_150572_a(Random p_150572_1_, int p_150572_2_, int p_150572_3_, int p_150572_4_) {
        if (this.forestMode == 1) {
            double d0 = MathHelper.clamp_double((1.0D + plantNoise.func_151601_a((double) p_150572_2_ / 48.0D, (double) p_150572_4_ / 48.0D)) / 2.0D, 0.0D, 0.9999D);
            int l = (int) (d0 * (double) BlockFlower.field_149859_a.length);

            if (l == 1) {
                l = 0;
            }

            return BlockFlower.field_149859_a[l];
        } else {
            return super.func_150572_a(p_150572_1_, p_150572_2_, p_150572_3_, p_150572_4_);
        }
    }

    @Override
    public void decorate(World par1World, Random par2Random, int par3, int par4) {
        int k;
        int l;
        int i1;
        int j1;
        int k1;

        if (this.forestMode == 3) {
            for (k = 0; k < 4; ++k) {
                for (l = 0; l < 4; ++l) {
                    i1 = par3 + k * 4 + 1 + 8 + par2Random.nextInt(3);
                    j1 = par4 + l * 4 + 1 + 8 + par2Random.nextInt(3);
                    k1 = par1World.getHeightValue(i1, j1);

                    if (par2Random.nextInt(20) == 0) {
                        WorldGenBigMushroom worldgenbigmushroom = new WorldGenBigMushroom();
                        worldgenbigmushroom.generate(par1World, par2Random, i1, k1, j1);
                    } else {
                        WorldGenAbstractTree worldgenabstracttree = this.func_150567_a(par2Random);
                        worldgenabstracttree.setScale(1.0D, 1.0D, 1.0D);

                        if (worldgenabstracttree.generate(par1World, par2Random, i1, k1, j1)) {
                            worldgenabstracttree.func_150524_b(par1World, par2Random, i1, k1, j1);
                        }
                    }
                }
            }
        }

        k = par2Random.nextInt(5) - 3;

        if (this.forestMode == 1) {
            k += 2;
        }

        l = 0;

        while (l < k) {
            i1 = par2Random.nextInt(3);

            if (i1 == 0) {
                genTallFlowers.func_150548_a(1);
            } else if (i1 == 1) {
                genTallFlowers.func_150548_a(4);
            } else if (i1 == 2) {
                genTallFlowers.func_150548_a(5);
            }

            j1 = 0;

            while (true) {
                if (j1 < 5) {
                    k1 = par3 + par2Random.nextInt(16) + 8;
                    int i2 = par4 + par2Random.nextInt(16) + 8;
                    int l1 = par2Random.nextInt(par1World.getHeightValue(k1, i2) + 32);

                    if (!genTallFlowers.generate(par1World, par2Random, k1, l1, i2)) {
                        ++j1;
                        continue;
                    }
                }

                ++l;
                break;
            }
        }

        super.decorate(par1World, par2Random, par3, par4);
    }

    @Override
    public BiomeGenBase createMutation() {
        if (this.biomeID == BiomeGenBase.forest.biomeID) {
            BiomeGenForest biomegenforest = new BiomeGenForest(this.biomeID + 128, 1);
            biomegenforest.setHeight(new BiomeGenBase.Height(this.rootHeight, this.heightVariation + 0.2F));
            biomegenforest.setBiomeName("Flower Forest");
            biomegenforest.func_150557_a(6976549, true);
            biomegenforest.func_76733_a(8233509);
            return biomegenforest;
        } else {
            return this.biomeID != BiomeGenBase.birchForest.biomeID && this.biomeID != BiomeGenBase.birchForestHills.biomeID ? new BiomeGenMutated(this.biomeID + 128, this) {
                private static final String __OBFID = "CL_00000171";

                public void decorate(World world, Random random, int i, int j) {
                    this.baseBiome.decorate(world, random, i, j);
                }
            } : new BiomeGenMutated(this.biomeID + 128, this) {
                private static final String __OBFID = "CL_00000172";

                public WorldGenAbstractTree func_150567_a(Random random) {
                    return random.nextBoolean() ? getBigTree() : getTree();
                }
            };
        }
    }

    @Override
    public boolean isHighHumidity() {
        return true;
    }
}
