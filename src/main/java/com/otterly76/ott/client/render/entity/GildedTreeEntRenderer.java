package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.GildedTreeEntModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.GildedTreeEnt;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GildedTreeEntRenderer extends GeoEntityRenderer<GildedTreeEnt> {
    public GildedTreeEntRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GildedTreeEntModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}