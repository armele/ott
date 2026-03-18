package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.CowGeoEntity;
import com.otterly76.ott.entity.variant.ClientAsset;
import com.otterly76.ott.entity.variant.CowVariant;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

public class CowGeoModel<T extends Cow & CowGeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Ott.resource("geo/entity/cow/cow.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        VariantDataHolder<CowVariant> holder = VariantDataHolder.getHolder(animatable);
        if (holder != null) {
            Optional<CowVariant> variant = holder.ott$getVariantData();
            if (variant.isPresent()) {
                ClientAsset asset = variant.get().modelAndTexture().asset();
                int index = (Math.abs((int) animatable.getUUID().getLeastSignificantBits()) % asset.count()) + 1;
                // Handle non-contiguous cow textures (5 and 7 are missing, up to 70)
                if (asset.id().getPath().equals("entity/cow/cow")) {
                    if (index >= 5) index++;
                    if (index >= 7) index++;
                }
                final int finalIndex = index;
                return asset.id().withPath((path) -> "textures/" + path + "_" + finalIndex + ".png");
            }
        }
        return Ott.resource("textures/entity/cow/cow_1.png");
    }

    @Override
    @Nullable
    public ResourceLocation getAnimationResource(T animatable) {
        return null;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone head = this.getAnimationProcessor().getBone("head");
        GeoBone leg1 = this.getAnimationProcessor().getBone("leg1");
        GeoBone leg2 = this.getAnimationProcessor().getBone("leg2");
        GeoBone leg3 = this.getAnimationProcessor().getBone("leg3");
        GeoBone leg4 = this.getAnimationProcessor().getBone("leg4");

        GeoModelUtils.applyHeadRotation(animationState, head);
        GeoModelUtils.applyLimbSwing4Legs(animationState, leg1, leg2, leg3, leg4);
    }
}




