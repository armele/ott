package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.TreeEntModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.TreeEnt;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TreeEntRenderer extends GeoEntityRenderer<TreeEnt> {
    public TreeEntRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TreeEntModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}