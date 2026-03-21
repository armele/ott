package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Butterfly;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ButterflyModel<T extends Butterfly> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<T> renderer) {
        return Constants.loc("geo/entity/butterfly/butterfly.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(T butterfly) {
        return getModelResource(butterfly, null);
    }

    @Override
    public ResourceLocation getTextureResource(T animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<T> renderer) {
        return Constants.loc("textures/entity/butterfly/" + animatable.getVariant().getName() + ".png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(T butterfly) {
        return getTextureResource(butterfly, null);
    }

    @Override
    public ResourceLocation getAnimationResource(T butterfly) {
        return Constants.loc("animations/entity/butterfly/butterfly.animation.json");
    }
}
