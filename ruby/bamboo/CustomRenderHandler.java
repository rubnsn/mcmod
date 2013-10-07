package ruby.bamboo;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.block.BlockBambooPane;
import ruby.bamboo.block.BlockKitunebi;
import ruby.bamboo.block.BlockPillar;
import ruby.bamboo.block.BlockRiceField;
import ruby.bamboo.block.IDelude;
import ruby.bamboo.render.CustomRenderBlocks;
import ruby.bamboo.render.RenderAndon;
import ruby.bamboo.render.RenderCampfire;
import ruby.bamboo.render.RenderManeki;
import ruby.bamboo.render.RenderMillStone;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CustomRenderHandler
{
    public static int kitunebiUID;
    public static int bambooUID;
    public static int bambooBlockUID;
    public static int madUID;
    public static int andonUID;
    public static int campfireUID;
    public static int bambooPaneUID;
    public static int riceFieldUID;
    public static int millStoneUID;
    public static int pillarUID;
    public static int deludeUID;
    public static int manekiUID;
    public static CustomRenderBlocks customRenders;
    private static CustomRenderHandler instance = new CustomRenderHandler();
    private SimpleInvRender SimpleInvRenderInstance = new SimpleInvRender();
    private Render3DInInventory Render3DInInvInstance = new Render3DInInventory();
    @SideOnly(Side.CLIENT)
    public static void init()
    {
        customRenders = new CustomRenderBlocks();
        bambooUID = getUIDAndRegistSimpleInvRender();
        kitunebiUID = getUIDAndRegistSimpleInvRender();
        bambooPaneUID = getUIDAndRegistSimpleInvRender();
        riceFieldUID = getUIDAndRegistSimpleInvRender();
        //3DInv
        andonUID = getUIDAndRegist3DRender();
        bambooBlockUID = getUIDAndRegist3DRender();
        campfireUID = getUIDAndRegist3DRender();
        millStoneUID = getUIDAndRegist3DRender();
        pillarUID = getUIDAndRegist3DRender();
        deludeUID = getUIDAndRegist3DRender();
        manekiUID = getUIDAndRegist3DRender();
    }
    private static int getUIDAndRegistSimpleInvRender()
    {
        int id = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(id, instance.SimpleInvRenderInstance);
        return id;
    }
    private static int getUIDAndRegist3DRender()
    {
        int id = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(id, instance.Render3DInInvInstance);
        return id;
    }
    private class SimpleInvRender implements ISimpleBlockRenderingHandler
    {
        @Override
        public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

        @Override
        public boolean renderWorldBlock(IBlockAccess world, int x, int y,
                                        int z, Block block, int modelId, RenderBlocks renderer)
        {
            int meta = world.getBlockMetadata(x, y, z);

            if (kitunebiUID == modelId)
            {
                if (((BlockKitunebi)block).isVisible())
                {
                    renderer.renderCrossedSquares(block, x, y, z);
                }

                return true;
            }

            if (bambooUID == modelId)
            {
                if (meta  != 15)
                {
                    renderer.renderBlockCrops(block, x, y, z);
                }
                else
                {
                    renderer.renderCrossedSquares(block, x, y, z);
                }

                return true;
            }

            if (bambooBlockUID == modelId)
            {
                if (meta == 0)
                {
                    renderer.renderBlockCrops(block, x, y, z);
                }
                else
                {
                    renderer.renderStandardBlock(block, x, y, z);
                }

                return true;
            }

            if (bambooPaneUID == modelId)
            {
                customRenders.renderBlockBambooPane(renderer, (BlockBambooPane) block, x, y, z);
                return true;
            }

            if (riceFieldUID == modelId)
            {
                customRenders.renderBlockRiceField(renderer, (BlockRiceField) block, x, y, z);
                return true;
            }

            if (pillarUID == modelId)
            {
                customRenders.renderBlockPillar(renderer, (BlockPillar)block, x, y, z);
            }

            if (deludeUID == modelId)
            {
                customRenders.renderDelude(renderer, block, x, y, z);
            }

            return false;
        }

        @Override
        public boolean shouldRender3DInInventory()
        {
            return false;
        }

        @Override
        public int getRenderId()
        {
            return 0;
        }
    }
    private class Render3DInInventory extends SimpleInvRender
    {
        @Override
        public void renderInventoryBlock(Block block, int metadata, int modelID,
                                         RenderBlocks renderer)
        {
            if (andonUID == modelID)
            {
                RenderAndon.instance.renderInv();
            }
            else if (bambooBlockUID == modelID)
            {
                if (metadata == 0)
                {
                    Tessellator tessellator = Tessellator.instance;
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0.0F, 1F, 0.0F);
                    renderer.drawCrossedSquares(block, metadata, -0.5D, -0.5D, -0.5D, 1F);
                    tessellator.draw();
                }
            }
            else if (campfireUID == modelID)
            {
                RenderCampfire.instance.renderInv();
            }
            else if (millStoneUID == modelID)
            {
                RenderMillStone.instance.renderInv();
            }
            else if (pillarUID == modelID)
            {
                standardItemRender(block, renderer, metadata);
            }
            else if (deludeUID == modelID)
            {
                switch (((IDelude)block).getOriginalRenderType())
                {
                    case 0:
                        standardItemRender(block, renderer, metadata);
                        break;

                    case 10:
                        renderItemStair(renderer, block);
                        break;
                }
            }
            else if (manekiUID == modelID)
            {
                RenderManeki.instance.renderInv();
            }
        }

        @Override
        public boolean shouldRender3DInInventory()
        {
            return true;
        }
        private void renderItemStair(RenderBlocks renderer, Block par1Block)
        {
            Tessellator tessellator = Tessellator.instance;
            renderer.setRenderBoundsFromBlock(par1Block);

            for (int k = 0; k < 2; ++k)
            {
                if (k == 0)
                {
                    renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
                }

                if (k == 1)
                {
                    renderer.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
                }

                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1.0F, 0.0F);
                renderer.renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(par1Block, 0));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderer.renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(par1Block, 1));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1.0F);
                renderer.renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(par1Block, 2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderer.renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(par1Block, 3));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1.0F, 0.0F, 0.0F);
                renderer.renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(par1Block, 4));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderer.renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSide(par1Block, 5));
                tessellator.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
        }
        private void standardItemRender(Block par1Block, RenderBlocks renderer, int par2)
        {
            Tessellator tessellator = Tessellator.instance;
            par1Block.setBlockBoundsForItemRender();
            renderer.setRenderBoundsFromBlock(par1Block);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 0, par2));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 1, par2));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 2, par2));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 3, par2));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 4, par2));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(par1Block, 5, par2));
            tessellator.draw();
            GL11.glTranslatef(1F, 1F, 1F);
        }
    }
}
