package com.otterly76.ott.util;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum WoodTypeVariant {
    PALE_OAK(WoodType.register(new WoodType("minecraft:pale_oak", BlockSetTypeVariant.PALE_OAK.getBlockSetType())));

    private final WoodType woodType;

    WoodTypeVariant(WoodType woodType) {
        this.woodType = woodType;
    }

    public WoodType getWoodType() {
        return woodType;
    }

    private static final Map<String, WoodType> OTT_WOOD_TYPES = new ConcurrentHashMap<>();

    public static WoodType ott(String setName) {
        return OTT_WOOD_TYPES.computeIfAbsent(setName, name ->
                WoodType.register(new WoodType("ott:" + name, BlockSetType.OAK))
        );
    }
}
