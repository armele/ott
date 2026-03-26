package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.EchidnaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EchidnaModel extends GeoModel<EchidnaEntity> {
    @Override
    public ResourceLocation getModelResource(EchidnaEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/echidna/echidna.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EchidnaEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/echidna/echidna.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EchidnaEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/echidna/echidna.animation.json");
    }
}
