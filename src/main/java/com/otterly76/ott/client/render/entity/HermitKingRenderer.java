package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.HermitKingModel;
import com.otterly76.ott.entity.custom.HermitKing;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HermitKingRenderer extends GeoEntityRenderer<HermitKing> {
    public HermitKingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HermitKingModel());
    }
}