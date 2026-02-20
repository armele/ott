package com.otterly76.ott.mixin.client;

import com.otterly76.ott.client.render.entity.RenderConditions;
import com.otterly76.ott.client.render.entity.SpecialMobRenderer;
import com.otterly76.ott.client.render.layers.SheepWoolUndercoatLayer;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepRenderer.class)
public abstract class SheepRendererMixin extends MobRendererMixin<Sheep, SheepModel<Sheep>> {
    public SheepRendererMixin(EntityRendererProvider.Context context, SheepModel<Sheep> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(
        method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V",
        at = @At("TAIL")
    )
    private void onInit(EntityRendererProvider.Context context, CallbackInfo ci) {
        SpecialMobRenderer.create(context, (ctx) -> new SheepWoolUndercoatLayer(this, ctx.getModelSet()), RenderConditions.SHEEP_UNDERCOAT).ifPresent((layer) -> this.addLayer(layer.get()));
    }
}