package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.CopperGolemModel;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.entity.custom.CopperGolem;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveLayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import com.otterly76.ott.client.render.layers.CopperGolemAntennaLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CopperGolemRenderer extends MobRenderer<CopperGolem, CopperGolemModel> {
    private static final ResourceLocation UNAFFECTED_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/copper_golem/copper_golem.png");
    private static final ResourceLocation EXPOSED_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/copper_golem/exposed_copper_golem.png");
    private static final ResourceLocation WEATHERED_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/copper_golem/weathered_copper_golem.png");
    private static final ResourceLocation OXIDIZED_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/copper_golem/oxidized_copper_golem.png");

    private static final ResourceLocation UNAFFECTED_EYES_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/copper_golem/copper_golem_eyes.png");
    private static final ResourceLocation EXPOSED_EYES_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/copper_golem/exposed_copper_golem_eyes.png");
    private static final ResourceLocation WEATHERED_EYES_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/copper_golem/weathered_copper_golem_eyes.png");
    private static final ResourceLocation OXIDIZED_EYES_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/copper_golem/oxidized_copper_golem_eyes.png");

    public CopperGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new CopperGolemModel(context.bakeLayer(ModModelLayers.COPPER_GOLEM)), 0.5F);
        this.addLayer(new LivingEntityEmissiveLayer<>(this, this::getEyesTextureLocation, (entity, ageInTicks) -> 1.0F, new CopperGolemModel(context.bakeLayer(ModModelLayers.COPPER_GOLEM)), RenderType::eyes, true));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new CopperGolemAntennaLayer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CopperGolem entity) {
        return switch (entity.getWeatherState()) {
            case UNAFFECTED -> UNAFFECTED_TEXTURE;
            case EXPOSED -> EXPOSED_TEXTURE;
            case WEATHERED -> WEATHERED_TEXTURE;
            case OXIDIZED -> OXIDIZED_TEXTURE;
        };
    }

    private ResourceLocation getEyesTextureLocation(CopperGolem entity) {
        return switch (entity.getWeatherState()) {
            case UNAFFECTED -> UNAFFECTED_EYES_TEXTURE;
            case EXPOSED -> EXPOSED_EYES_TEXTURE;
            case WEATHERED -> WEATHERED_EYES_TEXTURE;
            case OXIDIZED -> OXIDIZED_EYES_TEXTURE;
        };
    }
}