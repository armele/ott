package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.MooshroomGeoEntity;
import com.otterly76.ott.entity.variant.ClientAsset;
import com.otterly76.ott.entity.variant.MooshroomVariant;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

public class MooshroomGeoModel<T extends MushroomCow & MooshroomGeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Ott.resource("geo/entity/mooshroom/mooshroom.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        VariantDataHolder<MooshroomVariant> holder = VariantDataHolder.getHolder(animatable);
        if (holder != null) {
            Optional<MooshroomVariant> variant = holder.ott$getVariantData();
            if (variant.isPresent()) {
                ClientAsset asset = variant.get().modelAndTexture().asset();
                final int index = (Math.abs((int) animatable.getUUID().getLeastSignificantBits()) % asset.count()) + 1;
                return asset.id().withPath((path) -> "textures/" + path + "_" + index + ".png");
            }
        }
        return Ott.resource("textures/entity/mooshroom/red_mooshroom_1.png");
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




