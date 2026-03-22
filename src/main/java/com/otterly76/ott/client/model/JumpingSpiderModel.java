package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.JumpingSpiderEntity;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class JumpingSpiderModel extends GeoModel<JumpingSpiderEntity> {
    @Override
    public ResourceLocation getModelResource(JumpingSpiderEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/jumping_spider/jumping_spider.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(JumpingSpiderEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/jumping_spider/jumping_spider.png");
    }

    @Override
    public ResourceLocation getAnimationResource(JumpingSpiderEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/jumping_spider/jumping_spider.animation.json");
    }
}