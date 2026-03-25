package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SandHermit;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SandHermitModel extends GeoModel<SandHermit> {
    @Override
    public ResourceLocation getModelResource(SandHermit animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/sand_hermit/sand_hermit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SandHermit animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/sand_hermit/sand_hermit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SandHermit animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/sand_hermit/sand_hermit.animation.json");
    }
}