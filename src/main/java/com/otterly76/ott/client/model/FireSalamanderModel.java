package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.FireSalamanderEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FireSalamanderModel extends GeoModel<FireSalamanderEntity> {
    @Override
    public ResourceLocation getModelResource(FireSalamanderEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/fire_salamander/fire_salamander.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FireSalamanderEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/fire_salamander/fire_salamander.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FireSalamanderEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/fire_salamander/fire_salamander.animation.json");
    }
}
