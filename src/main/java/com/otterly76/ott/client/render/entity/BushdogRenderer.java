package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.BushdogModel;
import com.otterly76.ott.entity.custom.BushdogEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BushdogRenderer extends GeoEntityRenderer<BushdogEntity> {
    public BushdogRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BushdogModel());
    }
}
