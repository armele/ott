package com.otterly76.ott.mixin.common;

import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemCombinerMenu.class)
public interface ItemCombinerMenuAccessor {
    @Accessor("access")
    ContainerLevelAccess ott$getAccess();

    @Accessor("access")
    @Mutable
    void ott$setAccess(ContainerLevelAccess access);
}
