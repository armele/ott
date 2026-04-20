package com.otterly76.ott.client.model.ctm;

/**
 * CTM atlas layout types. Each layout defines how a neighbour-mask maps to
 * a tile coordinate within its texture atlas, and how many tiles wide/tall
 * that atlas is.
 *
 * <p>Our 8-bit mask encoding (matches {@link ConnectingBakedModel}):
 * bit 0=T, bit 1=TR, bit 2=R, bit 3=BR, bit 4=B, bit 5=BL, bit 6=L, bit 7=TL
 */
public enum CtmLayout {

    // ---- layout types ---------------------------------------------------------

    /** 8×8 atlas (128×128 px), all 8 neighbours, 256 combinations. Default. */
    FULL(8, 8) {
        @Override public int[] tile(int mask) {
            int m = mask & 0xFF;
            return new int[]{ FullLayoutLookup.TILE_X[m], FullLayoutLookup.TILE_Y[m] };
        }
    },

    /**
     * 4×4 atlas (64×64 px), 4 cardinal neighbours only, 16 combinations.
     * Handles all corner, edge, T-junction, and centre patterns.
     */
    SIMPLE(4, 4) {
        @Override public int[] tile(int mask) {
            int idx = simpleIdx(mask);
            return new int[]{ SIMPLE_X[idx], SIMPLE_Y[idx] };
        }
    },

    /**
     * 4×1 atlas (64×16 px), left + right neighbours only.
     * Tiles: isolated | left-end | centre | right-end.
     */
    HORIZONTAL(4, 1) {
        @Override public int[] tile(int mask) {
            // left=bit6, right=bit2
            int idx = ((mask >> 6) & 1) | (((mask >> 2) & 1) << 1);
            return new int[]{ HORIZ_X[idx], 0 };
        }
    },

    /**
     * 1×4 atlas (16×64 px), top + bottom neighbours only.
     * Tiles: isolated | bottom-end | centre | top-end.
     */
    VERTICAL(1, 4) {
        @Override public int[] tile(int mask) {
            // top=bit0, bottom=bit4
            int idx = (mask & 1) | (((mask >> 4) & 1) << 1);
            return new int[]{ 0, VERT_Y[idx] };
        }
    };

    // ---- dimensions -----------------------------------------------------------

    /** Number of tiles across the atlas horizontally. */
    public final int tilesWide;
    /** Number of tiles down the atlas vertically. */
    public final int tilesHigh;

    CtmLayout(int tilesWide, int tilesHigh) {
        this.tilesWide = tilesWide;
        this.tilesHigh = tilesHigh;
    }

    /**
     * Returns {tileX, tileY} for the given 8-bit neighbour mask.
     * Tile [0,0] is the top-left (isolated / no-connections) tile.
     */
    public abstract int[] tile(int mask);

    // ---- SIMPLE ---------------------------------------------------------------
    // 4-bit index: bit0=T, bit1=R, bit2=B, bit3=L  (cardinals extracted from 8-bit mask)

    private static int simpleIdx(int mask8) {
        return (mask8 & 1) | ((mask8 >> 2 & 1) << 1) | ((mask8 >> 4 & 1) << 2) | ((mask8 >> 6 & 1) << 3);
    }

    //                         0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15
    // idx key: none, T, R, TR, B, TB, RB, TRB, L, TL, LR, TRL, BL, TBL, RBL, all
    private static final int[] SIMPLE_X = { 0, 3, 2, 2, 2, 1, 2, 0, 3, 3, 0, 0, 3, 1, 1, 1 };
    private static final int[] SIMPLE_Y = { 0, 1, 1, 3, 0, 1, 2, 2, 0, 3, 1, 3, 2, 2, 3, 0 };

    // ---- HORIZONTAL -----------------------------------------------------------
    // 2-bit index: bit0=L, bit1=R

    //                          0  1  2  3   (none, L, R, L+R)
    private static final int[] HORIZ_X = { 0, 3, 1, 2 };

    // ---- VERTICAL -------------------------------------------------------------
    // 2-bit index: bit0=T, bit1=B

    //                         0  1  2  3   (none, T, B, T+B)
    private static final int[] VERT_Y = { 0, 3, 1, 2 };

    // ---- factory --------------------------------------------------------------

    /** Parses a layout id string; defaults to {@link #FULL} for unknown/absent values. */
    public static CtmLayout fromId(String id) {
        return switch (id.toLowerCase()) {
            case "simple"     -> SIMPLE;
            case "horizontal" -> HORIZONTAL;
            case "vertical"   -> VERTICAL;
            default           -> FULL;
        };
    }
}
