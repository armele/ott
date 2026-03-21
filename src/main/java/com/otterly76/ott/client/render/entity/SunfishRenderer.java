package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.SunfishModel;
import com.otterly76.ott.entity.custom.Sunfish;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SunfishRenderer extends GeoEntityRenderer<Sunfish> {
    private static final ResourceLocation TEXTURE_OCEAN = ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/sunfish/sunfish_0.png");
    private static final ResourceLocation TEXTURE_COLD = ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/sunfish/sunfish_1.png");
    private static final ResourceLocation TEXTURE_GOLDEN = ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/sunfish/sunfish_golden.png");

    public SunfishRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new SunfishModel());
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Sunfish animatable) {
        return switch (animatable.getVariant()) {
            case 1 -> TEXTURE_COLD;
            case 2 -> TEXTURE_GOLDEN;
            default -> TEXTURE_OCEAN;
        };
    }
}
