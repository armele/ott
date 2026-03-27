package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.MaulerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MaulerModel extends GeoModel<MaulerEntity> {
    @Override
    public ResourceLocation getModelResource(MaulerEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/mauler/mauler.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MaulerEntity entity) {
        return switch (entity.getVariant()) {
            case 1 -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/mauler/desert_mauler.png");
            case 2 -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/mauler/savanna_mauler.png");
            default -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/mauler/badlands_mauler.png");
        };
    }

    @Override
    public ResourceLocation getAnimationResource(MaulerEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/mauler/mauler.animation.json");
    }
}
