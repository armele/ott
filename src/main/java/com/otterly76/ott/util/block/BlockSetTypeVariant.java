package com.otterly76.ott.util.block;

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

    private static final java.util.Map<String, BlockSetType> OTT_BLOCK_SET_TYPES = new java.util.concurrent.ConcurrentHashMap<>();

    public static BlockSetType ott(String setName) {
        return OTT_BLOCK_SET_TYPES.computeIfAbsent(setName, name ->
                BlockSetType.register(new BlockSetType(name))
        );
    }
}