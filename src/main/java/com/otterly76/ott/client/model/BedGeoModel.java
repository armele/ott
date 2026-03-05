package com.otterly76.ott.client.model;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;

import static com.otterly76.ott.Constants.MOD_ID;

public class BedGeoModel extends GeoModel<BedAnimatable> {
    @Override
    public ResourceLocation getModelResource(BedAnimatable animatable) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/entity/bed/bed.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BedAnimatable animatable) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/entity/bed/" + animatable.getColor().getName() + ".png");
    }

    @Override
    public @Nullable ResourceLocation getAnimationResource(BedAnimatable animatable) {
        return null;
    }
}