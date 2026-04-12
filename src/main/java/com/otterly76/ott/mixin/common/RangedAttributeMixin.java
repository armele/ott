package com.otterly76.ott.mixin.common;

import com.otterly76.ott.accessor.RangedAttributeAccessor;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RangedAttribute.class)
public abstract class RangedAttributeMixin implements RangedAttributeAccessor {

    @Mutable
    @Final
    @Shadow
    private double minValue;

    @Mutable
    @Final
    @Shadow
    private double maxValue;

    @Override
    public void ott$setMinValue(double value) {
        this.minValue = value;
    }

    @Override
    public void ott$setMaxValue(double value) {
        this.maxValue = value;
    }
}
