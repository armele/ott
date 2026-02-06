package com.otterly76.ott.util;

import net.minecraft.world.level.block.state.properties.BlockSetType;

public enum BlockSetTypeVariant {
    PALE_OAK(BlockSetType.register(new BlockSetType("pale_oak")));

    private final BlockSetType blockSetType;

    BlockSetTypeVariant(BlockSetType blockSetType) {
        this.blockSetType = blockSetType;
    }

    public BlockSetType getBlockSetType() {
        return blockSetType;
    }
}
