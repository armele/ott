package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.PinkLandIguanaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PinkLandIguanaModel extends GeoModel<PinkLandIguanaEntity> {
    @Override
    public ResourceLocation getModelResource(PinkLandIguanaEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/pink_land_iguana/pink_land_iguana.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PinkLandIguanaEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/pink_land_iguana/pink_land_iguana.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PinkLandIguanaEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/pink_land_iguana/pink_land_iguana.animation.json");
    }
}
