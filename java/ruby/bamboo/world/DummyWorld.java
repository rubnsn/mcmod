package ruby.bamboo.world;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.util.ForgeDirection;

public class DummyWorld extends World implements IBlockAccess {
    public final static World dummyInstance = new DummyWorld();
    public static int lastSetMeta;

    public DummyWorld() {
        super(new DummySaveHandler(), null, new WorldSettings(0, GameType.NOT_SET, false, false, WorldType.FLAT), null, null);
    }

    @Override
    public boolean setBlockMetadataWithNotify(int par1, int par2, int par3, int par4, int par5) {
        lastSetMeta = par4;
        return true;
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return null;
    }

    @Override
    public Entity getEntityByID(int var1) {
        return null;
    }

    @Override
    public Block getBlock(int var1, int var2, int var3) {
        return null;
    }

    @Override
    public TileEntity getTileEntity(int var1, int var2, int var3) {
        return null;
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int var1, int var2, int var3, int var4) {
        return 0;
    }

    @Override
    public int getBlockMetadata(int var1, int var2, int var3) {
        return 0;
    }

    @Override
    public boolean isAirBlock(int var1, int var2, int var3) {
        return false;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int var1, int var2) {
        return null;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public boolean extendedLevelsInChunkCache() {
        return false;
    }

    @Override
    public Vec3Pool getWorldVec3Pool() {
        return null;
    }

    @Override
    public int isBlockProvidingPowerTo(int var1, int var2, int var3, int var4) {
        return 0;
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
        return false;
    }

    private static class DummySaveHandler implements ISaveHandler {

        @Override
        public WorldInfo loadWorldInfo() {
            return null;
        }

        @Override
        public void checkSessionLock() throws MinecraftException {
        }

        @Override
        public IChunkLoader getChunkLoader(WorldProvider var1) {
            return null;
        }

        @Override
        public void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2) {
        }

        @Override
        public void saveWorldInfo(WorldInfo var1) {
        }

        @Override
        public IPlayerFileData getSaveHandler() {
            return null;
        }

        @Override
        public void flush() {
        }

        @Override
        public File getWorldDirectory() {
            return null;
        }

        @Override
        public File getMapFileFromName(String var1) {
            return null;
        }

        @Override
        public String getWorldDirectoryName() {
            return null;
        }

    }
}
