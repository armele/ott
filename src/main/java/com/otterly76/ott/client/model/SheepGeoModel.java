package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.SheepGeoEntity;
import com.otterly76.ott.entity.variant.ClientAsset;
import com.otterly76.ott.entity.variant.SheepVariant;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Sheep;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.Locale;
import java.util.Optional;

public class SheepGeoModel<T extends Sheep & SheepGeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Ott.resource("geo/entity/sheep/sheep.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        VariantDataHolder<SheepVariant> holder = VariantDataHolder.getHolder(animatable);
        if (holder != null) {
            Optional<SheepVariant> variant = holder.ott$getVariantData();
            if (variant.isPresent()) {
                ClientAsset asset = variant.get().modelAndTexture().asset();
                if (asset.count() > 1) {
                    int index = (Math.abs((int) animatable.getUUID().getLeastSignificantBits()) % asset.count()) + 1;
                    return asset.id().withPath((path) -> "textures/" + path + "_" + index + ".png");
                }
                return asset.path();
            }
        }
        // Default temperate texture
        return Ott.resource("textures/entity/sheep/sheep_1.png");
    }

    @Override
    @Nullable
    public ResourceLocation getAnimationResource(T animatable) {
        return null;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        // Toggle visibility for all wool_* bones based on sheared state
        // We always hide wool bones in the main model pass; the SheepGeoWoolLayer handles their colored rendering
        boolean sheared = animatable.isSheared();
        for (GeoBone bone : this.getAnimationProcessor().getRegisteredBones()) {
            String name = bone.getName();
            if (name != null && name.toLowerCase(Locale.ROOT).startsWith("wool")) {
                bone.setHidden(true);
            }
        }

        GeoBone head = this.getAnimationProcessor().getBone("head");
        GeoBone leg1 = this.getAnimationProcessor().getBone("leg1");
        GeoBone leg2 = this.getAnimationProcessor().getBone("leg2");
        GeoBone leg3 = this.getAnimationProcessor().getBone("leg3");
        GeoBone leg4 = this.getAnimationProcessor().getBone("leg4");

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null) {
            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }

        float limbSwing = animationState.getLimbSwing();
        float limbSwingAmount = animationState.getLimbSwingAmount();

        if (leg1 != null) leg1.setRotX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
        if (leg2 != null) leg2.setRotX(Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount);
        if (leg3 != null) leg3.setRotX(Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount);
        if (leg4 != null) leg4.setRotX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);

        float headEatAngleScale = animatable.getHeadEatAngleScale(animationState.getPartialTick());
        if (headEatAngleScale > 0.0F && head != null) {
            head.setRotX(head.getRotX() + headEatAngleScale * 4.449911F);
        }
    }
}