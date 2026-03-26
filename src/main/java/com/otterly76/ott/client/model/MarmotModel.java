package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.MarmotEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MarmotModel extends GeoModel<MarmotEntity> {
    @Override
    public ResourceLocation getModelResource(MarmotEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/marmot/marmot.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MarmotEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/marmot/marmot.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MarmotEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/marmot/marmot.animation.json");
    }
}