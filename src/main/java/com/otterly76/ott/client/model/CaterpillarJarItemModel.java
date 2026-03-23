package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.item.custom.CaterpillarJarItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CaterpillarJarItemModel extends GeoModel<CaterpillarJarItem> {
    @Override
    public ResourceLocation getModelResource(CaterpillarJarItem animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<CaterpillarJarItem> renderer) {
        return Constants.loc("geo/block/butterfly_jar/caterpillarjar.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(CaterpillarJarItem animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(CaterpillarJarItem animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<CaterpillarJarItem> renderer) {
        return Constants.loc("textures/block/jar/caterpillar_jar.png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(CaterpillarJarItem animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getAnimationResource(CaterpillarJarItem animatable) {
        return Constants.loc("animations/block/butterfly_jar/caterpillarjar.animation.json");
    }

    @Override
    public void setCustomAnimations(CaterpillarJarItem animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<CaterpillarJarItem> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}