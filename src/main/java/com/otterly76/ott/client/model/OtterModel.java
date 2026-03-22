package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.OtterEntity;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class OtterModel extends GeoModel<OtterEntity> {
    @Override
    public ResourceLocation getModelResource(OtterEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, entity.isBaby() ? "geo/entity/otter/baby_otter.geo.json" : "geo/entity/otter/otter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OtterEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, entity.isBaby() ? "textures/entity/otter/baby_otter.png" : "textures/entity/otter/otter.png");
    }

    @Override
    public ResourceLocation getAnimationResource(OtterEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, entity.isBaby() ? "animations/entity/otter/baby_otter.animation.json" : "animations/entity/otter/otter.animation.json");
    }
}