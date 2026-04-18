package com.otterly76.ott.client.model.ctm;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Runtime model for the simplified 4-tile CTM system.
 *
 * <p>Per chunk section rebuild, {@link #getModelData} evaluates each face's 4 sub-quadrants
 * (TL, TR, BL, BR) against their 3 relevant neighbours and records a sprite index (0–3)
 * for each.  {@link #getQuads} then returns the 4 pre-baked quads looked up from those indices.
 *
 * <h3>Sprite index semantics</h3>
 * <ul>
 *   <li>0 — clean (used for both isolated and fully-interior sub-quadrants)</li>
 *   <li>1 — vertical seam (only the "first" / top-bottom neighbour connects)</li>
 *   <li>2 — horizontal seam (only the "second" / left-right neighbour connects)</li>
 *   <li>3 — L-corner seam (both adjacent cardinals connect but diagonal does not)</li>
 * </ul>
 */
public class SimpleCTMBakedModel extends BakedModelWrapper<net.minecraft.client.resources.model.BakedModel> {

    /** 24-byte array: spriteIndex for [face.ordinal() * 4 + quadrant]. */
    public static final ModelProperty<byte[]> CTM_DATA = new ModelProperty<>();

    // ── Neighbour offset tables (mirrors ConnectingBakedModel) ─────────────

    /** For each face, 8 neighbour offsets: T, TR, R, BR, B, BL, L, TL. */
    private static final BlockPos[][] NEIGHBOR_OFFSETS;

    static {
        NEIGHBOR_OFFSETS = new BlockPos[6][8];
        buildOffsets(Direction.UP,    Direction.NORTH, Direction.EAST);
        buildOffsets(Direction.DOWN,  Direction.SOUTH, Direction.EAST);
        buildOffsets(Direction.NORTH, Direction.UP,    Direction.EAST);
        buildOffsets(Direction.SOUTH, Direction.UP,    Direction.WEST);
        buildOffsets(Direction.WEST,  Direction.UP,    Direction.SOUTH);
        buildOffsets(Direction.EAST,  Direction.UP,    Direction.NORTH);
    }

    private static void buildOffsets(Direction face, Direction topDir, Direction rightDir) {
        Direction botDir  = topDir.getOpposite();
        Direction leftDir = rightDir.getOpposite();
        BlockPos top   = BlockPos.ZERO.relative(topDir);
        BlockPos right = BlockPos.ZERO.relative(rightDir);
        BlockPos bot   = BlockPos.ZERO.relative(botDir);
        BlockPos left  = BlockPos.ZERO.relative(leftDir);
        int fi = face.ordinal();
        NEIGHBOR_OFFSETS[fi][0] = top;
        NEIGHBOR_OFFSETS[fi][1] = top.offset(right);
        NEIGHBOR_OFFSETS[fi][2] = right;
        NEIGHBOR_OFFSETS[fi][3] = bot.offset(right);
        NEIGHBOR_OFFSETS[fi][4] = bot;
        NEIGHBOR_OFFSETS[fi][5] = bot.offset(left);
        NEIGHBOR_OFFSETS[fi][6] = left;
        NEIGHBOR_OFFSETS[fi][7] = top.offset(left);
    }

    /**
     * Which neighbour offset indices to use for each quadrant.
     * Outer index: 0 = standard faces (UP, DOWN, WEST, EAST),
     *              1 = flipped faces  (NORTH, SOUTH).
     * Inner index: quadrant 0–3 = TL, TR, BL, BR.
     * Triplet: [firstIdx, secondIdx, diagIdx] into NEIGHBOR_OFFSETS[face].
     */
    private static final int[][][] QUAD_NEIGHBOR_IDX = {
        // Standard (no horizontal flip)
        { {0, 6, 7}, {0, 2, 1}, {4, 6, 5}, {4, 2, 3} },
        // Flipped (NORTH / SOUTH — L and R are texture-swapped)
        { {0, 2, 1}, {0, 6, 7}, {4, 2, 3}, {4, 6, 5} },
    };

    /** NORTH and SOUTH faces have their texture left/right mirrored vs our offsets. */
    private static final boolean[] FLIP_H = new boolean[6];
    static {
        FLIP_H[Direction.NORTH.ordinal()] = true;
        FLIP_H[Direction.SOUTH.ordinal()] = true;
    }

    // ── Fields ─────────────────────────────────────────────────────────────

    /** Pre-baked quads: [face.ordinal()][quadrant 0-3 = TL/TR/BL/BR][sprite 0-3]. */
    private final BakedQuad[][][] prebakedQuads;
    private final ConnectionRule rule;

    public SimpleCTMBakedModel(net.minecraft.client.resources.model.BakedModel base,
                                BakedQuad[][][] prebakedQuads,
                                ConnectionRule rule) {
        super(base);
        this.prebakedQuads = prebakedQuads;
        this.rule = rule;
    }

    // ── ModelData (called once per chunk section rebuild) ──────────────────

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level,
                                           @NotNull BlockPos pos,
                                           @NotNull BlockState state,
                                           @NotNull ModelData existing) {
        byte[] data = new byte[6 * 4]; // face × quadrant
        for (Direction face : Direction.values()) {
            int fi = face.ordinal();
            int flip = FLIP_H[fi] ? 1 : 0;
            BlockPos[] offsets = NEIGHBOR_OFFSETS[fi];
            int[][] qni = QUAD_NEIGHBOR_IDX[flip];

            for (int q = 0; q < 4; q++) {
                int firstIdx  = qni[q][0];
                int secondIdx = qni[q][1];
                int diagIdx   = qni[q][2];

                boolean first  = rule.connects(level, pos, state, pos.offset(offsets[firstIdx]));
                boolean second = rule.connects(level, pos, state, pos.offset(offsets[secondIdx]));
                boolean diag   = first && second
                        && rule.connects(level, pos, state, pos.offset(offsets[diagIdx]));

                data[fi * 4 + q] = spriteIndex(first, second, diag);
            }
        }
        return existing.derive().with(CTM_DATA, data).build();
    }

    /**
     * Maps three neighbour connection booleans to a sprite index.
     * <ul>
     *   <li>0 — clean (isolated OR fully interior)</li>
     *   <li>1 — vertical seam</li>
     *   <li>2 — horizontal seam</li>
     *   <li>3 — L-corner seam</li>
     * </ul>
     */
    private static byte spriteIndex(boolean first, boolean second, boolean diag) {
        if (first && second) return diag ? (byte) 0 : (byte) 3;
        if (first)  return 1;
        if (second) return 2;
        return 0;
    }

    // ── getQuads (returns 4 pre-baked sub-quads for the requested face) ────

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state,
                                             @Nullable Direction side,
                                             @NotNull RandomSource rand,
                                             @NotNull ModelData data,
                                             @Nullable RenderType renderType) {
        if (side == null) return List.of(); // full cube; no unculled quads

        byte[] ctmData = data.get(CTM_DATA);
        if (ctmData == null) {
            // Fallback: no CTM data yet (e.g. item rendering or first frame)
            return originalModel.getQuads(state, side, rand, data, renderType);
        }

        int fi = side.ordinal();
        List<BakedQuad> result = new ArrayList<>(4);
        for (int q = 0; q < 4; q++) {
            int spriteIdx = ctmData[fi * 4 + q] & 0xFF;
            BakedQuad quad = prebakedQuads[fi][q][spriteIdx];
            if (quad != null) result.add(quad);
        }
        return result;
    }
}
