package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Catfish;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class CatfishModel extends GeoModel<Catfish> {
    @Override
    public ResourceLocation getModelResource(Catfish animatable, @Nullable GeoRenderer<Catfish> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/catfish/catfish.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(Catfish animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Catfish animatable, @Nullable GeoRenderer<Catfish> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/catfish/catfish.png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(Catfish animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getAnimationResource(Catfish animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/catfish/catfish.animation.json");
    }
}
