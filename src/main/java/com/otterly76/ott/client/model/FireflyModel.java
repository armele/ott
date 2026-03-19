package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Firefly;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FireflyModel<T extends Firefly> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<T> renderer) {
        return Constants.loc("geo/entity/firefly/firefly.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(T firefly) {
        return getModelResource(firefly, null);
    }

    @Override
    public ResourceLocation getTextureResource(T animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<T> renderer) {
        return Constants.loc("textures/entity/firefly/firefly.png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(T firefly) {
        return getTextureResource(firefly, null);
    }

    @Override
    public ResourceLocation getAnimationResource(T firefly) {
        return Constants.loc("animations/entity/firefly/firefly.animation.json");
    }
}