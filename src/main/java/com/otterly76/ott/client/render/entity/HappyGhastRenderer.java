package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.HappyGhastHarnessModel;
import com.otterly76.ott.client.model.HappyGhastModel;
import com.otterly76.ott.client.render.layers.GhastHarnessLayer;
import com.otterly76.ott.client.render.layers.RopesLayer;
import com.otterly76.ott.client.render.layers.SimpleEquipmentLayer;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.entity.custom.HappyGhast;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class HappyGhastRenderer extends MobRenderer<HappyGhast, HappyGhastModel<HappyGhast>> {
    private static final ResourceLocation GHAST_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/ghast/happy_ghast.png");
    private static final ResourceLocation GHAST_BABY_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/ghast/happy_ghast_baby.png");
    private static final ResourceLocation GHAST_ROPES = ResourceLocation.withDefaultNamespace("textures/entity/ghast/happy_ghast_ropes.png");

    public HappyGhastRenderer(EntityRendererProvider.Context context) {
        super(context, new HappyGhastModel<>(context.bakeLayer(ModModelLayers.HAPPY_GHAST)), 1.5F);
        this.addLayer(new SimpleEquipmentLayer<>(this, GhastHarnessLayer.TEXTURE_BY_ITEM, EquipmentSlot.CHEST, HappyGhast::isHarnessed, new HappyGhastHarnessModel<>(context.bakeLayer(ModModelLayers.HAPPY_GHAST_HARNESS)), null));
        this.addLayer(new RopesLayer<>(this, context.getModelSet(), GHAST_ROPES));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(HappyGhast entity) {
        return entity.isBaby() ? GHAST_BABY_LOCATION : GHAST_LOCATION;
    }

    @Override
    protected void scale(HappyGhast entity, PoseStack matrices, float partialTicks) {
        float scale = entity.isBaby() ? 0.95F : 4.0F;
        matrices.scale(scale, scale, scale);
    }
}