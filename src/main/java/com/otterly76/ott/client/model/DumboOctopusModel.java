package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.DumboOctopusEntity;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@OnlyIn(Dist.CLIENT)
public class DumboOctopusModel extends GeoModel<DumboOctopusEntity> {
    @Override
    public ResourceLocation getModelResource(DumboOctopusEntity entity) {
        return getModelResource(entity, null);
    }

    @Override
    public ResourceLocation getModelResource(DumboOctopusEntity entity, @Nullable GeoRenderer<DumboOctopusEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/dumbo_octopus/dumbo_octopus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DumboOctopusEntity entity) {
        return getTextureResource(entity, null);
    }

    @Override
    public ResourceLocation getTextureResource(DumboOctopusEntity entity, @Nullable GeoRenderer<DumboOctopusEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/dumbo_octopus/dumbo_octopus_" + entity.getVariant() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(DumboOctopusEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/dumbo_octopus/dumbo_octopus.animation.json");
    }
}