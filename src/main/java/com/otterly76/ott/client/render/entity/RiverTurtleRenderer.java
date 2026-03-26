package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.RiverTurtleModel;
import com.otterly76.ott.entity.custom.RiverTurtleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RiverTurtleRenderer extends GeoEntityRenderer<RiverTurtleEntity> {
    public RiverTurtleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RiverTurtleModel());
    }
}
