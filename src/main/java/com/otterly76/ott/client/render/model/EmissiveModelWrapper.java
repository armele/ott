package com.otterly76.ott.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EmissiveModelWrapper implements BakedModel {
    private final BakedModel baseModel;
    private final BakedModel emissiveModel;

    public EmissiveModelWrapper(BakedModel baseModel, BakedModel emissiveModel) {
        this.baseModel = baseModel;
        this.emissiveModel = emissiveModel;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        return this.getQuads(state, side, rand, ModelData.EMPTY, null);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        List<BakedQuad> quads = new ArrayList<>(this.baseModel.getQuads(state, side, rand, extraData, renderType));

        for(BakedQuad quad : this.emissiveModel.getQuads(state, side, rand, extraData, renderType)) {
            quads.add(new EmissiveQuad(quad));
        }

        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.baseModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.baseModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.baseModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return this.baseModel.isCustomRenderer();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return this.getParticleIcon(ModelData.EMPTY);
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return this.baseModel.getParticleIcon(data);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull ItemTransforms getTransforms() {
        return this.baseModel.getTransforms();
    }

    @Override
    public @NotNull BakedModel applyTransform(@NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, boolean leftHand) {
        BakedModel bakedmodel = this.baseModel.applyTransform(context, poseStack, leftHand);
        return (bakedmodel == this.baseModel) ? this : new EmissiveModelWrapper(bakedmodel, this.emissiveModel);
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return this.baseModel.getOverrides();
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
        return this.baseModel.getModelData(level, pos, state, modelData);
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return this.baseModel.getRenderTypes(state, rand, data);
    }

    @Override
    public @NotNull List<RenderType> getRenderTypes(@NotNull ItemStack stack, boolean isGui) {
        return this.baseModel.getRenderTypes(stack, isGui);
    }
}