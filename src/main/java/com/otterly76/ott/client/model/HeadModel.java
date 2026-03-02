package com.otterly76.ott.client.model;

import com.otterly76.ott.util.block.ModSkullType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.world.level.block.SkullBlock;

import static com.otterly76.ott.Constants.MOD_ID;

public class HeadModel extends GeoModel<HeadAnimatable> {
    @Override
    public ResourceLocation getModelResource(HeadAnimatable animatable) {
        SkullBlock.Type type = animatable.getHeadType();
        String path;
        if (type == ModSkullType.DRAGON_SKULL) {
            path = "dragon/head_dragon";
        } else {
            path = switch ((SkullBlock.Types) type) {
                case SKELETON -> "skeleton/head_skeleton";
                case WITHER_SKELETON -> "wither_skeleton/head_wither_skeleton";
                case ZOMBIE -> "zombie/head_zombie";
                default -> "dragon/head_dragon";
            };
        }
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/entity/" + path + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HeadAnimatable animatable) {
        SkullBlock.Type type = animatable.getHeadType();
        if (type == ModSkullType.DRAGON_SKULL) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/entity/dragon/dragon_skull.png");
        }

        String path = switch ((SkullBlock.Types) type) {
            case SKELETON -> "skeleton/skeleton_head";
            case WITHER_SKELETON -> "skeleton/wither_skeleton_head";
            case ZOMBIE -> "zombie/zombie_head";
            default -> "dragon/dragon_head";
        };
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/entity/" + path + ".png");
    }

    @Override
    public @Nullable ResourceLocation getAnimationResource(HeadAnimatable animatable) {
        return null;
    }
}
