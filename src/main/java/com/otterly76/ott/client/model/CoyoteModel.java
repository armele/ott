package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.CoyoteEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CoyoteModel extends GeoModel<CoyoteEntity> {
    @Override
    public ResourceLocation getModelResource(CoyoteEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/coyote/coyote.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CoyoteEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/coyote/coyote.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CoyoteEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/coyote/coyote.animation.json");
    }
}