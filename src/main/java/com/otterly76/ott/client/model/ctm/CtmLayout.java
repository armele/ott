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
     * Good for block-types that just need corner/edge/centre differentiation
     * without diagonal seams.
     */
    SIMPLE(4, 4) {
        @Override public int[] tile(int mask) {
            int idx = simpleIdx(mask);
            return new int[]{ SIMPLE_X[idx], SIMPLE_Y[idx] };
        }
    },

    /**
     * 5×1 atlas (80×16 px), all 8 neighbours used for diagonal awareness,
     * but only 5 visual patterns: isolated, full-with-corners, vertical,
     * horizontal, full-without-corners.
     */
    COMPACT(5, 1) {
        @Override public int[] tile(int mask) {
            return new int[]{ COMPACT_X[mask & 0xFF], 0 };
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

    // ---- COMPACT --------------------------------------------------------------
    // 256-entry table built at class init from Fusion's CompactLayoutHandler logic.
    // Tile indices: 0=isolated, 1=full+corners, 2=vertical, 3=horizontal, 4=full-no-corners

    private static final int[] COMPACT_X = buildCompact();

    private static int[] buildCompact() {
        int[] t = new int[256];
        for (int mask = 0; mask < 256; mask++) {
            boolean top         = (mask & 1)   != 0;
            boolean topRight    = (mask & 2)   != 0;
            boolean right       = (mask & 4)   != 0;
            boolean bottomRight = (mask & 8)   != 0;
            boolean bottom      = (mask & 16)  != 0;
            boolean bottomLeft  = (mask & 32)  != 0;
            boolean left        = (mask & 64)  != 0;
            boolean topLeft     = (mask & 128) != 0;

            int sides = (top ? 1 : 0) + (right ? 1 : 0) + (bottom ? 1 : 0) + (left ? 1 : 0);

            if (sides <= 1) {
                t[mask] = 0;
            } else if (sides == 2) {
                if (left && right)       t[mask] = 3;  // horizontal run
                else if (top && bottom)  t[mask] = 2;  // vertical run
                else                     t[mask] = 0;  // corner — no dedicated compact tile
            } else if (sides == 3) {
                if (left && right) {
                    // missing top or bottom; show horizontal only if the missing row is fully connected
                    t[mask] = ((topLeft && top && topRight) || (bottomLeft && bottom && bottomRight)) ? 3 : 0;
                } else {
                    // top && bottom; missing left or right; show vertical only if the missing column is fully connected
                    t[mask] = ((topLeft && left && bottomLeft) || (topRight && right && bottomRight)) ? 2 : 0;
                }
            } else { // sides == 4
                if (topLeft && topRight && bottomLeft && bottomRight)           t[mask] = 1; // all corners
                else if (!topLeft && !topRight && !bottomLeft && !bottomRight)  t[mask] = 4; // no corners
                else                                                             t[mask] = 0; // mixed
            }
        }
        return t;
    }

    // ---- factory --------------------------------------------------------------

    /** Parses a layout id string; defaults to {@link #FULL} for unknown/absent values. */
    public static CtmLayout fromId(String id) {
        return switch (id.toLowerCase()) {
            case "simple"     -> SIMPLE;
            case "compact"    -> COMPACT;
            case "horizontal" -> HORIZONTAL;
            case "vertical"   -> VERTICAL;
            default           -> FULL;
        };
    }
}
