package com.otterly76.ott.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.otterly76.ott.client.model.CopperGolemModel;
import com.otterly76.ott.entity.custom.CopperGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class CopperGolemAntennaLayer extends RenderLayer<CopperGolem, CopperGolemModel> {
    private final BlockRenderDispatcher blockRenderer;

    public CopperGolemAntennaLayer(RenderLayerParent<CopperGolem, CopperGolemModel> parent) {
        super(parent);
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull CopperGolem entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.hasPoppy()) return;

        BlockState poppyState = Blocks.POPPY.defaultBlockState();
        poseStack.pushPose();
        
        this.getParentModel().applyBlockOnAntennaTransform(poseStack);
        
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        
        this.blockRenderer.renderSingleBlock(poppyState, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutout());
        
        poseStack.popPose();
    }
}
