package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.BassModel;
import com.otterly76.ott.entity.custom.Bass;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BassRenderer extends GeoEntityRenderer<Bass> {
    public BassRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BassModel());
    }
}