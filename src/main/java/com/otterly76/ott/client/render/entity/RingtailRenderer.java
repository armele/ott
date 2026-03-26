package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.RingtailModel;
import com.otterly76.ott.entity.custom.RingtailEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RingtailRenderer extends GeoEntityRenderer<RingtailEntity> {
    public RingtailRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RingtailModel());
    }
}