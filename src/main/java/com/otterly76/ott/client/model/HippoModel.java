package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Hippo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

@OnlyIn(Dist.CLIENT)
public class HippoModel extends GeoModel<Hippo> {
    @Override
    public ResourceLocation getModelResource(Hippo hippo, @Nullable GeoRenderer<Hippo> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/hippo/hippo.geo.json");
    }

    @Override
    public ResourceLocation getModelResource(Hippo hippo) {
        return this.getModelResource(hippo, null);
    }

    @Override
    public ResourceLocation getTextureResource(Hippo hippo, @Nullable GeoRenderer<Hippo> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/hippo/hippo.png");
    }

    @Override
    public ResourceLocation getTextureResource(Hippo hippo) {
        return this.getTextureResource(hippo, null);
    }

    @Override
    public ResourceLocation getAnimationResource(Hippo hippo) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/hippo/hippo.animation.json");
    }

    @Override
    public void setCustomAnimations(Hippo entity, long instanceId, @Nullable AnimationState<Hippo> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if (extraDataOfType == null) return;
        GeoBone skull = this.getAnimationProcessor().getBone("skull");

        if (skull != null) {
            if (entity.isBaby()) {
                skull.setScaleX(1.75F);
                skull.setScaleY(1.75F);
                skull.setScaleZ(1.75F);
            } else {
                skull.setScaleX(1.0F);
                skull.setScaleY(1.0F);
                skull.setScaleZ(1.0F);
            }
            skull.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            skull.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}