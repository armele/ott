package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.SnowGolemGeoEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.SnowGolem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

public class SnowGolemGeoModel<T extends SnowGolem & SnowGolemGeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Ott.resource("geo/entity/snow_golem/snow_golem.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return Ott.resource("textures/entity/snow_golem/snow_golem_1.png");
    }

    @Override
    @Nullable
    public ResourceLocation getAnimationResource(T animatable) {
        return null;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone head = this.getAnimationProcessor().getBone("head");
        GeoBone leftHand = this.getAnimationProcessor().getBone("left_hand");
        GeoBone rightHand = this.getAnimationProcessor().getBone("right_hand");

        GeoModelUtils.applyHeadRotation(animationState, head);
    }
}




