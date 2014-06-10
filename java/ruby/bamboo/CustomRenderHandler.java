package ruby.bamboo;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import ruby.bamboo.render.block.IRenderBlocks;
import ruby.bamboo.render.block.IRenderInventory;
import ruby.bamboo.render.block.RenderBambooBlock;
import ruby.bamboo.render.block.RenderBambooPane;
import ruby.bamboo.render.block.RenderCoordinateBlock;
import ruby.bamboo.render.block.RenderDelude;
import ruby.bamboo.render.block.RenderInvAndon;
import ruby.bamboo.render.block.RenderInvCampfire;
import ruby.bamboo.render.block.RenderInvManeki;
import ruby.bamboo.render.block.RenderInvMillStone;
import ruby.bamboo.render.block.RenderKitunebi;
import ruby.bamboo.render.block.RenderMultiBlock;
import ruby.bamboo.render.block.RenderMultiPot;
import ruby.bamboo.render.block.RenderPillar;
import ruby.bamboo.render.block.RenderQuadRotatedPillar;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CustomRenderHandler {
    public static final int kitunebiUID;
    public static final int coordinateCrossUID;
    public static final int bambooBlockUID;
    public static final int andonUID;
    public static final int campfireUID;
    public static final int bambooPaneUID;
    public static final int riceFieldUID;
    public static final int millStoneUID;
    public static final int pillarUID;
    public static final int deludeUID;
    public static final int manekiUID;
    public static final int multiPotUID;
    public static final int quadRotatedPillarUID;
    public static final int multiBlockUID;
    public static HashMap<Integer, IRenderBlocks> customRenderMap;
    public static HashMap<Integer, IRenderInventory> customRenderInvMap;
    private static CustomRenderHandler instance = new CustomRenderHandler();
    private final SimpleInvRender SimpleInvRenderInstance = new SimpleInvRender();
    private final Render3DInInventory Render3DInInvInstance = new Render3DInInventory();
    static {
        //2DInv
        customRenderMap = new HashMap<Integer, IRenderBlocks>();
        customRenderInvMap = new HashMap<Integer, IRenderInventory>();
        coordinateCrossUID = getUIDAndRegistSimpleInvRender();
        kitunebiUID = getUIDAndRegistSimpleInvRender();
        bambooPaneUID = getUIDAndRegistSimpleInvRender();
        riceFieldUID = getUIDAndRegistSimpleInvRender();
        bambooBlockUID = getUIDAndRegistSimpleInvRender();
        multiPotUID = getUIDAndRegistSimpleInvRender();
        multiBlockUID = getUIDAndRegistSimpleInvRender();
        // 3DInv
        andonUID = getUIDAndRegist3DRender();
        campfireUID = getUIDAndRegist3DRender();
        millStoneUID = getUIDAndRegist3DRender();
        pillarUID = getUIDAndRegist3DRender();
        deludeUID = getUIDAndRegist3DRender();
        manekiUID = getUIDAndRegist3DRender();
        quadRotatedPillarUID = getUIDAndRegist3DRender();
    }

    @SideOnly(Side.CLIENT)
    public static void init() {
        customRenderMap.put(kitunebiUID, new RenderKitunebi());
        customRenderMap.put(coordinateCrossUID, new RenderCoordinateBlock());
        customRenderMap.put(bambooBlockUID, RenderBambooBlock.instance);
        customRenderMap.put(bambooPaneUID, new RenderBambooPane());
        customRenderMap.put(pillarUID, RenderPillar.instance);
        customRenderMap.put(deludeUID, RenderDelude.instance);
        customRenderMap.put(multiPotUID, new RenderMultiPot());
        customRenderMap.put(quadRotatedPillarUID, RenderQuadRotatedPillar.instance);
        customRenderMap.put(multiBlockUID, new RenderMultiBlock());

        //Inventory
        customRenderInvMap.put(andonUID, new RenderInvAndon());
        //customRenderInvMap.put(bambooBlockUID, new RenderInvBambooBlock());
        customRenderInvMap.put(campfireUID, new RenderInvCampfire());
        customRenderInvMap.put(millStoneUID, new RenderInvMillStone());
        customRenderInvMap.put(pillarUID, RenderPillar.instance);
        customRenderInvMap.put(deludeUID, RenderDelude.instance);
        customRenderInvMap.put(manekiUID, new RenderInvManeki());
        customRenderInvMap.put(quadRotatedPillarUID, RenderQuadRotatedPillar.instance);
    }

    private static int getUIDAndRegistSimpleInvRender() {
        int id = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(id, instance.SimpleInvRenderInstance);
        return id;
    }

    private static int getUIDAndRegist3DRender() {
        int id = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(id, instance.Render3DInInvInstance);
        return id;
    }

    private class SimpleInvRender implements ISimpleBlockRenderingHandler {
        @Override
        public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        }

        @Override
        public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
            if (customRenderMap.containsKey(modelId)) {
                customRenderMap.get(modelId).renderBlock(renderer, block, x, y, z);
                return true;
            }
            return false;
        }

        @Override
        public int getRenderId() {
            return 0;
        }

        @Override
        public boolean shouldRender3DInInventory(int modelId) {
            return false;
        }
    }

    private class Render3DInInventory extends SimpleInvRender {
        @Override
        public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
            if (customRenderInvMap.containsKey(modelID)) {
                customRenderInvMap.get(modelID).renderInventory(renderer, block, metadata);
            }
        }

        @Override
        public boolean shouldRender3DInInventory(int modelId) {
            return true;
        }

    }
}
