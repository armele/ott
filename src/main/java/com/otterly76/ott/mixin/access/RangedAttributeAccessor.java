package com.otterly76.ott.mixin.access;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RangedAttribute.class)
public interface RangedAttributeAccessor {
    @Accessor("minValue")
    void ott$setMinValue(double value);

    @Accessor("maxValue")
    void ott$setMaxValue(double value);
}
