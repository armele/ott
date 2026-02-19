package com.otterly76.ott.mixin.access;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BundleContents.class)
public interface BundleContentsAccessor {
    @Invoker("getWeight")
    static Fraction callGetWeight(ItemStack stack) {
        throw new UnsupportedOperationException();
    }
}
