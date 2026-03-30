package com.otterly76.ott.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.otterly76.ott.block.color.ColorSetBedBlock;
import com.otterly76.ott.block.color.ColorSetBedBlockEntity;
import com.otterly76.ott.block.entity.ModBlockEntities;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import org.jetbrains.annotations.NotNull;

public class ColorSetBedRenderer implements BlockEntityRenderer<ColorSetBedBlockEntity> {
    private final ModelPart headRoot;
    private final ModelPart footRoot;

    public ColorSetBedRenderer(BlockEntityRendererProvider.Context context) {
        EntityModelSet entityModelSet = context.getModelSet();
        this.headRoot = entityModelSet.bakeLayer(ModelLayers.BED_HEAD);
        this.footRoot = entityModelSet.bakeLayer(ModelLayers.BED_FOOT);
    }

    @Override
    public void render(ColorSetBedBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        if (!(blockState.getBlock() instanceof ColorSetBedBlock bedBlock)) return;

        String colorName = bedBlock.getColorName();
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("ott", "textures/block/color_set/" + colorName + "/bed.png");

        Level level = blockEntity.getLevel();
        if (level != null) {
            DoubleBlockCombiner.NeighborCombineResult<? extends ColorSetBedBlockEntity> neighborResult = DoubleBlockCombiner.combineWithNeigbour(
                    ModBlockEntities.COLOR_SET_BED.get(), BedBlock::getBlockType, BedBlock::getConnectedDirection, ChestBlock.FACING, blockState, level, blockEntity.getBlockPos(), (p_112202_, p_112203_) -> false
            );
            int light = neighborResult.apply(new BrightnessCombiner<>()).get(combinedLight);
            this.renderPiece(poseStack, bufferSource, blockState.getValue(BedBlock.PART) == BedPart.HEAD ? this.headRoot : this.footRoot, blockState.getValue(BedBlock.FACING), light, combinedOverlay, false, texture);
        } else {
            this.renderPiece(poseStack, bufferSource, this.headRoot, Direction.SOUTH, combinedLight, combinedOverlay, false, texture);
            this.renderPiece(poseStack, bufferSource, this.footRoot, Direction.SOUTH, combinedLight, combinedOverlay, true, texture);
        }
    }

    private void renderPiece(PoseStack poseStack, MultiBufferSource bufferSource, ModelPart modelPart, Direction direction, int light, int combinedOverlay, boolean isFoot, ResourceLocation texture) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.5625D, isFoot ? -1.0D : 0.0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F + direction.toYRot()));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entitySolid(texture));
        modelPart.render(poseStack, consumer, light, combinedOverlay, 0xFFFFFFFF);

        poseStack.popPose();
    }
}