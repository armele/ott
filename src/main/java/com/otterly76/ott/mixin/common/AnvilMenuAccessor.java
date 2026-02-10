package com.otterly76.ott.mixin.common;

import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnvilMenu.class)
public interface AnvilMenuAccessor {
    @Accessor("itemName")
    String ott$getItemName();

    @Accessor("itemName")
    void ott$setItemName(String itemName);

    @Accessor("repairItemCountCost")
    int ott$getRepairItemCountCost();

    @Accessor("repairItemCountCost")
    void ott$setRepairItemCountCost(int repairItemCountCost);

    @Accessor("cost")
    net.minecraft.world.inventory.DataSlot ott$getCost();
}
