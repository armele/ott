package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.FerretEntity;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@OnlyIn(Dist.CLIENT)
public class FerretModel extends GeoModel<FerretEntity> {
    @Override
    public ResourceLocation getModelResource(FerretEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getModelResource(FerretEntity entity, @Nullable GeoRenderer<FerretEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, entity.isBaby() ? "geo/entity/ferret/baby_ferret.geo.json" : "geo/entity/ferret/ferret.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FerretEntity animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(FerretEntity entity, @Nullable GeoRenderer<FerretEntity> renderer) {
        String prefix = entity.isBaby() ? "baby_ferret_" : "ferret_";
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/ferret/" + prefix + entity.getVariant() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(FerretEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, entity.isBaby() ? "animations/entity/ferret/baby_ferret.animation.json" : "animations/entity/ferret/ferret.animation.json");
    }
}