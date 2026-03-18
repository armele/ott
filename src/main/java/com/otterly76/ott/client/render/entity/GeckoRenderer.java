package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.GeckoModel;
import com.otterly76.ott.entity.custom.Gecko;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GeckoRenderer extends GeoEntityRenderer<Gecko> {
    public GeckoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeckoModel());
    }

    @Override
    protected float getShadowRadius(@NotNull Gecko entity) {
        return 0.3F;
    }
}