package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.OtterEntity;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@OnlyIn(Dist.CLIENT)
public class OtterModel extends GeoModel<OtterEntity> {
    @Override
    public ResourceLocation getModelResource(OtterEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getModelResource(OtterEntity entity, @Nullable GeoRenderer<OtterEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, entity.isBaby() ? "geo/entity/otter/baby_otter.geo.json" : "geo/entity/otter/otter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OtterEntity animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(OtterEntity entity, @Nullable GeoRenderer<OtterEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, entity.isBaby() ? "textures/entity/otter/baby_otter.png" : "textures/entity/otter/otter.png");
    }

    @Override
    public ResourceLocation getAnimationResource(OtterEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, entity.isBaby() ? "animations/entity/otter/baby_otter.animation.json" : "animations/entity/otter/otter.animation.json");
    }
}