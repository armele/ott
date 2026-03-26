package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.EchidnaModel;
import com.otterly76.ott.entity.custom.EchidnaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EchidnaRenderer extends GeoEntityRenderer<EchidnaEntity> {
    public EchidnaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EchidnaModel());
    }
}
