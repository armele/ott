package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.entity.custom.IceologerEntity;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class IceologerRenderer extends MobRenderer<IceologerEntity, IllagerModel<IceologerEntity>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("ott",
            "textures/entity/iceologer/iceologer.png");

    public IceologerRenderer(EntityRendererProvider.Context context) {
        super(context, new IllagerModel<>(context.bakeLayer(ModelLayers.EVOKER)), 0.5f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull IceologerEntity entity) {
        return TEXTURE;
    }
}
