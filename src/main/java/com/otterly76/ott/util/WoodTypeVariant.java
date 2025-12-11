package com.otterly76.ott.util;

import net.minecraft.world.level.block.state.properties.WoodType;

public enum WoodTypeVariant {
    PALE_OAK(WoodType.register(new WoodType("pale_oak", BlockSetTypeVariant.PALE_OAK.getBlockSetType())));

    private final WoodType woodType;

    WoodTypeVariant(WoodType woodType) {
        this.woodType = woodType;
    }

    public WoodType getWoodType() {
        return woodType;
    }
}