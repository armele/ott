package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.StingrayModel;
import com.otterly76.ott.entity.custom.Stingray;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StingrayRenderer extends GeoEntityRenderer<Stingray> {
    private static final ResourceLocation TEXTURE_GRAY = ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/stingray/stingray_0.png");
    private static final ResourceLocation TEXTURE_MUDDY = ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/stingray/stingray_1.png");
    private static final ResourceLocation TEXTURE_BLUE_SPOTTED = ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/stingray/stingray_2.png");

    public StingrayRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new StingrayModel());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(Stingray animatable) {
        return switch (animatable.getVariant()) {
            case 1 -> TEXTURE_MUDDY;
            case 2 -> TEXTURE_BLUE_SPOTTED;
            default -> TEXTURE_GRAY;
        };
    }
}