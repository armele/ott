package com.otterly76.ott.mixin.client;

import com.otterly76.ott.client.neat.HealthBarRenderer;
import com.otterly76.ott.client.render.entity.LeashFeatureRenderer;
import com.otterly76.ott.config.OttConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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

    @Inject(
        method = {"render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"},
        at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;renderNameTag(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IF)V")},
        cancellable = true
    )
    private void ott$suppressNameTag(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        OttConfig.Neat.NameTagRenderBehavior behavior = OttConfig.NEAT.NAME_TAG_RENDER_BEHAVIOR.get();
        if (behavior == OttConfig.Neat.NameTagRenderBehavior.NEVER
                || (behavior == OttConfig.Neat.NameTagRenderBehavior.WHEN_NO_HEALTHBAR && ott$entityHasHealthbar(entity))) {
            ci.cancel();
        }
    }

    @Unique
    private boolean ott$entityHasHealthbar(Entity entity) {
        if (!(entity instanceof LivingEntity)) return false;
        if (entity instanceof Player && !OttConfig.NEAT.SHOW_ON_PLAYERS.get()) return false;
        if (HealthBarRenderer.isBoss(entity) && !OttConfig.NEAT.SHOW_ON_BOSSES.get()) return false;
        if (entity.getType().getCategory().isFriendly() && !OttConfig.NEAT.SHOW_ON_PASSIVE.get()) return false;
        if (!entity.getType().getCategory().isFriendly() && !HealthBarRenderer.isBoss(entity) && !OttConfig.NEAT.SHOW_ON_HOSTILE.get()) return false;
        var id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        @SuppressWarnings("unchecked")
        java.util.List<String> blacklist = (java.util.List<String>) OttConfig.NEAT.BLACKLIST.get();
        return !blacklist.contains(id.toString()) && OttConfig.NEAT_DRAW;
    }
}
