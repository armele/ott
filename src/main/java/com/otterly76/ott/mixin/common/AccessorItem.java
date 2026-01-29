package com.otterly76.ott.mixin.common;


import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Item.class})
public interface AccessorItem {
    @Mutable
    @Accessor("craftingRemainingItem")
    void ott$setCraftingRemainder(Item var1);

    @Accessor("craftingRemainingItem")
    Item ott$getCraftingRemainder();
}
