package com.otterly76.ott.client.model.ctm;

/**
 * Lookup table for the CTM "full" layout (8×6 tile grid).
 * <p>The 8-bit connection mask uses:
 * bit 0 (1) = top, bit 1 (2) = top-right (only when top AND right connect),
 * bit 2 (4) = right, bit 3 (8) = bottom-right (only when right AND bottom connect),
 * bit 4 (16) = bottom, bit 5 (32) = bottom-left (only when bottom AND left connect),
 * bit 6 (64) = left, bit 7 (128) = top-left (only when left AND top connect).
 * <p>TILE_X[mask] and TILE_Y[mask] give the column (0–7) and row (0–5)
 * of the correct tile in the 128×128 CTM atlas PNG.
 * Generated from Fusion's FullLayoutHandler via reflection dump.
 */
public final class FullLayoutLookup {

    private FullLayoutLookup() {}

    // 256-entry arrays: index = 8-bit connection mask
    public static final byte[] TILE_X = {
        0, 0, 0, 0, 1, 4, 1, 1, 0, 0, 0, 0, 1, 4, 1, 1,
        0, 0, 0, 0, 4, 6, 4, 4, 0, 0, 0, 0, 1, 6, 1, 1,
        0, 0, 0, 0, 1, 4, 1, 1, 0, 0, 0, 0, 1, 4, 1, 1,
        0, 0, 0, 0, 4, 6, 4, 4, 0, 0, 0, 0, 1, 6, 1, 1,
        3, 5, 3, 5, 2, 6, 2, 6, 3, 5, 3, 5, 2, 6, 2, 6,
        5, 7, 5, 7, 7, 1, 7, 4, 5, 7, 5, 7, 5, 4, 5, 2,
        3, 5, 3, 5, 2, 6, 2, 6, 3, 5, 3, 5, 2, 6, 2, 6,
        3, 5, 3, 5, 7, 5, 7, 0, 3, 5, 3, 5, 2, 3, 2, 7,
        0, 0, 0, 0, 1, 4, 1, 1, 0, 0, 0, 0, 1, 4, 1, 1,
        0, 0, 0, 0, 4, 6, 4, 4, 0, 0, 0, 0, 1, 6, 1, 1,
        0, 0, 0, 0, 1, 4, 1, 1, 0, 0, 0, 0, 1, 4, 1, 1,
        0, 0, 0, 0, 4, 6, 4, 4, 0, 0, 0, 0, 1, 6, 1, 1,
        3, 3, 3, 3, 2, 4, 2, 2, 3, 3, 3, 3, 2, 4, 2, 2,
        5, 7, 5, 7, 7, 5, 7, 2, 5, 7, 5, 7, 5, 0, 5, 7,
        3, 3, 3, 3, 2, 4, 2, 2, 3, 3, 3, 3, 2, 4, 2, 2,
        3, 3, 3, 3, 7, 3, 7, 6, 3, 3, 3, 3, 2, 6, 2, 2
    };

    public static final byte[] TILE_Y = {
        0, 3, 0, 3, 0, 1, 0, 3, 0, 3, 0, 3, 0, 1, 0, 3,
        1, 2, 1, 2, 0, 0, 0, 2, 1, 2, 1, 2, 1, 2, 1, 2,
        0, 3, 0, 3, 0, 1, 0, 3, 0, 3, 0, 3, 0, 1, 0, 3,
        1, 2, 1, 2, 0, 0, 0, 2, 1, 2, 1, 2, 1, 2, 1, 2,
        0, 1, 0, 1, 0, 1, 0, 3, 0, 1, 0, 1, 0, 1, 0, 3,
        0, 1, 0, 1, 0, 4, 0, 5, 0, 1, 0, 1, 2, 4, 2, 4,
        0, 1, 0, 1, 0, 1, 0, 3, 0, 1, 0, 1, 0, 1, 0, 3,
        1, 3, 1, 3, 2, 4, 2, 4, 1, 3, 1, 3, 1, 4, 1, 5,
        0, 3, 0, 3, 0, 1, 0, 3, 0, 3, 0, 3, 0, 1, 0, 3,
        1, 2, 1, 2, 0, 0, 0, 2, 1, 2, 1, 2, 1, 2, 1, 2,
        0, 3, 0, 3, 0, 1, 0, 3, 0, 3, 0, 3, 0, 1, 0, 3,
        1, 2, 1, 2, 0, 0, 0, 2, 1, 2, 1, 2, 1, 2, 1, 2,
        0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3,
        0, 3, 0, 3, 0, 5, 0, 5, 0, 3, 0, 3, 2, 5, 2, 4,
        0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3,
        1, 2, 1, 2, 2, 5, 2, 4, 1, 2, 1, 2, 1, 5, 1, 2
    };

    /** Number of tile columns in the atlas (8 tiles × 16px = 128px wide). */
    public static final int TILES_WIDE = 8;
    /** Number of tile rows in the atlas (8 tiles × 16px = 128px tall). */
    public static final int TILES_TALL = 8;
}
