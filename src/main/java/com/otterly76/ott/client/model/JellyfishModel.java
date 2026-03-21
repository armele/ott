package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.JellyfishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class JellyfishModel extends GeoModel<JellyfishEntity> {
    @Override
    public ResourceLocation getModelResource(JellyfishEntity animatable, GeoRenderer<JellyfishEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/jellyfish/jellyfish_1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(JellyfishEntity animatable, GeoRenderer<JellyfishEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/jellyfish/jellyfish_1_" + animatable.getVariant() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(JellyfishEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/jellyfish/jellyfish_1.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(JellyfishEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(JellyfishEntity animatable) {
        return getTextureResource(animatable, null);
    }
}