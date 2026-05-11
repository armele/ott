package com.otterly76.ott.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.client.model.nautilus.NautilusModel;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.entity.custom.NautilusEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class NautilusRenderer extends MobRenderer<NautilusEntity, NautilusModel<NautilusEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/entity/nautilus/nautilus.png");
    private static final ResourceLocation TEXTURE_BABY =
            ResourceLocation.withDefaultNamespace("textures/entity/nautilus/nautilus_baby.png");

    private final NautilusModel<NautilusEntity> adultModel;
    private final NautilusModel<NautilusEntity> babyModel;

    public NautilusRenderer(EntityRendererProvider.Context context) {
        super(context, new NautilusModel<>(context.bakeLayer(ModModelLayers.NAUTILUS)), 0.5F);
        this.adultModel = this.model;
        this.babyModel = new NautilusModel<>(context.bakeLayer(ModModelLayers.NAUTILUS_BABY));
    }

    @Override
    public void render(@NotNull NautilusEntity entity, float entityYaw, float partialTick,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        this.model = entity.isBaby() ? this.babyModel : this.adultModel;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull NautilusEntity entity) {
        return entity.isBaby() ? TEXTURE_BABY : TEXTURE;
    }
}
