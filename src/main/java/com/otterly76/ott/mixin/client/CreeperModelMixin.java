package com.otterly76.ott.mixin.client;

import com.otterly76.ott.util.entity.OttBabyMob;
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
        boolean tiny = entity instanceof OttBabyMob babyMob && babyMob.ott$isBaby();
        float s = tiny ? 2.0f : 1.0f;
        this.head.xScale = s; this.head.yScale = s; this.head.zScale = s;
    }
}