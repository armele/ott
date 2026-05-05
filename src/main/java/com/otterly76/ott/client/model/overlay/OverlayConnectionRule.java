package com.otterly76.ott.client.model.overlay;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Predicate determining whether a given face should connect (show overlay) toward a specific neighbour.
 * Evaluated per-neighbour during {@link OverlayBakedModel#getModelData}.
 *
 * <p>Parameters:
 * <ul>
 *   <li>{@code level} – block access
 *   <li>{@code ownPos} – position of the block being rendered
 *   <li>{@code ownState} – state of the block being rendered
 *   <li>{@code face} – the face direction being evaluated
 *   <li>{@code neighborPos} – neighbour position in texture-space (T/TR/R/BR/B/BL/L/TL)
 * </ul>
 *
 * <p>"In front" of a neighbour (used by {@link MatchBlockInFront}) is
 * {@code neighborPos.relative(face)} — one step further in the face direction.
 */
public interface OverlayConnectionRule {

    boolean connects(BlockAndTintGetter level, BlockPos ownPos, BlockState ownState,
                     Direction face, BlockPos neighborPos);

    // ---- built-in implementations ----------------------------------------

    /** Connects if the neighbour block IS the given block. */
    record MatchBlock(Block block) implements OverlayConnectionRule {
        @Override
        public boolean connects(BlockAndTintGetter level, BlockPos ownPos, BlockState ownState,
                                Direction face, BlockPos neighborPos) {
            return level.getBlockState(neighborPos).is(block);
        }
    }

    /**
     * Connects if the block <em>in front of</em> the neighbour
     * (at {@code neighborPos.relative(face)}) is the given block.
     * Handles diagonal/elevated overflow scenarios.
     */
    record MatchBlockInFront(Block block) implements OverlayConnectionRule {
        @Override
        public boolean connects(BlockAndTintGetter level, BlockPos ownPos, BlockState ownState,
                                Direction face, BlockPos neighborPos) {
            return level.getBlockState(neighborPos.relative(face)).is(block);
        }
    }

    /**
     * Connects if the face of the neighbour block toward {@code face} is visible —
     * i.e. the block directly in front of the neighbour does not fully occlude it.
     */
    enum IsFaceVisible implements OverlayConnectionRule {
        INSTANCE;

        @Override
        public boolean connects(BlockAndTintGetter level, BlockPos ownPos, BlockState ownState,
                                Direction face, BlockPos neighborPos) {
            BlockState inFront = level.getBlockState(neighborPos.relative(face));
            return !inFront.canOcclude();
        }
    }

    /** Connects only if ALL child rules connect. */
    record And(OverlayConnectionRule[] rules) implements OverlayConnectionRule {
        @Override
        public boolean connects(BlockAndTintGetter level, BlockPos ownPos, BlockState ownState,
                                Direction face, BlockPos neighborPos) {
            for (OverlayConnectionRule rule : rules) {
                if (!rule.connects(level, ownPos, ownState, face, neighborPos)) return false;
            }
            return true;
        }
    }

    /** Connects if ANY child rule connects. */
    record Or(OverlayConnectionRule[] rules) implements OverlayConnectionRule {
        @Override
        public boolean connects(BlockAndTintGetter level, BlockPos ownPos, BlockState ownState,
                                Direction face, BlockPos neighborPos) {
            for (OverlayConnectionRule rule : rules) {
                if (rule.connects(level, ownPos, ownState, face, neighborPos)) return true;
            }
            return false;
        }
    }
}
