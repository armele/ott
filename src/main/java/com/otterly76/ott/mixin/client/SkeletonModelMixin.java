package com.otterly76.ott.mixin.client;

import com.otterly76.ott.util.entity.TinyMobRenderState;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonModel.class)
public abstract class SkeletonModelMixin<T extends AbstractSkeleton> extends HumanoidModel<T> {

    private SkeletonModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("RETURN"))
    private void ott$scaleTinyHead(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (TinyMobRenderState.isRenderingTiny) {
            this.head.xScale = 2.0f;
            this.head.yScale = 2.0f;
            this.head.zScale = 2.0f;
            this.hat.xScale = 2.0f;
            this.hat.yScale = 2.0f;
            this.hat.zScale = 2.0f;
        } else {
            this.head.xScale = 1.0f;
            this.head.yScale = 1.0f;
            this.head.zScale = 1.0f;
            this.hat.xScale = 1.0f;
            this.hat.yScale = 1.0f;
            this.hat.zScale = 1.0f;
        }
    }
}
