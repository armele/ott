package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SeaBunnyEntity;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class SeaBunnyModel extends GeoModel<SeaBunnyEntity> {
    @Override
    public ResourceLocation getModelResource(SeaBunnyEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/sea_bunny/sea_bunny.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SeaBunnyEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/sea_bunny/sea_bunny_" + entity.getVariant() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(SeaBunnyEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/sea_bunny/sea_bunny.animation.json");
    }
}