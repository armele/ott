package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.GlareEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GlareModel extends GeoModel<GlareEntity> {
    @Override
    public ResourceLocation getModelResource(GlareEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/glare/glare.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GlareEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/glare/glare.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GlareEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/glare/glare.animation.json");
    }
}
