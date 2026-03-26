package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.GoblinSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GoblinSharkModel extends GeoModel<GoblinSharkEntity> {
    @Override
    public ResourceLocation getModelResource(GoblinSharkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/goblin_shark/goblin_shark.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GoblinSharkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/goblin_shark/goblin_shark.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GoblinSharkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/goblin_shark/goblin_shark.animation.json");
    }
}
