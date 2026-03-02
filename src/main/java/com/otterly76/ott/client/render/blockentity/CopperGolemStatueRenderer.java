package com.otterly76.ott.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.block.custom.CopperGolemStatueBlock;
import com.otterly76.ott.block.entity.CopperGolemStatueBlockEntity;
import com.otterly76.ott.client.registries.ModModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Axis;
import org.jetbrains.annotations.NotNull;

public class CopperGolemStatueRenderer implements BlockEntityRenderer<CopperGolemStatueBlockEntity> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    private static final ResourceLocation UNAFFECTED_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/copper_golem/copper_golem.png");
    private static final ResourceLocation EXPOSED_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/copper_golem/exposed_copper_golem.png");
    private static final ResourceLocation WEATHERED_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/copper_golem/weathered_copper_golem.png");
    private static final ResourceLocation OXIDIZED_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/copper_golem/oxidized_copper_golem.png");

    public CopperGolemStatueRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelPart = context.bakeLayer(ModModelLayers.COPPER_GOLEM);
        this.root = modelPart;
        this.body = modelPart.getChild("body");
        this.head = this.body.getChild("head");
        this.rightArm = this.body.getChild("right_arm");
        this.leftArm = this.body.getChild("left_arm");
        this.rightLeg = modelPart.getChild("right_leg");
        this.leftLeg = modelPart.getChild("left_leg");
    }

    @Override
    public void render(@NotNull CopperGolemStatueBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        var state = blockEntity.getBlockState();
        if (!(state.getBlock() instanceof CopperGolemStatueBlock statueBlock)) return;

        poseStack.pushPose();
        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        
        float rotation = state.getValue(CopperGolemStatueBlock.FACING).toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        this.resetModel();
        this.applyPose(state.getValue(CopperGolemStatueBlock.POSE));

        ResourceLocation texture = switch (statueBlock.getAge()) {
            case UNAFFECTED -> UNAFFECTED_TEXTURE;
            case EXPOSED -> EXPOSED_TEXTURE;
            case WEATHERED -> WEATHERED_TEXTURE;
            case OXIDIZED -> OXIDIZED_TEXTURE;
        };

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        
        poseStack.popPose();
    }

    private void resetModel() {
        this.root.resetPose();
        this.body.resetPose();
        this.head.resetPose();
        this.rightArm.resetPose();
        this.leftArm.resetPose();
        this.rightLeg.resetPose();
        this.leftLeg.resetPose();
    }

    private void applyPose(CopperGolemStatueBlock.Pose pose) {
        switch (pose) {
            case STANDING -> {}
            case RUNNING -> {
                this.rightLeg.xRot = 0.5F;
                this.leftLeg.xRot = -0.5F;
                this.rightArm.xRot = -0.5F;
                this.leftArm.xRot = 0.5F;
            }
            case SITTING -> {
                this.root.y = 22.0F;
                this.rightLeg.xRot = -1.5F;
                this.rightLeg.yRot = 0.3F;
                this.leftLeg.xRot = -1.5F;
                this.leftLeg.yRot = -0.3F;
            }
            case STAR -> {
                this.rightLeg.zRot = 0.5F;
                this.leftLeg.zRot = -0.5F;
                this.rightArm.zRot = 1.0F;
                this.leftArm.zRot = -1.0F;
            }
        }
    }
}
