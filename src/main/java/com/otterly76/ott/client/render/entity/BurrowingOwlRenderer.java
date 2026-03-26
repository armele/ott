package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.BurrowingOwlModel;
import com.otterly76.ott.entity.custom.BurrowingOwlEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BurrowingOwlRenderer extends GeoEntityRenderer<BurrowingOwlEntity> {
    public BurrowingOwlRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BurrowingOwlModel());
    }
}
