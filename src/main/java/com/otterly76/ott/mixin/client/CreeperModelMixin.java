package com.otterly76.ott.mixin.client;

import com.otterly76.ott.util.entity.TinyMobRenderState;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperModel.class)
public abstract class CreeperModelMixin extends EntityModel<Creeper> {
    @Shadow @Final private ModelPart head;

    @Inject(method = "setupAnim", at = @At("RETURN"))
    private void ott$scaleHead(Entity entity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (TinyMobRenderState.isRenderingTiny) {
            this.head.xScale = 2.0f;
            this.head.yScale = 2.0f;
            this.head.zScale = 2.0f;
        } else {
            this.head.xScale = 1.0f;
            this.head.yScale = 1.0f;
            this.head.zScale = 1.0f;
        }
    }
}
