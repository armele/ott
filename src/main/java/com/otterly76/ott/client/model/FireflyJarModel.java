package com.otterly76.ott.client.model;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.entity.FireflyJarBlockEntity;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

import static com.otterly76.ott.Constants.MOD_ID;

public class FireflyJarModel extends GeoModel<FireflyJarBlockEntity> {
    @Override
    public ResourceLocation getModelResource(FireflyJarBlockEntity animatable, @Nullable GeoRenderer<FireflyJarBlockEntity> renderer) {
        if (animatable.getBlockState().is(ModBlocks.FIREFLY_IN_A_JAR.get())) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/block/jar/firefly_in_a_jar.geo.json");
        } else if (animatable.getBlockState().is(ModBlocks.FIREFLIES_IN_A_JAR.get())) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/block/jar/fireflies_in_a_jar.geo.json");
        } else {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/block/jar/firefly_jar.geo.json");
        }
    }

    @Override
    public ResourceLocation getModelResource(FireflyJarBlockEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(FireflyJarBlockEntity animatable, @Nullable GeoRenderer<FireflyJarBlockEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/block/jar/firefly_jar.png");
    }

    @Override
    public ResourceLocation getTextureResource(FireflyJarBlockEntity animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getAnimationResource(FireflyJarBlockEntity animatable) {
        if (animatable.getBlockState().is(ModBlocks.FIREFLY_IN_A_JAR.get())) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "animations/block/jar/firefly_in_a_jar.animation.json");
        } else if (animatable.getBlockState().is(ModBlocks.FIREFLIES_IN_A_JAR.get())) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "animations/block/jar/fireflies_in_a_jar.animation.json");
        } else {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "animations/block/jar/firefly_jar.animation.json");
        }
    }

    @Override
    public void setCustomAnimations(FireflyJarBlockEntity animatable, long instanceId, AnimationState<FireflyJarBlockEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;

        // Change this value to adjust the size of the fireflies inside the jars!
        float scale = 0.6F;

        String[] fireflyBones = {"firefly_root", "firefly_root_1", "firefly_root_2", "firefly_root_3"};
        for (String boneName : fireflyBones) {
            GeoBone bone = getAnimationProcessor().getBone(boneName);
            if (bone != null) {
                bone.setScaleX(scale);
                bone.setScaleY(scale);
                bone.setScaleZ(scale);
            }
        }
    }
}
