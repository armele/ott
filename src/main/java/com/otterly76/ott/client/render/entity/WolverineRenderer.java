package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.WolverineModel;
import com.otterly76.ott.entity.custom.WolverineEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WolverineRenderer extends GeoEntityRenderer<WolverineEntity> {
    public WolverineRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WolverineModel());
    }
}