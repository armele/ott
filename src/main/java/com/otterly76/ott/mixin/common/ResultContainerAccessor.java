package com.otterly76.ott.mixin.common;

import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ResultContainer.class)
public interface ResultContainerAccessor {
    @Accessor("itemStacks")
    NonNullList<ItemStack> ott$getItemStacks();

    @Mutable
    @Accessor("itemStacks")
    void ott$setItemStacks(NonNullList<ItemStack> itemStacks);
}