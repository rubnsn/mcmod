package ruby.bamboo;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.block.BlockBambooPane;
import ruby.bamboo.block.BlockKitunebi;
import ruby.bamboo.block.BlockPillar;
import ruby.bamboo.block.BlockRiceField;
import ruby.bamboo.block.IDelude;
import ruby.bamboo.render.RenderAndon;
import ruby.bamboo.render.RenderCampfire;
import ruby.bamboo.render.RenderManeki;
import ruby.bamboo.render.RenderMillStone;
import ruby.bamboo.render.block.IRenderBlocks;
import ruby.bamboo.render.block.RenderBamboo;
import ruby.bamboo.render.block.RenderBambooBlock;
import ruby.bamboo.render.block.RenderBambooPane;
import ruby.bamboo.render.block.RenderDelude;
import ruby.bamboo.render.block.RenderKitunebi;
import ruby.bamboo.render.block.RenderPillar;
import ruby.bamboo.render.block.RenderRiceField;
import ruby.bamboo.render.inventory.IRenderInventory;
import ruby.bamboo.render.inventory.RenderInvAndon;
import ruby.bamboo.render.inventory.RenderInvBambooBlock;
import ruby.bamboo.render.inventory.RenderInvCampfire;
import ruby.bamboo.render.inventory.RenderInvDelude;
import ruby.bamboo.render.inventory.RenderInvManeki;
import ruby.bamboo.render.inventory.RenderInvMillStone;
import ruby.bamboo.render.inventory.RenderInvPillar;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CustomRenderHandler {
    public static final int kitunebiUID;
    public static final int bambooUID;
    public static final int bambooBlockUID;
    public static final int andonUID;
    public static final int campfireUID;
    public static final int bambooPaneUID;
    public static final int riceFieldUID;
    public static final int millStoneUID;
    public static final int pillarUID;
    public static final int deludeUID;
    public static final int manekiUID;
    public static HashMap<Integer, IRenderBlocks> customRenderMap;
    public static HashMap<Integer, IRenderInventory> customRenderInvMap;
    private static CustomRenderHandler instance = new CustomRenderHandler();
    private final SimpleInvRender SimpleInvRenderInstance = new SimpleInvRender();
    private final Render3DInInventory Render3DInInvInstance = new Render3DInInventory();
    static {
        customRenderMap = new HashMap<Integer, IRenderBlocks>();
        customRenderInvMap = new HashMap<Integer, IRenderInventory>();
        bambooUID = getUIDAndRegistSimpleInvRender();
        kitunebiUID = getUIDAndRegistSimpleInvRender();
        bambooPaneUID = getUIDAndRegistSimpleInvRender();
        riceFieldUID = getUIDAndRegistSimpleInvRender();
        // 3DInv
        andonUID = getUIDAndRegist3DRender();
        bambooBlockUID = getUIDAndRegist3DRender();
        campfireUID = getUIDAndRegist3DRender();
        millStoneUID = getUIDAndRegist3DRender();
        pillarUID = getUIDAndRegist3DRender();
        deludeUID = getUIDAndRegist3DRender();
        manekiUID = getUIDAndRegist3DRender();
    }

    @SideOnly(Side.CLIENT)
    public static void init() {
        customRenderMap.put(kitunebiUID, new RenderKitunebi());
        customRenderMap.put(bambooUID, new RenderBamboo());
        customRenderMap.put(bambooBlockUID, new RenderBambooBlock());
        customRenderMap.put(bambooPaneUID, new RenderBambooPane());
        customRenderMap.put(riceFieldUID, new RenderRiceField());
        customRenderMap.put(pillarUID, new RenderPillar());
        customRenderMap.put(deludeUID, new RenderDelude());
        //Inventory
        customRenderInvMap.put(andonUID, new RenderInvAndon());
        customRenderInvMap.put(bambooBlockUID, new RenderInvBambooBlock());
        customRenderInvMap.put(campfireUID, new RenderInvCampfire());
        customRenderInvMap.put(millStoneUID, new RenderInvMillStone());
        customRenderInvMap.put(pillarUID, new RenderInvPillar());
        customRenderInvMap.put(deludeUID, new RenderInvDelude());
        customRenderInvMap.put(manekiUID, new RenderInvManeki());

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
                customRenderMap.get(modelId).render(renderer, block, x, y, z);
                return true;
            }
            return false;
        }

        @Override
        public boolean shouldRender3DInInventory() {
            return false;
        }

        @Override
        public int getRenderId() {
            return 0;
        }
    }

    private class Render3DInInventory extends SimpleInvRender {
        @Override
        public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
            if (customRenderInvMap.containsKey(modelID)) {
                customRenderInvMap.get(modelID).render(renderer, block, metadata);
            }
        }

        @Override
        public boolean shouldRender3DInInventory() {
            return true;
        }

    }
}
