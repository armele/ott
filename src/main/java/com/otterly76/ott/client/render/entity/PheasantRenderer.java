package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.PheasantModel;
import com.otterly76.ott.entity.custom.Pheasant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PheasantRenderer extends GeoEntityRenderer<Pheasant> {
    public PheasantRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PheasantModel());
    }

    @Override
    protected float getShadowRadius(@NotNull Pheasant entity) {
        return 0.4F;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Pheasant animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/pheasant/pheasant.png");
    }
}