package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.CoconutCrabModel;
import com.otterly76.ott.entity.custom.CoconutCrabEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CoconutCrabRenderer extends GeoEntityRenderer<CoconutCrabEntity> {
    public CoconutCrabRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CoconutCrabModel());
        this.shadowRadius = 0.6f;
    }
}
