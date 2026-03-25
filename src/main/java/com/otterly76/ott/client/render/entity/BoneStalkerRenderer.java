package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.BoneStalkerModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.BoneStalker;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BoneStalkerRenderer extends GeoEntityRenderer<BoneStalker> {
    public BoneStalkerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BoneStalkerModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}