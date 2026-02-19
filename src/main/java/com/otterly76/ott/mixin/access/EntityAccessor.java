package com.otterly76.ott.mixin.access;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("dimensions")
    EntityDimensions getDimensions();

    @Invoker("reapplyPosition")
    void callReapplyPosition();

    @Invoker("setRot")
    void callSetRot(float yRot, float xRot);
}
