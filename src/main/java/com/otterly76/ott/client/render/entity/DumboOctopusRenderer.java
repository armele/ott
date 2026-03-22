package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.DumboOctopusModel;
import com.otterly76.ott.entity.custom.DumboOctopusEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class DumboOctopusRenderer extends GeoEntityRenderer<DumboOctopusEntity> {
    public DumboOctopusRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DumboOctopusModel());
        this.shadowRadius = 0.3F;
    }

    @Override
    public float getMotionAnimThreshold(DumboOctopusEntity animatable) {
        return 0.000001f;
    }

    @Override
    public RenderType getRenderType(DumboOctopusEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}