package com.otterly76.ott.client.model.overlay;

/**
 * Pre-computed tile lookup table for the 6×3 overlay atlas.
 *
 * <p>Each face is decomposed into four full-face quads (corners 0–3).
 * For each (corner, 8-bit connection mask) pair, {@link #getTile} returns a
 * packed int encoding the tile position, or {@code -1} if that corner quad
 * should be discarded (no overlay needed there).
 *
 * <p>Packed tile format: {@code (tileY << 4) | tileX}.
 * Decode with {@code tileX = tile & 0xF} and {@code tileY = (tile >> 4) & 0xF}.
 *
 * <p>8-bit mask bit layout (matches {@link OverlayBakedModel}):
 * <pre>
 *   bit 0 = T, 1 = TR, 2 = R, 3 = BR, 4 = B, 5 = BL, 6 = L, 7 = TL
 * </pre>
 *
 * <p>Direct port of Fusion's {@code OverlayLayoutHandler.getTilePos}.
 */
public final class OverlayLayout {

    /** Number of tile columns in the overlay atlas. */
    public static final int TILES_WIDE = 6;
    /** Number of tile rows in the overlay atlas. */
    public static final int TILES_HIGH = 3;

    /**
     * Pre-computed tile positions, packed as {@code (tileY << 4) | tileX}, or {@code -1}.
     * Index: {@code corner * 256 + mask}.
     */
    private static final int[] TILES = new int[4 * 256];

    static {
        for (int mask = 0; mask < 256; mask++) {
            for (int corner = 0; corner < 4; corner++) {
                TILES[corner * 256 + mask] = computeTile(corner, mask);
            }
        }
    }

    private OverlayLayout() {}

    /**
     * Returns the packed tile position {@code (tileY << 4) | tileX} for the given corner and
     * connection mask, or {@code -1} if this corner quad should be discarded.
     *
     * @param corner 0–3 (the four sub-quads of a face)
     * @param mask   8-bit connection mask
     */
    public static int getTile(int corner, int mask) {
        return TILES[corner * 256 + (mask & 0xFF)];
    }

    private static int pack(int x, int y) {
        return (y << 4) | x;
    }

    // ---- ported from Fusion's OverlayLayoutHandler.getTilePos ---------------

    @SuppressWarnings("DuplicatedCode")
    private static int computeTile(int corner, int mask) {
        boolean t  = (mask & 1)   != 0;
        boolean tr = (mask & 2)   != 0;
        boolean r  = (mask & 4)   != 0;
        boolean br = (mask & 8)   != 0;
        boolean b  = (mask & 16)  != 0;
        boolean bl = (mask & 32)  != 0;
        boolean l  = (mask & 64)  != 0;
        boolean tl = (mask & 128) != 0;

        // All four cardinals connected → only corner 0 draws (centre-of-full-surround tile)
        if (t && r && b && l) {
            return corner == 0 ? pack(4, 1) : -1;
        }
        // Nothing connected → nothing to draw
        if (!t && !tr && !r && !br && !b && !bl && !l && !tl) {
            return -1;
        }

        return switch (corner) {
            case 0 -> {
                if (l)  yield -1;
                if (!t) yield tl ? pack(2, 2) : -1;
                if (!r) yield pack(1, 2);
                if (!b) yield pack(3, 2);
                yield pack(3, 1);
            }
            case 1 -> {
                if (t)  yield -1;
                if (!r) yield tr ? pack(0, 2) : -1;
                if (!b) yield pack(0, 1);
                if (!l) yield pack(3, 0);
                yield pack(4, 0);
            }
            case 2 -> {
                if (r)  yield -1;
                if (!b) yield br ? pack(0, 0) : -1;
                if (!l) yield pack(1, 0);
                if (!t) yield pack(5, 0);
                yield pack(5, 1);
            }
            default -> { // corner 3
                if (b)  yield -1;
                if (!l) yield bl ? pack(2, 0) : -1;
                if (!t) yield pack(2, 1);
                if (!r) yield pack(5, 2);
                yield pack(4, 2);
            }
        };
    }
}
