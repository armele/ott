package com.otterly76.ott.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.entity.TorchArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TorchArrowRenderer extends ArrowRenderer<TorchArrowEntity> {
    public static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/projectiles/tipped_arrow.png");

    public TorchArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TorchArrowEntity entity) {
        return TEXTURE;
    }

    @Override
    public void vertex(PoseStack.@NotNull Pose p_324380_, VertexConsumer p_253902_, int p_254058_, int p_254338_, int p_254196_, float p_254003_, float p_254165_, int p_253982_, int p_254037_, int p_254038_, int p_254271_) {
        p_253902_.addVertex(p_324380_, (float)p_254058_, (float)p_254338_, (float)p_254196_)
                .setColor(0xFFFFC400)
                .setUv(p_254003_, p_254165_)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(p_254271_)
                .setNormal(p_324380_, (float)p_253982_, (float)p_254038_, (float)p_254037_);
    }
}