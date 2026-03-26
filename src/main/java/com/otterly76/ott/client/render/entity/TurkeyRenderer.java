package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.TurkeyModel;
import com.otterly76.ott.entity.custom.TurkeyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TurkeyRenderer extends GeoEntityRenderer<TurkeyEntity> {
    public TurkeyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TurkeyModel());
    }
}