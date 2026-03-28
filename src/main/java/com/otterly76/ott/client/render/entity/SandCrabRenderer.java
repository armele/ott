package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.SandCrabModel;
import com.otterly76.ott.entity.custom.SandCrabEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SandCrabRenderer extends GeoEntityRenderer<SandCrabEntity> {
    public SandCrabRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SandCrabModel());
        this.shadowRadius = 0.6f;
    }
}
