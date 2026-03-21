package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SealEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class SealModel extends GeoModel<SealEntity> {
    @Override
    public ResourceLocation getModelResource(SealEntity animatable, GeoRenderer<SealEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/seal/seal.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SealEntity animatable, GeoRenderer<SealEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/seal/seal_" + (animatable.isBaby() ? "baby" : "adult") + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(SealEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/seal/seal.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SealEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(SealEntity animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public void setCustomAnimations(SealEntity animatable, long instanceId, AnimationState<SealEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null && !animatable.isLaying()) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if (entityData != null) {
                head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
                head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
            }
        }
    }
}