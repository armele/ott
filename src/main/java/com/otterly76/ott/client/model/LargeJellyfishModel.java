package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.LargeJellyfishEntity;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class LargeJellyfishModel extends GeoModel<LargeJellyfishEntity> {
    @Override
    public ResourceLocation getModelResource(LargeJellyfishEntity animatable, @Nullable GeoRenderer<LargeJellyfishEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/jellyfish/jellyfish_1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LargeJellyfishEntity animatable, @Nullable GeoRenderer<LargeJellyfishEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/jellyfish/jellyfish_1_" + animatable.getVariant() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(LargeJellyfishEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/jellyfish/jellyfish_1.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(LargeJellyfishEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(LargeJellyfishEntity animatable) {
        return getTextureResource(animatable, null);
    }
}
