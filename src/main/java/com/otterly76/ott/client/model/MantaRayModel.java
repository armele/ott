package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.MantaRayEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MantaRayModel extends GeoModel<MantaRayEntity> {
    @Override
    public ResourceLocation getModelResource(MantaRayEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/manta_ray/manta_ray.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MantaRayEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/manta_ray/manta_ray.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MantaRayEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/manta_ray/manta_ray.animation.json");
    }
}
