package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Caterpillar;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CaterpillarModel<T extends Caterpillar> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<T> renderer) {
        return Constants.loc("geo/entity/caterpillar/caterpillar.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(T caterpillar) {
        return getModelResource(caterpillar, null);
    }

    @Override
    public ResourceLocation getTextureResource(T animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<T> renderer) {
        return Constants.loc("textures/entity/caterpillar/caterpillar.png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(T caterpillar) {
        return getTextureResource(caterpillar, null);
    }

    @Override
    public ResourceLocation getAnimationResource(T caterpillar) {
        return Constants.loc("animations/entity/caterpillar/caterpillar.animation.json");
    }
}