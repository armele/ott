package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.TuffGolemModel;
import com.otterly76.ott.client.render.layers.TuffGolemColorLayer;
import com.otterly76.ott.entity.custom.TuffGolemEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TuffGolemRenderer extends GeoEntityRenderer<TuffGolemEntity> {
    public TuffGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TuffGolemModel());
        this.shadowRadius = 0.5f;
        this.addRenderLayer(new TuffGolemColorLayer(this));
    }
}
