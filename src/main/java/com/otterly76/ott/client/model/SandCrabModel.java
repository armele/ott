package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.SandCrabEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SandCrabModel extends GeoModel<SandCrabEntity> {
    @Override
    public ResourceLocation getModelResource(SandCrabEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/sand_crab/sand_crab.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SandCrabEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/sand_crab/sand_crab.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SandCrabEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/sand_crab/sand_crab.animation.json");
    }
}
