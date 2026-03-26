package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.MoleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MoleModel extends GeoModel<MoleEntity> {
    @Override
    public ResourceLocation getModelResource(MoleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/mole/mole.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MoleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/mole/mole.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MoleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/mole/mole.animation.json");
    }
}
