package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.HoopoeModel;
import com.otterly76.ott.entity.custom.Hoopoe;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HoopoeRenderer extends GeoEntityRenderer<Hoopoe> {
    public HoopoeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HoopoeModel());
    }

    @Override
    protected float getShadowRadius(@NotNull Hoopoe entity) {
        return 0.3F;
    }
}
