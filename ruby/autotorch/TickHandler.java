package ruby.autotorch;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements ITickHandler
{
    private Config config;
    private EntityPlayer player;
    private Random rand;
    private ItemStack itemStack;
    private int[] biomeHeght;
    private int range;
    TickHandler(Config config)
    {
        this.config = config;
        this.range=config.getRange();
        this.rand = new Random();
        this.biomeHeght = new int[BiomeGenBase.biomeList.length];

        for (int i = 0; i < biomeHeght.length; i++)
        {
            if (BiomeGenBase.biomeList[i] != null)
            {
                biomeHeght[i] = (int)(BiomeGenBase.biomeList[i].maxHeight * 64) + 64;
            }
            else
            {
                biomeHeght[i] = 0;
            }
        }
    }
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        if (type.equals(EnumSet.of(TickType.SERVER)))
        {
            if (player != null)
            {
                onTickInGame(player);
            }
        }
    }

    private void onTickInGame(EntityPlayer player)
    {
        for (int i = -(this.range); i < this.range; i++)
        {
            for (int j = -(this.range); j < this.range; j++)
            {
                canPlaceBlockHere(player.worldObj,
                                  (player.chunkCoordX + i) * 16 + rand.nextInt(16),
                                  (player.chunkCoordZ + j) * 16 + rand.nextInt(16),
                                  player,
                                  config.getBlock());
            }
        }
    }
    private void canPlaceBlockHere(World world, int posX, int posZ, EntityPlayer player, int blockId)
    {
        int posY = rand.nextInt(biomeHeght[world.getBiomeGenForCoordsBody(posX, posZ).biomeID] + 1);
        if(posY>0xFF){
        	posY=0xFF;
        }
        if (!world.isAirBlock(posX, posY, posZ))
        {
            return;
        }

        if (world.getBlockMaterial(posX, posY - 1, posZ) == Material.water || world.getBlockMaterial(posX, posY - 1, posZ) == Material.lava)
        {
            return;
        }

        if (getBlockLightValue(world, posX, posY, posZ) > config.getLight())
        {
            return;
        }

        if (itemStack != null)
        {
            if (itemStack.stackSize < 2)
            {
                itemStack.stackSize = 64;
            }
        }
        else
        {
            itemStack = new ItemStack(blockId, 64, 0);
        }

        if (canPlaceItemBlockOnSide(world, posX, posY, posZ, 0, blockId, itemStack))
        {
            Item.itemsList[blockId].onItemUse(itemStack, player, world, posX, posY, posZ, 0, 0, 0, 0);
        }
    }
    private int getBlockLightValue(World world, int i, int j, int k)
    {
        Chunk chunk = world.getChunkFromChunkCoords(i >> 4, k >> 4);
        i &= 0xf;
        k &= 0xf;
        if(chunk.isChunkLoaded){
            return chunk.getBlockLightValue(i, j, k, 15);
        }else{
        	return 0xFF;
        }
    }
    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.SERVER);
    }

    @Override
    public String getLabel()
    {
        return "AutoTorch";
    }
    public void setPlayer(EntityPlayer player)
    {
        this.player = player;
    }
    private boolean canPlaceItemBlockOnSide(World par1World, int par2, int par3, int par4, int par5, int blockID, ItemStack is)
    {
        int var8 = par1World.getBlockId(par2, par3, par4);

        if (var8 == Block.snow.blockID)
        {
            par5 = 1;
        }
        else if (var8 != Block.vine.blockID && var8 != Block.tallGrass.blockID && var8 != Block.deadBush.blockID
                 && (Block.blocksList[var8] == null || !Block.blocksList[var8].isBlockReplaceable(par1World, par2, par3, par4)))
        {
            if (par5 == 0)
            {
                --par3;
            }

            if (par5 == 1)
            {
                ++par3;
            }

            if (par5 == 2)
            {
                --par4;
            }

            if (par5 == 3)
            {
                ++par4;
            }

            if (par5 == 4)
            {
                --par2;
            }

            if (par5 == 5)
            {
                ++par2;
            }
        }

        return par1World.canPlaceEntityOnSide(blockID, par2, par3, par4, false, par5, (Entity)null, is);
    }
}
