package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.DragonflyEntity;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class DragonflyModel extends GeoModel<DragonflyEntity> {
    @Override
    public ResourceLocation getModelResource(DragonflyEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/dragonfly/dragonfly.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DragonflyEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/dragonfly/dragonfly.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DragonflyEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/dragonfly/dragonfly.animation.json");
    }
}