package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.render.layers.LivingEntityEmissiveLayer;
import com.otterly76.ott.client.model.CreakingModel;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.entity.Creaking;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CreakingRenderer<T extends Creaking> extends MobRenderer<T, CreakingModel<T>> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/creaking/creaking.png");
    private static final ResourceLocation EYES_TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/creaking/creaking_eyes.png");

    public CreakingRenderer(EntityRendererProvider.Context context) {
        super(context, new CreakingModel<>(context.bakeLayer(ModModelLayers.CREAKING)), 0.7F);
        this.addLayer(new LivingEntityEmissiveLayer<>(this, (creaking) -> EYES_TEXTURE_LOCATION, (creaking, ageInTicks) -> creaking.shouldEyesGlow() ? 1.0F : 0.0F, new CreakingModel<>(context.bakeLayer(ModModelLayers.CREAKING)), RenderType::entityTranslucentEmissive, true));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Creaking entity) {
        return TEXTURE_LOCATION;
    }
}
