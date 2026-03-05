package com.otterly76.ott.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.otterly76.ott.client.render.BedGeoRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.cache.object.BakedGeoModel;

@Mixin(BedRenderer.class)
public class BedMixin {
    @Unique
    private static final BedGeoRenderer ott$RENDERER = new BedGeoRenderer();

    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/BedBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At("HEAD"), cancellable = true)
    private void ott$replaceBed(BedBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, CallbackInfo ci) {
        BlockState state = blockEntity.getBlockState();
        Level level = blockEntity.getLevel();

        if (state.getBlock() instanceof BedBlock) {
            ci.cancel();

            DyeColor color = blockEntity.getColor();
            ott$RENDERER.getAnimatable().setColor(color);

            poseStack.pushPose();

            if (level != null) {
                // World rendering
                Direction facing = state.getValue(BedBlock.FACING);
                BedPart part = state.getValue(BedBlock.PART);

                // Match vanilla light combining
                DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> neighborCombineResult = DoubleBlockCombiner.combineWithNeigbour(
                        BlockEntityType.BED, BedBlock::getBlockType, BedBlock::getConnectedDirection, ChestBlock.FACING, state, level, blockEntity.getBlockPos(), (l, p) -> false);
                int combinedLight = neighborCombineResult.apply(new BrightnessCombiner<>()).applyAsInt(packedLight);

                // Move to center of the block
                poseStack.translate(0.5D, 0.0D, 0.5D);

                // Rotate based on facing.
                float rotation = -facing.toYRot();
                poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

                // FOOT: model starts at Z 0. Move it to block Z 0 (from current block center 0.5)
                // HEAD: model head part starts at Z 16. Move it to block Z 0.
                double zOffset = (part == BedPart.FOOT) ? -0.5D : -1.5D;
                poseStack.translate(0, 0, zOffset);

                ott$renderBedPart(poseStack, bufferSource, partialTick, combinedLight, packedOverlay, part == BedPart.FOOT, false);
            } else {
                // Item rendering
                poseStack.translate(0.5D, 0.5D, 0.5D);

                // Default orientation for item - SOUTH matches vanilla item view mostly
                poseStack.mulPose(Axis.YP.rotationDegrees(-40.0F));

                // Centering the 2-block bed
                poseStack.translate(0, -0.75, -0.3D);

                ott$renderBedPart(poseStack, bufferSource, partialTick, packedLight, packedOverlay, false, true);
            }

            poseStack.popPose();
        }
    }

    @Unique
    private void ott$renderBedPart(PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay, boolean isFoot, boolean isItem) {
        ResourceLocation texture = ott$RENDERER.getTextureLocation(ott$RENDERER.getAnimatable());
        BakedGeoModel bakedModel = ott$RENDERER.getGeoModel().getBakedModel(ott$RENDERER.getGeoModel().getModelResource(ott$RENDERER.getAnimatable()));
        RenderType renderType = ott$RENDERER.getRenderType(ott$RENDERER.getAnimatable(), texture, bufferSource, partialTick);

        if (isItem) {
            // Show everything for item
            bakedModel.getBone("head").ifPresent(bone -> bone.setHidden(false));
            bakedModel.getBone("leg1").ifPresent(bone -> bone.setHidden(false));
            bakedModel.getBone("leg2").ifPresent(bone -> bone.setHidden(false));
            bakedModel.getBone("foot").ifPresent(bone -> bone.setHidden(false));
            bakedModel.getBone("leg3").ifPresent(bone -> bone.setHidden(false));
            bakedModel.getBone("leg4").ifPresent(bone -> bone.setHidden(false));
        } else {
            // Visibility handling for split rendering in world
            bakedModel.getBone("head").ifPresent(bone -> bone.setHidden(isFoot));
            bakedModel.getBone("leg1").ifPresent(bone -> bone.setHidden(isFoot));
            bakedModel.getBone("leg2").ifPresent(bone -> bone.setHidden(isFoot));

            bakedModel.getBone("foot").ifPresent(bone -> bone.setHidden(!isFoot));
            bakedModel.getBone("leg3").ifPresent(bone -> bone.setHidden(!isFoot));
            bakedModel.getBone("leg4").ifPresent(bone -> bone.setHidden(!isFoot));
        }

        ott$RENDERER.actuallyRender(poseStack, ott$RENDERER.getAnimatable(), bakedModel,
                renderType, bufferSource, bufferSource.getBuffer(renderType != null ? renderType : RenderType.entityCutout(texture)),
                false, partialTick, packedLight, packedOverlay, -1);
    }
}