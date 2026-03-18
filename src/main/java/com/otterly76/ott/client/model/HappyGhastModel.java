package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.HappyGhast;
import com.otterly76.ott.util.ModTags;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class HappyGhastModel<T extends HappyGhast> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart inner_body;
    private final ModelPart face_neutral;
    private final ModelPart face_happy;
    private final ModelPart face_sad;
    private final ModelPart[] tentacles = new ModelPart[9];

    public HappyGhastModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.inner_body = this.body.getChild("inner_body");
        this.face_neutral = this.body.getChild("face_neutral");
        this.face_happy = this.body.getChild("face_happy");
        this.face_sad = this.body.getChild("face_sad");

        for(int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i] = this.body.getChild(createTentacleName(i));
        }
    }

    private static String createTentacleName(int index) {
        return "tentacle" + index;
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, deformation), PartPose.offset(0.0F, 16.0F, 0.0F));
        body.addOrReplaceChild("inner_body", CubeListBuilder.create().texOffs(0, 32).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, deformation.extend(-0.1F)), PartPose.ZERO);
        body.addOrReplaceChild("face_neutral", CubeListBuilder.create().texOffs(48, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 0.0F, deformation.extend(0.01F)), PartPose.ZERO);
        body.addOrReplaceChild("face_happy", CubeListBuilder.create().texOffs(0, 32).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 0.0F, deformation.extend(0.01F)), PartPose.ZERO);
        body.addOrReplaceChild("face_sad", CubeListBuilder.create().texOffs(48, 32).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 0.0F, deformation.extend(0.01F)), PartPose.ZERO);
        body.addOrReplaceChild(createTentacleName(0), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, deformation), PartPose.offset(-3.75F, 7.0F, -5.0F));
        body.addOrReplaceChild(createTentacleName(1), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, deformation), PartPose.offset(1.25F, 7.0F, -5.0F));
        body.addOrReplaceChild(createTentacleName(2), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, deformation), PartPose.offset(6.25F, 7.0F, -5.0F));
        body.addOrReplaceChild(createTentacleName(3), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, deformation), PartPose.offset(-6.25F, 7.0F, 0.0F));
        body.addOrReplaceChild(createTentacleName(4), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, deformation), PartPose.offset(-1.25F, 7.0F, 0.0F));
        body.addOrReplaceChild(createTentacleName(5), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, deformation), PartPose.offset(3.75F, 7.0F, 0.0F));
        body.addOrReplaceChild(createTentacleName(6), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, deformation), PartPose.offset(-3.75F, 7.0F, 5.0F));
        body.addOrReplaceChild(createTentacleName(7), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, deformation), PartPose.offset(1.25F, 7.0F, 5.0F));
        body.addOrReplaceChild(createTentacleName(8), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, deformation), PartPose.offset(6.25F, 7.0F, 5.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.inner_body.visible = entity.isBaby();
        
        this.face_neutral.visible = true;
        this.face_happy.visible = false;
        this.face_sad.visible = false;

        if (entity.getItemBySlot(EquipmentSlot.CHEST).is(ModTags.ItemTags.HARNESSES)) {
            this.body.xScale = 0.9375F;
            this.body.yScale = 0.9375F;
            this.body.zScale = 0.9375F;
        } else {
            this.body.xScale = 1.0F;
            this.body.yScale = 1.0F;
            this.body.zScale = 1.0F;
        }

        for(int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i].xRot = 0.2F * Mth.sin(ageInTicks * 0.3F + (float)i) + 0.4F;
        }
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }
}





