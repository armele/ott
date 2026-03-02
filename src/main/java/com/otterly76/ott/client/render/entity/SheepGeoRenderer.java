package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.model.SheepGeoModel;
import com.otterly76.ott.client.render.layers.SheepGeoWoolLayer;
import com.otterly76.ott.entity.gecko.SheepGeoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.animal.Sheep;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class SheepGeoRenderer<T extends Sheep & SheepGeoEntity> extends GeoEntityRenderer<T> {
    private boolean isWoolPass = false;

    public SheepGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SheepGeoModel<>());
        this.addRenderLayer(new SheepGeoWoolLayer<>(this));
    }

    public void setWoolPass(boolean woolPass) {
        this.isWoolPass = woolPass;
    }

    @Override
    public void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
        if (isWoolPass) {
            String name = bone.getName();
            if (name == null || !name.toLowerCase(Locale.ROOT).startsWith("wool")) {
                return;
            }
        }
        super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, colour);
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if (!isReRender && animatable.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }
}