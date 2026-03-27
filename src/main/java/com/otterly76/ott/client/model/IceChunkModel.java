package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.IceologerIceChunkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class IceChunkModel extends GeoModel<IceologerIceChunkEntity> {
    @Override
    public ResourceLocation getModelResource(IceologerIceChunkEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/ice_chunk/ice_chunk.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(IceologerIceChunkEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/iceologer/ice_chunk.png");
    }

    @Override
    public ResourceLocation getAnimationResource(IceologerIceChunkEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/ice_chunk/ice_chunk.animation.json");
    }
}
