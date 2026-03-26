package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.PitViperModel;
import com.otterly76.ott.entity.custom.PitViperEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PitViperRenderer extends GeoEntityRenderer<PitViperEntity> {
    public PitViperRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PitViperModel());
    }
}