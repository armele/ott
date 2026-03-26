package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.ChupacabraEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ChupacabraModel extends GeoModel<ChupacabraEntity> {
    @Override
    public ResourceLocation getModelResource(ChupacabraEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/chupacabra/chupacabra.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ChupacabraEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/chupacabra/chupacabra.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ChupacabraEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/chupacabra/chupacabra.animation.json");
    }
}