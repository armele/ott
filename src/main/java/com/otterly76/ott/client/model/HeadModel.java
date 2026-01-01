package com.otterly76.ott.client.model;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.world.level.block.SkullBlock;

public class HeadModel extends GeoModel<HeadAnimatable> {
    @Override
    public ResourceLocation getModelResource(HeadAnimatable animatable) {
        String path = switch ((SkullBlock.Types)animatable.getHeadType()) {
            case SKELETON -> "skeleton/head_skeleton";
            case WITHER_SKELETON -> "wither_skeleton/head_wither_skeleton";
            case ZOMBIE -> "zombie/head_zombie";
            default -> "dragon/head_dragon";
        };
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/" + path + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HeadAnimatable animatable) {
        String path = switch ((SkullBlock.Types)animatable.getHeadType()) {
            case SKELETON -> "skeleton/skeleton";
            case WITHER_SKELETON -> "wither_skeleton/wither_skeleton";
            case ZOMBIE -> "zombie/zombie_head";
            default -> "dragon/dragon_head";
        };
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/" + path + ".png");
    }

    @Override
    public @Nullable ResourceLocation getAnimationResource(HeadAnimatable animatable) {
        return null;
    }
}