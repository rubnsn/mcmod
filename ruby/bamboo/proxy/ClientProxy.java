package ruby.bamboo.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import ruby.bamboo.BambooInit;
import ruby.bamboo.BambooLocalize;
import ruby.bamboo.BambooUtil;
import ruby.bamboo.CustomRenderHandler;
import ruby.bamboo.entity.*;
import ruby.bamboo.entity.magatama.*;
import ruby.bamboo.render.*;
import ruby.bamboo.render.magatama.*;
import ruby.bamboo.tileentity.TileEntityAndon;
import ruby.bamboo.tileentity.TileEntityCampfire;
import ruby.bamboo.tileentity.TileEntityManeki;
import ruby.bamboo.tileentity.TileEntityMillStone;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
        CustomRenderHandler.init();
        BambooLocalize.init();
        addRenderer();
    }

    @Override
    public void registerTESTileEntity() {
        ClientRegistry.registerTileEntity(TileEntityCampfire.class, "Camp fire", new RenderCampfire());
        ClientRegistry.registerTileEntity(TileEntityAndon.class, "Andon", new RenderAndon());
        ClientRegistry.registerTileEntity(TileEntityMillStone.class, "MillStone", new RenderMillStone());
        ClientRegistry.registerTileEntity(TileEntityManeki.class, "MManeki", new RenderManeki());
    }

    private void addRenderer() {

        MinecraftForgeClient.registerItemRenderer(BambooInit.bambooBowIID, (IItemRenderer) BambooUtil.getItemInstance(BambooInit.bambooBowIID));
        RenderingRegistry.registerEntityRenderingHandler(EntityBambooSpear.class, new RenderBSpear());
        RenderingRegistry.registerEntityRenderingHandler(EntitySlideDoor.class, new RenderSlideDoor());
        RenderingRegistry.registerEntityRenderingHandler(EntityKakeziku.class, new RenderKakeziku());
        RenderingRegistry.registerEntityRenderingHandler(EntitySakuraPetal.class, new RenderPetal());
        RenderingRegistry.registerEntityRenderingHandler(EntityHuton.class, new RenderHuton());
        RenderingRegistry.registerEntityRenderingHandler(EntityFirecracker.class, new RenderFirecracker());
        RenderingRegistry.registerEntityRenderingHandler(EntityWindChime.class, new RenderWindBell());
        RenderingRegistry.registerEntityRenderingHandler(EntityWind.class, new Render() {
            @Override
            public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
            }

            @Override
            protected ResourceLocation getEntityTexture(Entity entity) {
                return null;
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityWindmill.class, new RenderWindmill());
        RenderingRegistry.registerEntityRenderingHandler(EntityWaterwheel.class, new RenderWaterwheel());
        RenderingRegistry.registerEntityRenderingHandler(EntityKaginawa.class, new RenderKaginawa());
        RenderingRegistry.registerEntityRenderingHandler(EntityObon.class, new RenderObon());
        RenderingRegistry.registerEntityRenderingHandler(EntityMagatama.class, new RenderMagatama());
        RenderingRegistry.registerEntityRenderingHandler(EntityFlareEffect.class, new RenderFlareEffect());
        RenderingRegistry.registerEntityRenderingHandler(EntityThunderEffect.class, new RenderThunderEffect());
        RenderingRegistry.registerEntityRenderingHandler(EntityGravityHole.class, new RenderGravityHole());
        RenderingRegistry.registerEntityRenderingHandler(EntityRuneEffect.class, new RenderRuneEffect());
        RenderingRegistry.registerEntityRenderingHandler(EntityClock.class, new RenderClock());
    }
}
