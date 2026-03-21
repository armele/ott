package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

public abstract class GeoLivingRendererWrapper<T extends Mob & GeoEntity> extends MobRenderer<T, EntityModel<T>> implements GeoRenderer<T> {
    protected final GeoEntityRenderer<T> geoRenderer;

    protected GeoLivingRendererWrapper(EntityRendererProvider.Context context, GeoEntityRenderer<T> geoRenderer) {
        super(context, new DummyModel<>(), 0.5f);
        this.geoRenderer = geoRenderer;
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void render(@NotNull T entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        this.geoRenderer.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return this.geoRenderer.getTextureLocation(entity);
    }

    public GeoEntityRenderer<T> getGeoRenderer() {
        return this.geoRenderer;
    }

    @Override
    public GeoModel<T> getGeoModel() {
        return this.geoRenderer.getGeoModel();
    }

    @Override
    public T getAnimatable() {
        return this.geoRenderer.getAnimatable();
    }

    @Override
    public long getInstanceId(T animatable) {
        return this.geoRenderer.getInstanceId(animatable);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        this.geoRenderer.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void fireCompileRenderLayersEvent() {
        this.geoRenderer.fireCompileRenderLayersEvent();
    }

    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return this.geoRenderer.firePreRenderEvent(poseStack, model, bufferSource, partialTick, packedLight);
    }

    @Override
    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        this.geoRenderer.firePostRenderEvent(poseStack, model, bufferSource, partialTick, packedLight);
    }

    @Override
    public void updateAnimatedTextureFrame(T animatable) {
        this.geoRenderer.updateAnimatedTextureFrame(animatable);
    }

    private static class DummyModel<T extends Mob> extends EntityModel<T> {
        @Override
        public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

        @Override
        public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay, int color) {}
    }
}
