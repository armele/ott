package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.GoldenHermitKingModel;
import com.otterly76.ott.entity.custom.GoldenHermitKing;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GoldenHermitKingRenderer extends GeoEntityRenderer<GoldenHermitKing> {
    public GoldenHermitKingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GoldenHermitKingModel());
    }
}