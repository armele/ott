package com.otterly76.ott.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.client.render.entity.SheepGeoRenderer;
import com.otterly76.ott.entity.gecko.SheepGeoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.animal.Sheep;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.Locale;

public class SheepGeoWoolLayer<T extends Sheep & SheepGeoEntity> extends GeoRenderLayer<T> {
    public SheepGeoWoolLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.isSheared())
            return;

        if (!(getRenderer() instanceof SheepGeoRenderer<T> sheepRenderer))
            return;

        int diffuseColor = animatable.getColor().getTextureDiffuseColor() | 0xFF000000;
        RenderType woolRenderType = getRenderer().getRenderType(animatable, getRenderer().getTextureLocation(animatable), bufferSource, partialTick);
        if (woolRenderType == null)
            return;
        VertexConsumer woolBuffer = bufferSource.getBuffer(woolRenderType);

        sheepRenderer.setWoolPass(true);
        // Temporarily unhide wool bones for this render pass
        for (GeoBone bone : sheepRenderer.getGeoModel().getAnimationProcessor().getRegisteredBones()) {
            String name = bone.getName();
            if (name != null && name.toLowerCase(Locale.ROOT).startsWith("wool")) {
                bone.setHidden(false);
            }
        }

        // Use GeckoLib's actuallyRender which handles transformations and recursion correctly
        sheepRenderer.actuallyRender(poseStack, animatable, model, woolRenderType, bufferSource, woolBuffer, true, partialTick, packedLight, packedOverlay, diffuseColor);

        // Hide wool bones again so they don't appear in the main skin pass of the next entity
        for (GeoBone bone : sheepRenderer.getGeoModel().getAnimationProcessor().getRegisteredBones()) {
            String name = bone.getName();
            if (name != null && name.toLowerCase(Locale.ROOT).startsWith("wool")) {
                bone.setHidden(true);
            }
        }
        sheepRenderer.setWoolPass(false);
    }
}