package com.otterly76.ott.mixin.client;

import com.otterly76.ott.util.entity.TinyMobRenderState;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> {
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart hat;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void ott$scaleTinyHead(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (TinyMobRenderState.isRenderingTiny) {
            // Apply 2.0x scale to the head and hat parts to get the "big head" baby look.
            // Since isBaby() is now false, we don't have to worry about vanilla baby offsets.
            // ModelPart.render applies this scale around the part's pivot, preserving neck alignment.
            this.head.xScale = 2.0f;
            this.head.yScale = 2.0f;
            this.head.zScale = 2.0f;
            this.hat.xScale = 2.0f;
            this.hat.yScale = 2.0f;
            this.hat.zScale = 2.0f;
        } else {
            // Reset to default for other entities using the same model instance.
            this.head.xScale = 1.0f;
            this.head.yScale = 1.0f;
            this.head.zScale = 1.0f;
            this.hat.xScale = 1.0f;
            this.hat.yScale = 1.0f;
            this.hat.zScale = 1.0f;
        }
    }
}
