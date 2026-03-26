package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.MoleModel;
import com.otterly76.ott.entity.custom.MoleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MoleRenderer extends GeoEntityRenderer<MoleEntity> {
    public MoleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MoleModel());
    }
}
