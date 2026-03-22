package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.Shrimp1Model;
import com.otterly76.ott.entity.custom.Shrimp1Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class Shrimp1Renderer extends GeoEntityRenderer<Shrimp1Entity> {
    public Shrimp1Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Shrimp1Model());
    }
}