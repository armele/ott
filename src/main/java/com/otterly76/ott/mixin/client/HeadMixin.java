package com.otterly76.ott.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.otterly76.ott.client.render.DragonHeadGeoRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.cache.object.BakedGeoModel;

@Mixin(SkullBlockRenderer.class)
public class HeadMixin {
    @Unique
    private static final DragonHeadGeoRenderer ott$RENDERER = new DragonHeadGeoRenderer();

    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/SkullBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At("HEAD"), cancellable = true)
    private void ott$replaceDragonHead(SkullBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, CallbackInfo ci) {
        BlockState state = blockEntity.getBlockState();

        if (state.getBlock() instanceof AbstractSkullBlock skullBlock) {
            SkullBlock.Type type = skullBlock.getType();

            // Catch all five types
            if (type == SkullBlock.Types.DRAGON || type == SkullBlock.Types.ZOMBIE ||
                    type == SkullBlock.Types.SKELETON || type == SkullBlock.Types.WITHER_SKELETON ||
                    type == com.otterly76.ott.util.block.ModSkullType.DRAGON_SKULL) {

                ci.cancel();
                ott$RENDERER.getAnimatable().setHeadType(type);

                poseStack.pushPose();
                poseStack.translate(0.5D, 0.0D, 0.5D);

                if (state.hasProperty(SkullBlock.ROTATION)) {
                    // FLOOR
                    float rotation = (float)state.getValue(SkullBlock.ROTATION) * 22.5F;
                    poseStack.mulPose(Axis.YP.rotationDegrees(-rotation));
                }
                else if (state.hasProperty(WallSkullBlock.FACING)) {
                    // WALL
                    Direction facing = state.getValue(WallSkullBlock.FACING);
                    poseStack.translate(0, 0.25, 0);

                    switch (facing) {
                        case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(0f));
                        case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180f));
                        case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(270f));
                        case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90f));
                    }

                    // NUDGE: Dragon needs -0.5, small heads might only need -0.05
                    float nudge = (type == SkullBlock.Types.DRAGON || type == com.otterly76.ott.util.block.ModSkullType.DRAGON_SKULL) ? -0.5f : 0.25f;
                    poseStack.translate(0, 0, nudge);
                }

                // Render Call
                ResourceLocation texture = ott$RENDERER.getTextureLocation(ott$RENDERER.getAnimatable());
                BakedGeoModel bakedModel = ott$RENDERER.getGeoModel().getBakedModel(ott$RENDERER.getGeoModel().getModelResource(ott$RENDERER.getAnimatable()));
                RenderType renderType = ott$RENDERER.getRenderType(ott$RENDERER.getAnimatable(), texture, bufferSource, partialTick);

                ott$RENDERER.actuallyRender(poseStack, ott$RENDERER.getAnimatable(), bakedModel,
                        renderType, bufferSource, bufferSource.getBuffer(renderType != null ? renderType : RenderType.entityCutout(texture)),
                        false, partialTick, packedLight, packedOverlay, -1);

                poseStack.popPose();
            }
        }
    }
}
