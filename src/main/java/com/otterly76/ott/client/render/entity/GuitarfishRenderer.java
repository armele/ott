package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.GuitarfishModel;
import com.otterly76.ott.entity.custom.GuitarfishEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GuitarfishRenderer extends GeoEntityRenderer<GuitarfishEntity> {
    public GuitarfishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GuitarfishModel());
    }
}
