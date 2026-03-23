package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.entity.ButterflyJarBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ButterflyJarModel extends GeoModel<ButterflyJarBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ButterflyJarBlockEntity animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<ButterflyJarBlockEntity> renderer) {
        return Constants.loc("geo/block/butterfly_jar/" + animatable.getVariant().getName() + "jar.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(ButterflyJarBlockEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(ButterflyJarBlockEntity animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<ButterflyJarBlockEntity> renderer) {
        return Constants.loc("textures/block/butterfly_jar/" + animatable.getVariant().getJarTextureName() + ".png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(ButterflyJarBlockEntity animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getAnimationResource(ButterflyJarBlockEntity animatable) {
        return Constants.loc("animations/block/butterfly_jar/" + animatable.getVariant().getName() + "jar.animation.json");
    }
}