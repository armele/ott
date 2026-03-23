package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SmallFirefly;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SmallFireflyModel extends GeoModel<SmallFirefly> {
    @Override
    public ResourceLocation getModelResource(SmallFirefly animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<SmallFirefly> renderer) {
        return Constants.loc("geo/entity/firefly/firefly.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(SmallFirefly animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(SmallFirefly animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<SmallFirefly> renderer) {
        return Constants.loc("textures/entity/firefly/firefly.png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(SmallFirefly animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getAnimationResource(SmallFirefly animatable) {
        return Constants.loc("animations/entity/firefly/firefly.animation.json");
    }
}