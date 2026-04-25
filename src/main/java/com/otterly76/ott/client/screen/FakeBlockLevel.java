package com.otterly76.ott.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import java.util.Set;

public class FakeBlockLevel implements BlockAndTintGetter {

    @Nullable private BlockState state;
    @Nullable private Set<BlockPos> positions;

    public void setState(@Nullable BlockState state) { this.state = state; }
    public void setPositions(@Nullable Set<BlockPos> positions) { this.positions = positions; }

    @Override
    public float getShade(@NotNull Direction direction, boolean shade) { return 1f; }

    @Override
    public @NotNull LevelLightEngine getLightEngine() { throw new UnsupportedOperationException(); }

    @Override
    public int getBlockTint(@NotNull BlockPos pos, @NotNull ColorResolver resolver) {
        return Objects.requireNonNull(Minecraft.getInstance().level).getBlockTint(pos, resolver);
    }

    @Override
    public int getBrightness(@NotNull LightLayer lightType, @NotNull BlockPos pos) { return 15; }

    @Override
    public int getRawBrightness(@NotNull BlockPos pos, int amount) { return 15; }

    @Override
    public @Nullable BlockEntity getBlockEntity(@NotNull BlockPos pos) { return null; }

    @Override
    public @NotNull BlockState getBlockState(@NotNull BlockPos pos) {
        return (state != null && positions != null && positions.contains(pos))
                ? state : Blocks.AIR.defaultBlockState();
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockPos pos) {
        return Blocks.AIR.defaultBlockState().getFluidState();
    }

    @Override
    public int getHeight() { return 384; }

    @Override
    public int getMinBuildHeight() { return -64; }

    public void renderBlock(PoseStack poseStack) {
        if (state == null || positions == null) return;
        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher dispatcher = mc.getBlockRenderer();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        var random = Objects.requireNonNull(mc.level).random;

        Lighting.setupForFlatItems();

        var model = dispatcher.getBlockModel(state);
        // Use the model's own render types — avoids deprecated ItemBlockRenderTypes
        // and correctly handles multi-layer blocks (cutout, translucent, etc.)
        var renderTypes = model.getRenderTypes(state, random, ModelData.EMPTY);

        for (var renderType : renderTypes) {
            for (BlockPos pos : positions) {
                poseStack.pushPose();
                poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
                var modelData = model.getModelData(this, pos, state, ModelData.EMPTY);
                dispatcher.renderBatched(state, pos, this, poseStack,
                        bufferSource.getBuffer(renderType), true,
                        random, modelData, renderType);
                poseStack.popPose();
            }
            bufferSource.endBatch();
        }
        Lighting.setupFor3DItems();
    }
}