package com.otterly76.ott.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.otterly76.ott.block.entity.ElevatorBlockEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static com.otterly76.ott.Constants.MOD_ID;

public class ElevatorArrowRenderer implements BlockEntityRenderer<ElevatorBlockEntity> {

    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/block/arrow.png");

    public ElevatorArrowRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(@NotNull ElevatorBlockEntity be, float partialTick, @NotNull PoseStack pose,
                       @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (!be.isShowArrow()) return;

        Direction facing = be.getFacing();

        pose.pushPose();
        // Translate to the center of the top face, slightly above to avoid z-fighting
        pose.translate(0.5, 1.001, 0.5);
        // The arrow texture points WEST (left). Rotate so it points in the facing direction.
        // facing.toYRot(): SOUTH=0, WEST=90, NORTH=180, EAST=270
        // Arrow default is WEST (90°), so offset by -90 to align with facing.
        pose.mulPose(Axis.YP.rotationDegrees(-(facing.toYRot() - 90f)));
        pose.translate(-0.5, 0.0, -0.5);

        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentCull(ARROW_TEXTURE));
        PoseStack.Pose last = pose.last();

        // Top-face quad: x∈[0,1], z∈[0,1], y=0 (already at top face height)
        // Full-bright so the arrow is unaffected by scene lighting.
        vc.addVertex(last, 0f, 0f, 0f).setColor(255, 255, 255, 255).setUv(0f, 0f).setOverlay(packedOverlay).setLight(LightTexture.FULL_BRIGHT).setNormal(last, 0f, 1f, 0f);
        vc.addVertex(last, 0f, 0f, 1f).setColor(255, 255, 255, 255).setUv(0f, 1f).setOverlay(packedOverlay).setLight(LightTexture.FULL_BRIGHT).setNormal(last, 0f, 1f, 0f);
        vc.addVertex(last, 1f, 0f, 1f).setColor(255, 255, 255, 255).setUv(1f, 1f).setOverlay(packedOverlay).setLight(LightTexture.FULL_BRIGHT).setNormal(last, 0f, 1f, 0f);
        vc.addVertex(last, 1f, 0f, 0f).setColor(255, 255, 255, 255).setUv(1f, 0f).setOverlay(packedOverlay).setLight(LightTexture.FULL_BRIGHT).setNormal(last, 0f, 1f, 0f);

        pose.popPose();
    }
}
