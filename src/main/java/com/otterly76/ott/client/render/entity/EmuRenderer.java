package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.EmuModel;
import com.otterly76.ott.entity.custom.Emu;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EmuRenderer extends GeoEntityRenderer<Emu> {
    public EmuRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EmuModel());
    }

    @Override
    protected float getShadowRadius(@NotNull Emu entity) {
        return 0.6F;
    }
}