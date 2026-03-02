package com.otterly76.ott.mixin.client;

import com.otterly76.ott.client.render.entity.LeashFeatureRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityRenderer.class})
public class EntityRendererMixin<T extends Entity> {
    @Unique
    private LeashFeatureRenderer<T> ott$leashRenderer;
    @Shadow
    @Final
    protected EntityRenderDispatcher entityRenderDispatcher;

    @Inject(
        method = {"<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V"},
        at = {@At("TAIL")}
    )
    private void vb$init(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.ott$leashRenderer = new LeashFeatureRenderer<>(this.entityRenderDispatcher);
    }

    @Inject(
        method = {"render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"},
        at = {@At("HEAD")}
    )
    private void renderAdditional(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        this.ott$leashRenderer.render(entity, partialTick, poseStack, buffer);
    }

    @Inject(
        method = {"shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z"},
        at = {@At("TAIL")},
        cancellable = true
    )
    private void vb$shouldRender(T entity, Frustum camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.ott$leashRenderer.shouldRender(entity, camera, cir.getReturnValue()));
    }
}
