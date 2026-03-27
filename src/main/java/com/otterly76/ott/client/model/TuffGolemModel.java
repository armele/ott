package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.TuffGolemEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TuffGolemModel extends GeoModel<TuffGolemEntity> {
    @Override
    public ResourceLocation getModelResource(TuffGolemEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/tuff_golem/tuff_golem.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TuffGolemEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/tuff_golem/tuff_golem.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TuffGolemEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/tuff_golem/tuff_golem.animation.json");
    }
}
