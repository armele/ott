package com.otterly76.ott.client.model;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.item.custom.FireflyJarItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import static com.otterly76.ott.Constants.MOD_ID;

public class FireflyJarItemModel extends GeoModel<FireflyJarItem> {
    @Override
    public ResourceLocation getModelResource(FireflyJarItem animatable) {
        return getModelResource(animatable, null);
    }

    public ResourceLocation getModelResource(FireflyJarItem animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<FireflyJarItem> renderer) {
        if (animatable == ModItems.FIREFLY_IN_A_JAR.get()) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/block/jar/firefly_in_a_jar.geo.json");
        } else if (animatable == ModItems.FIREFLIES_IN_A_JAR.get()) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/block/jar/fireflies_in_a_jar.geo.json");
        } else {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "geo/block/jar/firefly_jar.geo.json");
        }
    }

    @Override
    public ResourceLocation getTextureResource(FireflyJarItem animatable) {
        return getTextureResource(animatable, null);
    }

    public ResourceLocation getTextureResource(FireflyJarItem animatable, @org.jetbrains.annotations.Nullable software.bernie.geckolib.renderer.GeoRenderer<FireflyJarItem> renderer) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/block/jar/firefly_jar.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FireflyJarItem animatable) {
        if (animatable == ModItems.FIREFLY_IN_A_JAR.get()) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "animations/block/jar/firefly_in_a_jar.animation.json");
        } else if (animatable == ModItems.FIREFLIES_IN_A_JAR.get()) {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "animations/block/jar/fireflies_in_a_jar.animation.json");
        } else {
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, "animations/block/jar/firefly_jar.animation.json");
        }
    }

    @Override
    public void setCustomAnimations(FireflyJarItem animatable, long instanceId, AnimationState<FireflyJarItem> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState == null) return;

        // Matches the scale set in FireflyJarModel for the block
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