package com.otterly76.ott.mixin.client;

import com.otterly76.ott.util.entity.OttBabyMob;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndermanModel.class)
public abstract class EndermanModelMixin<T extends LivingEntity> extends HumanoidModel<T> {

    private EndermanModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("RETURN"))
    private void ott$scaleTinyHead(T entity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        boolean tiny = entity instanceof OttBabyMob babyMob && babyMob.ott$isBaby();
        float s = tiny ? 2.0f : 1.0f;
        this.head.xScale = s; this.head.yScale = s; this.head.zScale = s;
    }
}