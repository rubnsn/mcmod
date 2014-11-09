package mmm.littleMaidMob;

import mmm.lib.multiModel.RenderMultiModel;
import mmm.lib.multiModel.model.mc162.IModelCaps;
import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderLittleMaid extends RenderMultiModel {

    public RenderLittleMaid(float pShadowSize) {
        super(pShadowSize);

        modelMain.textures = new ResourceLocation[0];
        //		modelMain.textures = new ResourceLocation[] { textures };
        /*
        		modelMain.model = new ModelLittleMaid_Aug();
        		try {
        			ClassLoader lcl = getClass().getClassLoader();
        			Class lc = lcl.loadClass("ModelLittleMaid_Kelo");
        			Constructor<ModelMultiBase> lcc = lc.getConstructor();
        			modelMain.model = lcc.newInstance();
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        */
    }

    @Override
    public void setModelValues(EntityLivingBase par1EntityLiving, double par2, double par4, double par6, float par8, float par9, IModelCaps pEntityCaps) {
        EntityLittleMaidBase lmaid = (EntityLittleMaidBase) par1EntityLiving;
        super.setModelValues(par1EntityLiving, par2, par4, par6, par8, par9, pEntityCaps);

        //      modelMain.setRender(this);
        //      modelMain.setEntityCaps(pEntityCaps);
        //      modelMain.showAllParts();
        //      modelMain.isAlphablend = true;
        //      modelFATT.isAlphablend = true;

        modelMain.setCapsValue(IModelCaps.caps_heldItemLeft, (Integer) 0);
        modelMain.setCapsValue(IModelCaps.caps_heldItemRight, (Integer) 0);
        //      modelMain.setCapsValue(MMM_IModelCaps.caps_onGround, renderSwingProgress(lmaid, par9));
        modelMain.setCapsValue(IModelCaps.caps_onGround, lmaid.getSwingStatus().mstatSwingStatus[0].getSwingProgress(par9), lmaid.getSwingStatus().mstatSwingStatus[1].getSwingProgress(par9));
        modelMain.setCapsValue(IModelCaps.caps_isRiding, lmaid.isRiding());
        modelMain.setCapsValue(IModelCaps.caps_isSneak, lmaid.isSneaking());
        //modelMain.setCapsValue(IModelCaps.caps_aimedBow, lmaid.isAimebow());
        modelMain.setCapsValue(IModelCaps.caps_isWait, lmaid.isMaidWait());
        modelMain.setCapsValue(IModelCaps.caps_isChild, lmaid.isChild());
        //modelMain.setCapsValue(IModelCaps.caps_entityIdFactor, lmaid.entityIdFactor);
        modelMain.setCapsValue(IModelCaps.caps_ticksExisted, lmaid.ticksExisted);
        modelMain.setCapsValue(IModelCaps.caps_dominantArm, lmaid.getSwingStatus().getDominantArm());
        // だが無意味だ
        //      plittleMaid.textureModel0.isChild = plittleMaid.textureModel1.isChild = plittleMaid.textureModel2.isChild = plittleMaid.isChild();
    }

    /*
    @Override
    public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
        doRender((EntityLittleMaidBase) par1EntityLiving, par2, par4, par6, par8, par9);
    }
    */

    @Override
    public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
        EntityLittleMaidBase lmm = (EntityLittleMaidBase) par1EntityLiving;
        fcaps = (IModelCaps) lmm.multiModel.modelCaps;
        //      doRenderLitlleMaid(lmm, par2, par4, par6, par8, par9);
        renderModelMulti(lmm, par2, par4, par6, par8, par9, fcaps);
        // ロープ
        //      func_110827_b(lmm, par2, par4 - modelMain.model.getLeashOffset(lmm.maidCaps), par6, par8, par9);
    }

    /*
        public void doRender(EntityLittleMaidBase pEntity, double pX, double pY, double pZ, float par8, float par9) {
            modelMain.isRendering = true;
            //		modelMain.model = pEntity.getModel();
            //		modelContainer = MultiModelManager.instance.getMultiModel("MMM_Aug");
            modelContainer = pEntity.multiModel.model;
            modelMain.model = (ModelMultiBase) modelContainer.getModelClass()[0];
            super.doRender(pEntity, pX, pY, pZ, par8, par9);

        }
      */

    @Override
    protected void passSpecialRender(EntityLivingBase par1EntityLiving, double par2, double par4, double par6) {
        super.passSpecialRender(par1EntityLiving, par2, par4, par6);

        EntityLittleMaidBase llmm = (EntityLittleMaidBase) par1EntityLiving;
        // 追加分
        llmm.modeController.showSpecial(this, par2, par4, par6);

    }

    /*
        @Override
        protected int getColorMultiplier(EntityLivingBase par1EntityLiving, float par2, float par3) {
            return ((EntityLittleMaidBase)par1EntityLiving).colorMultiplier(par2, par3);
        }
    */
    @Override
    protected ResourceLocation getEntityTexture(Entity var1) {
        // TODO Auto-generated method stub
        //		return ((EntityLittleMaidMob)var1).getTexture();
        //		return textures;
        return modelContainer.getTexture(((EntityLittleMaidBase) var1).multiModel.getColor());
    }

}
