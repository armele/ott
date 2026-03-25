package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.BoggedBoneStalkerModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.BoggedBoneStalker;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BoggedBoneStalkerRenderer extends GeoEntityRenderer<BoggedBoneStalker> {
    public BoggedBoneStalkerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BoggedBoneStalkerModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}