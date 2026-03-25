package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.SandHermitModel;
import com.otterly76.ott.entity.custom.SandHermit;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SandHermitRenderer extends GeoEntityRenderer<SandHermit> {
    public SandHermitRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SandHermitModel());
    }
}