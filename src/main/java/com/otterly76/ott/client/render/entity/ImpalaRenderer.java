package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.ImpalaModel;
import com.otterly76.ott.entity.custom.ImpalaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ImpalaRenderer extends GeoEntityRenderer<ImpalaEntity> {
    public ImpalaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ImpalaModel());
    }
}
