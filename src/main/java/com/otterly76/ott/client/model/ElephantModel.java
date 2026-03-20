package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Elephant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;
import org.jetbrains.annotations.Nullable;

public class ElephantModel extends GeoModel<Elephant> {
    @Override
    @Deprecated
    public ResourceLocation getModelResource(Elephant animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getModelResource(Elephant animatable, @Nullable GeoRenderer<Elephant> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/elephant/elephant.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(Elephant animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Elephant animatable, @Nullable GeoRenderer<Elephant> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/elephant/elephant.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Elephant animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/elephant/elephant.animation.json");
    }

    @Override
    public void setCustomAnimations(Elephant entity, long instanceId, AnimationState<Elephant> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if (extraDataOfType == null) return;
        GeoBone head = this.getAnimationProcessor().getBone("head");
        if (head == null) head = this.getAnimationProcessor().getBone("skull");

        if (head != null) {
            head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }

        GeoBone saddle = this.getAnimationProcessor().getBone("saddle");
        if (saddle != null) {
            saddle.setHidden(!entity.isSaddled());
        }
    }
}