package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.HedgehogEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class HedgehogModel extends GeoModel<HedgehogEntity> {
    @Override
    public ResourceLocation getModelResource(HedgehogEntity animatable, GeoRenderer<HedgehogEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/hedgehog/hedgehog.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HedgehogEntity animatable, GeoRenderer<HedgehogEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/hedgehog/hedgehog_" + (animatable.isBaby() ? "baby" : "adult") + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(HedgehogEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/hedgehog/hedgehog.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(HedgehogEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(HedgehogEntity animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public void setCustomAnimations(HedgehogEntity animatable, long instanceId, AnimationState<HedgehogEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null && !animatable.isScared()) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if (entityData != null) {
                head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
                head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
            }
        }
    }
}