package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.BoggedBoneStalker;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BoggedBoneStalkerModel extends GeoModel<BoggedBoneStalker> {
    @Override
    public ResourceLocation getModelResource(BoggedBoneStalker animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/bogged_bone_stalker/bogged_bone_stalker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BoggedBoneStalker animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/bogged_bone_stalker/bogged_bone_stalker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BoggedBoneStalker animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/bogged_bone_stalker/bogged_bone_stalker.animation.json");
    }
}