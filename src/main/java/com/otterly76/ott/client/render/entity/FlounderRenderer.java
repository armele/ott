package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.FlounderModel;
import com.otterly76.ott.entity.custom.Flounder;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FlounderRenderer extends GeoEntityRenderer<Flounder> {
    public FlounderRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new FlounderModel());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Flounder animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/flounder/flounder.png");
    }
}
