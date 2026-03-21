package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.Starfish1Model;
import com.otterly76.ott.entity.custom.Starfish1Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class Starfish1Renderer extends GeoEntityRenderer<Starfish1Entity> {
    public Starfish1Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Starfish1Model());
    }
}
