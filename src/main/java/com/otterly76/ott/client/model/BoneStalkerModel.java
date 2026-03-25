package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.BoneStalker;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BoneStalkerModel extends GeoModel<BoneStalker> {
    @Override
    public ResourceLocation getModelResource(BoneStalker animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/bone_stalker/bone_stalker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BoneStalker animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/bone_stalker/bone_stalker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BoneStalker animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/bone_stalker/bone_stalker.animation.json");
    }
}