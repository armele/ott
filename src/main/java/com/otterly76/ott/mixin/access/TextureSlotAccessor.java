package com.otterly76.ott.mixin.access;

import net.minecraft.data.models.model.TextureSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextureSlot.class)
public interface TextureSlotAccessor {
    @Invoker("create")
    static TextureSlot create(String name) {
        throw new UnsupportedOperationException();
    }
}
