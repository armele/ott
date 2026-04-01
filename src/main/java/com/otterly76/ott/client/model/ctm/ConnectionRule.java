package com.otterly76.ott.client.model.ctm;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Determines whether a neighbor block "connects" for CTM purposes.
 */
public interface ConnectionRule {

    boolean connects(BlockAndTintGetter level, BlockPos selfPos, BlockState selfState, BlockPos neighborPos);

    // ---------- built-in rule types ----------

    /** Connects when neighbor is the same block as self. */
    record IsSameBlock() implements ConnectionRule {
        @Override
        public boolean connects(BlockAndTintGetter level, BlockPos selfPos, BlockState selfState, BlockPos neighborPos) {
            return level.getBlockState(neighborPos).is(selfState.getBlock());
        }
    }

    /** Connects when neighbor matches a specific block. */
    record MatchBlock(Block block) implements ConnectionRule {
        @Override
        public boolean connects(BlockAndTintGetter level, BlockPos selfPos, BlockState selfState, BlockPos neighborPos) {
            return level.getBlockState(neighborPos).is(block);
        }
    }

    /**
     * Combines multiple rules with OR logic — connects if ANY rule matches.
     */
    record AnyOf(ConnectionRule[] rules) implements ConnectionRule {
        @Override
        public boolean connects(BlockAndTintGetter level, BlockPos selfPos, BlockState selfState, BlockPos neighborPos) {
            for (ConnectionRule rule : rules) {
                if (rule.connects(level, selfPos, selfState, neighborPos)) return true;
            }
            return false;
        }
    }
}
