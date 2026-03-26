package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.CandycaneSnailModel;
import com.otterly76.ott.entity.custom.CandycaneSnailEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CandycaneSnailRenderer extends GeoEntityRenderer<CandycaneSnailEntity> {
    public CandycaneSnailRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CandycaneSnailModel());
    }
}
