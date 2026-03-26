package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.BonnetheadSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BonnetheadSharkModel extends GeoModel<BonnetheadSharkEntity> {
    @Override
    public ResourceLocation getModelResource(BonnetheadSharkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/bonnethead_shark/bonnethead_shark.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BonnetheadSharkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/bonnethead_shark/bonnethead_shark.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BonnetheadSharkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/bonnethead_shark/bonnethead_shark.animation.json");
    }
}
