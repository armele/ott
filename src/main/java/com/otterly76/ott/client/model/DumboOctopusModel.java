package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.DumboOctopusEntity;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class DumboOctopusModel extends GeoModel<DumboOctopusEntity> {
    @Override
    public ResourceLocation getModelResource(DumboOctopusEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/dumbo_octopus/dumbo_octopus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DumboOctopusEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/dumbo_octopus/dumbo_octopus_" + entity.getVariant() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(DumboOctopusEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/dumbo_octopus/dumbo_octopus.animation.json");
    }
}