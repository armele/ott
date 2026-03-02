package com.otterly76.ott.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.client.render.entity.CowVariantRenderer;
import com.otterly76.ott.client.render.entity.RenderConditions;
import com.otterly76.ott.client.render.entity.SpecialMobRenderer;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin({CowRenderer.class})
public abstract class CowRendererMixin extends MobRendererMixin<Cow, CowModel<Cow>> {
    @Unique
    private Supplier<CowVariantRenderer> ott$renderer;

    public CowRendererMixin(EntityRendererProvider.Context context, CowModel<Cow> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(
        method = {"<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V"},
        at = {@At("TAIL")}
    )
    private void ott$onInit(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.ott$renderer = SpecialMobRenderer.create(context, CowVariantRenderer::new, RenderConditions.FARM_ANIMALS).orElse(null);
    }

    @Inject(
        method = {"getTextureLocation(Lnet/minecraft/world/entity/animal/Cow;)Lnet/minecraft/resources/ResourceLocation;"},
        at = {@At("HEAD")},
        cancellable = true
    )
    private void ott$getTextureLocation(Cow entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (this.ott$renderer != null) {
            this.ott$renderer.get().getTexture(entity).ifPresent(cir::setReturnValue);
        }
    }

    @Override
    public void render(@NotNull Cow entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        if (this.ott$renderer != null) {
            this.model = this.ott$renderer.get().getModel(entity).orElse(this.defaultModel);
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
