package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.PsychoJellyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PsychoJellyModel extends GeoModel<PsychoJellyEntity> {
    @Override
    public ResourceLocation getModelResource(PsychoJellyEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/psycho_jelly/psycho_jelly.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PsychoJellyEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/psycho_jelly/psycho_jelly.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PsychoJellyEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/psycho_jelly/psycho_jelly.animation.json");
    }
}
