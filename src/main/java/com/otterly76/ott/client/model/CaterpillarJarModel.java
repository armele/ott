package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.entity.CaterpillarJarBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CaterpillarJarModel extends GeoModel<CaterpillarJarBlockEntity> {
    @Override
    public ResourceLocation getModelResource(CaterpillarJarBlockEntity animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<CaterpillarJarBlockEntity> renderer) {
        return Constants.loc("geo/block/butterfly_jar/caterpillarjar.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(CaterpillarJarBlockEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(CaterpillarJarBlockEntity animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<CaterpillarJarBlockEntity> renderer) {
        return Constants.loc("textures/block/jar/caterpillar_jar.png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(CaterpillarJarBlockEntity animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getAnimationResource(CaterpillarJarBlockEntity animatable) {
        return Constants.loc("animations/block/butterfly_jar/caterpillarjar.animation.json");
    }

    @Override
    public void setCustomAnimations(CaterpillarJarBlockEntity animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<CaterpillarJarBlockEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}