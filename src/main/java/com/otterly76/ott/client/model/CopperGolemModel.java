package com.otterly76.ott.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.entity.custom.CopperGolem;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

public class CopperGolemModel extends HierarchicalModel<CopperGolem> implements ArmedModel {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public CopperGolemModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.rightArm = this.body.getChild("right_arm");
        this.leftArm = this.body.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 15).addBox(-4.0F, -6.0F, -3.0F, 8.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 19.0F, 0.0F));
        
        body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -5.0F, 8.0F, 5.0F, 10.0F, new CubeDeformation(0.015F))
                .texOffs(56, 0).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 3.0F, 2.0F)
                .texOffs(37, 8).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.015F))
                .texOffs(37, 0).addBox(-2.0F, -13.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.015F)), PartPose.offset(0.0F, -6.0F, 0.0F));
        
        body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(36, 16).addBox(-2.0F, 0.0F, -2.0F, 3.0F, 7.0F, 4.0F), PartPose.offset(-5.0F, -6.0F, 0.0F));
        body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(50, 16).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 7.0F, 4.0F), PartPose.offset(5.0F, -6.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 27).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F), PartPose.offset(-2.0F, 19.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 27).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F), PartPose.offset(2.0F, 19.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(CopperGolem entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        
        if (entity.getGolemState() == com.otterly76.ott.entity.custom.CopperGolemState.PRESSING_BUTTON) {
            this.head.yRot = ageInTicks * 0.5F;
        } else {
            this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
            this.head.xRot = headPitch * ((float)Math.PI / 180F);
        }
        
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }

    @Override
    public void translateToHand(@NotNull HumanoidArm arm, @NotNull PoseStack poseStack) {
        this.body.translateAndRotate(poseStack);
        if (arm == HumanoidArm.RIGHT) {
            this.rightArm.translateAndRotate(poseStack);
        } else {
            this.leftArm.translateAndRotate(poseStack);
        }
    }

    public void applyBlockOnAntennaTransform(PoseStack poseStack) {
        this.body.translateAndRotate(poseStack);
        this.head.translateAndRotate(poseStack);
        poseStack.translate(0.0D, -0.8125D, 0.0D);
    }
}
