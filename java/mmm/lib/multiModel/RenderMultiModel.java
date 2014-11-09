package mmm.lib.multiModel;

import mmm.lib.MMMLib;
import mmm.lib.multiModel.model.mc162.IModelCaps;
import mmm.lib.multiModel.model.mc162.ModelBaseDuo;
import mmm.lib.multiModel.model.mc162.ModelBaseSolo;
import mmm.lib.multiModel.model.mc162.ModelMultiBase;
import mmm.lib.multiModel.texture.IMultiModelEntity;
import mmm.lib.multiModel.texture.MultiModelContainer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderMultiModel extends RenderLiving {

    public ModelBaseSolo modelMain;
    public ModelBaseDuo modelFATT;
    public MultiModelContainer modelContainer;
    public IModelCaps fcaps;

    public RenderMultiModel(float pShadowSize) {
        super(null, pShadowSize);
        modelFATT = new ModelBaseDuo(this);
        modelFATT.isModelAlphablend = MMMLib.isModelAlphaBlend;
        modelFATT.isRendering = true;
        modelMain = new ModelBaseSolo(this);
        modelMain.isModelAlphablend = MMMLib.isModelAlphaBlend;
        modelMain.capsLink = modelFATT;
        mainModel = modelMain;
        setRenderPassModel(modelFATT);
    }

    protected int showArmorParts(EntityLivingBase par1EntityLiving, int par2, float par3) {
        // アーマーの表示設定
        modelFATT.renderParts = par2;
        modelFATT.renderCount = 0;
        ItemStack is = par1EntityLiving.getEquipmentInSlot(par2 + 1);
        if (is != null && is.stackSize > 0) {
            modelFATT.showArmorParts(par2);
            return is.isItemEnchanted() ? 15 : 1;
        }

        return -1;
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
        return showArmorParts((EntityLivingBase) par1EntityLiving, par2, par3);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entityliving, float f) {
        Float lscale = (Float) modelMain.getCapsValue(IModelCaps.caps_ScaleFactor);
        if (lscale != null) {
            GL11.glScalef(lscale, lscale, lscale);
        }
    }

    public void setModelValues(EntityLivingBase par1EntityLiving, double par2, double par4, double par6, float par8, float par9, IModelCaps pEntityCaps) {
        if (par1EntityLiving instanceof IMultiModelEntity) {
            IMultiModelEntity ltentity = (IMultiModelEntity) par1EntityLiving;
            modelContainer = ltentity.getMultiModel().model;
            modelMain.model = (ModelMultiBase) modelContainer.getModelClass()[0];
            modelFATT.modelInner = (ModelMultiBase) modelContainer.getModelClass()[1];
            modelFATT.modelOuter = (ModelMultiBase) modelContainer.getModelClass()[2];
            //          modelMain.model = ((MMM_TextureBox)ltentity.getTextureBox()[0]).models[0];
            modelMain.textures = new ResourceLocation[] { getEntityTexture(par1EntityLiving) };
            //          modelFATT.modelInner = ((MMM_TextureBox)ltentity.getTextureBox()[1]).models[1];
            //          modelFATT.modelOuter = ((MMM_TextureBox)ltentity.getTextureBox()[1]).models[2];
            //テクスチャは拾える？
            ItemStack is = par1EntityLiving.getEquipmentInSlot(2);
            if (is != null) {
                //TODO:効率よく…
                ResourceLocation[] loc = new ResourceLocation[] { modelContainer.getArmorTexture(MultiModelManager.tx_armor1, is), modelContainer.getArmorTexture(MultiModelManager.tx_armor1, is), modelContainer.getArmorTexture(MultiModelManager.tx_armor2, is), modelContainer.getArmorTexture(MultiModelManager.tx_armor2, is) };
                modelFATT.textureInner = loc;
                modelFATT.textureOuter = loc;
                //modelFATT.textureInnerLight = ltentity.getTextures(3);
                //modelFATT.textureOuterLight = ltentity.getTextures(4);
            }
            modelFATT.textureLightColor = (float[]) modelFATT.getCapsValue(IModelCaps.caps_textureLightColor, pEntityCaps);

        }
        modelMain.setEntityCaps(pEntityCaps);
        modelFATT.setEntityCaps(pEntityCaps);
        modelMain.setRender(this);
        modelFATT.setRender(this);
        modelMain.showAllParts();
        modelFATT.showAllParts();
        modelMain.isAlphablend = true;
        modelMain.isRendering = true;
        modelFATT.isAlphablend = true;
        modelMain.renderCount = 0;
        modelFATT.renderCount = 0;
        modelMain.lighting = modelFATT.lighting = par1EntityLiving.getBrightnessForRender(par8);

        modelMain.setCapsValue(IModelCaps.caps_heldItemLeft, (Integer) 0);
        modelMain.setCapsValue(IModelCaps.caps_heldItemRight, (Integer) 0);
        modelMain.setCapsValue(IModelCaps.caps_onGround, renderSwingProgress(par1EntityLiving, par9));
        modelMain.setCapsValue(IModelCaps.caps_isRiding, par1EntityLiving.isRiding());
        modelMain.setCapsValue(IModelCaps.caps_isSneak, par1EntityLiving.isSneaking());
        modelMain.setCapsValue(IModelCaps.caps_aimedBow, false);
        modelMain.setCapsValue(IModelCaps.caps_isWait, false);
        modelMain.setCapsValue(IModelCaps.caps_isChild, par1EntityLiving.isChild());
        modelMain.setCapsValue(IModelCaps.caps_entityIdFactor, 0F);
        modelMain.setCapsValue(IModelCaps.caps_ticksExisted, par1EntityLiving.ticksExisted);
    }

    @Override
    public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
        fcaps = (IModelCaps) ((IMultiModelEntity) par1EntityLiving).getMultiModel().modelCaps;
        doRender((EntityLivingBase) par1EntityLiving, par2, par4, par6, par8, par9);
    }

    public void renderModelMulti(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9, IModelCaps pEntityCaps) {
        setModelValues(par1EntityLiving, par2, par4, par6, par8, par9, pEntityCaps);
        super.doRender(par1EntityLiving, par2, par4, par6, par8, par9);

    }

    @Override
    protected ResourceLocation getEntityTexture(Entity var1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void renderModel(EntityLivingBase par1EntityLiving, float par2, float par3, float par4, float par5, float par6, float par7) {
        if (!par1EntityLiving.isInvisible()) {
            modelMain.setArmorRendering(true);
        } else {
            modelMain.setArmorRendering(false);
        }
        // アイテムのレンダリング位置を獲得するためrenderを呼ぶ必要がある
        mainModel.render(par1EntityLiving, par2, par3, par4, par5, par6, par7);
    }

    @Override
    protected void renderEquippedItems(EntityLivingBase par1EntityLiving, float par2) {
        // ハードポイントの描画
        modelMain.renderItems(par1EntityLiving, this);
        renderArrowsStuckInEntity(par1EntityLiving, par2);
    }

    @Override
    protected int getColorMultiplier(EntityLivingBase par1EntityLivingBase, float par2, float par3) {
        modelMain.renderCount = 16;
        return super.getColorMultiplier(par1EntityLivingBase, par2, par3);
    }

    @Override
    protected int inheritRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3) {
        int li = super.inheritRenderPass(par1EntityLivingBase, par2, par3);
        modelFATT.renderCount = 16;
        return li;
    }

}
