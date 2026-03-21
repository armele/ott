package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.AngelfishModel;
import com.otterly76.ott.entity.custom.Angelfish;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AngelfishRenderer extends GeoEntityRenderer<Angelfish> {
    public AngelfishRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new AngelfishModel());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Angelfish animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/angelfish/angelfish.png");
    }
}
