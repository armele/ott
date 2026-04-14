package com.otterly76.ott.client.model.nautilus;

import com.otterly76.ott.entity.custom.AbstractNautilusEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Ported from 1.21.11 NautilusArmorModel.
 * Shell slightly inflated (CubeDeformation 0.01) to sit over the base model.
 */
@OnlyIn(Dist.CLIENT)
public class NautilusArmorModel<T extends AbstractNautilusEntity> extends NautilusModel<T> {

    public NautilusArmorModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = createBodyMesh();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition nautilus = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 29.0F, -6.0F));
        nautilus.addOrReplaceChild("shell",
            CubeListBuilder.create()
                .texOffs(0, 0).addBox(-7.0F, -10.0F, -7.0F, 14.0F, 10.0F, 16.0F, new CubeDeformation(0.01F))
                .texOffs(0, 26).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 8.0F, 20.0F, new CubeDeformation(0.01F))
                .texOffs(48, 26).addBox(-7.0F, 0.0F, 6.0F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -13.0F, 5.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
