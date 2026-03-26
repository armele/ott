package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.PallasCatModel;
import com.otterly76.ott.entity.custom.PallasCatEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PallasCatRenderer extends GeoEntityRenderer<PallasCatEntity> {
    public PallasCatRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PallasCatModel());
    }
}
