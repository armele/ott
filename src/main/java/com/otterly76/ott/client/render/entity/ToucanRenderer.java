package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.ToucanModel;
import com.otterly76.ott.entity.custom.Toucan;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ToucanRenderer extends GeoEntityRenderer<Toucan> {
    public ToucanRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ToucanModel());
    }

    @Override
    protected float getShadowRadius(@NotNull Toucan entity) {
        return 0.3F;
    }
}
