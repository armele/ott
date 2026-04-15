package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.entity.custom.CamelHuskEntity;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CamelHuskRenderer extends MobRenderer<CamelHuskEntity, CamelModel<CamelHuskEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/entity/camel/camel_husk.png");

    public CamelHuskRenderer(EntityRendererProvider.Context context) {
        super(context, new CamelModel<>(context.bakeLayer(ModelLayers.CAMEL)), 0.7F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CamelHuskEntity entity) {
        return TEXTURE;
    }
}
