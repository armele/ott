package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.StorkModel;
import com.otterly76.ott.entity.custom.StorkEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StorkRenderer extends GeoEntityRenderer<StorkEntity> {
    public StorkRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new StorkModel());
    }
}
