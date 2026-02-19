package com.otterly76.ott.mixin.common;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleContainer.class)
public interface SimpleContainerAccessor {
    @Accessor("items")
    NonNullList<ItemStack> ott$getItems();

    @Mutable
    @Accessor("items")
    void ott$setItems(NonNullList<ItemStack> items);

    @Accessor("size")
    int ott$getSize();

    @Accessor("size")
    void ott$setSize(int size);
}
