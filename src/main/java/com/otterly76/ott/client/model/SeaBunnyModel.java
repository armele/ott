package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SeaBunnyEntity;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@OnlyIn(Dist.CLIENT)
public class SeaBunnyModel extends GeoModel<SeaBunnyEntity> {
    @Override
    public ResourceLocation getModelResource(SeaBunnyEntity entity) {
        return getModelResource(entity, null);
    }

    @Override
    public ResourceLocation getModelResource(SeaBunnyEntity entity, @Nullable GeoRenderer<SeaBunnyEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/sea_bunny/sea_bunny.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SeaBunnyEntity entity) {
        return getTextureResource(entity, null);
    }

    @Override
    public ResourceLocation getTextureResource(SeaBunnyEntity entity, @Nullable GeoRenderer<SeaBunnyEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/sea_bunny/sea_bunny_" + entity.getVariant() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(SeaBunnyEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/sea_bunny/sea_bunny.animation.json");
    }
}