package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.JumpingSpiderEntity;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@OnlyIn(Dist.CLIENT)
public class JumpingSpiderModel extends GeoModel<JumpingSpiderEntity> {
    @Override
    public ResourceLocation getModelResource(JumpingSpiderEntity entity) {
        return getModelResource(entity, null);
    }

    @Override
    public ResourceLocation getModelResource(JumpingSpiderEntity entity, @Nullable GeoRenderer<JumpingSpiderEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/jumping_spider/jumping_spider.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(JumpingSpiderEntity entity) {
        return getTextureResource(entity, null);
    }

    @Override
    public ResourceLocation getTextureResource(JumpingSpiderEntity entity, @Nullable GeoRenderer<JumpingSpiderEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/jumping_spider/jumping_spider.png");
    }

    @Override
    public ResourceLocation getAnimationResource(JumpingSpiderEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/jumping_spider/jumping_spider.animation.json");
    }
}