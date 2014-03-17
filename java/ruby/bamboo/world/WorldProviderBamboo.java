package ruby.bamboo.world;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import ruby.bamboo.Config;

public class WorldProviderBamboo extends WorldProvider {

    @Override
    public String getDimensionName() {
        return "Bamboo";
    }

    @Override
    protected void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerBamboo(this.worldObj);
        this.dimensionId = Config.dimensionId;
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderBamboo(this.worldObj, this.worldObj.getSeed(), true);
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public String getWelcomeMessage() {
        return "This world is a test world,Don't use the main world";
    }

}
