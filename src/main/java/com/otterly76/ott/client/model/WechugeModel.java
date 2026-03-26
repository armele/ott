package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.WechugeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WechugeModel extends GeoModel<WechugeEntity> {
    @Override
    public ResourceLocation getModelResource(WechugeEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/wechuge/wechuge.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WechugeEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/wechuge/wechuge.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WechugeEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/wechuge/wechuge.animation.json");
    }
}