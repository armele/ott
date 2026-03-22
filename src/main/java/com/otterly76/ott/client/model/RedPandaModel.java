package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.RedPandaEntity;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class RedPandaModel extends GeoModel<RedPandaEntity> {
    @Override
    public ResourceLocation getModelResource(RedPandaEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, entity.isBaby() ? "geo/entity/red_panda/baby_red_panda.geo.json" : "geo/entity/red_panda/red_panda.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RedPandaEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, entity.isBaby() ? "textures/entity/red_panda/baby_red_panda.png" : "textures/entity/red_panda/red_panda.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RedPandaEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, entity.isBaby() ? "animations/entity/red_panda/baby_red_panda.animation.json" : "animations/entity/red_panda/red_panda.animation.json");
    }
}