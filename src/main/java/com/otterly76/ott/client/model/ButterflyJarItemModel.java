package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Butterfly;
import com.otterly76.ott.item.custom.ButterflyJarItem;
import com.otterly76.ott.client.renderer.item.ButterflyJarItemRenderer;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import org.jetbrains.annotations.Nullable;

public class ButterflyJarItemModel extends GeoModel<ButterflyJarItem> {
    @Override
    public ResourceLocation getModelResource(ButterflyJarItem animatable, @Nullable GeoRenderer<ButterflyJarItem> renderer) {
        if (renderer instanceof ButterflyJarItemRenderer r) {
            return getModelResource(r.getCurrentVariant());
        }
        return getModelResource(Butterfly.Variant.MONARCH);
    }

    public ResourceLocation getModelResource(Butterfly.Variant variant) {
        return Constants.loc("geo/block/butterfly_jar/" + variant.getName() + "jar.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(ButterflyJarItem animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(ButterflyJarItem animatable, @Nullable GeoRenderer<ButterflyJarItem> renderer) {
        if (renderer instanceof ButterflyJarItemRenderer r) {
            return getTextureResource(r.getCurrentVariant());
        }
        return getTextureResource(Butterfly.Variant.MONARCH);
    }

    public ResourceLocation getTextureResource(Butterfly.Variant variant) {
        return Constants.loc("textures/block/butterfly_jar/" + variant.getJarTextureName() + ".png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(ButterflyJarItem animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getAnimationResource(ButterflyJarItem animatable) {
        return Constants.loc("animations/block/butterfly_jar/monarchjar.animation.json"); // Default
    }

    public ResourceLocation getAnimationResource(Butterfly.Variant variant) {
        return Constants.loc("animations/block/butterfly_jar/" + variant.getName() + "jar.animation.json");
    }
}