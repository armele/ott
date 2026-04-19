package com.otterly76.ott.client.model.ctm;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Baked model for CTM "connecting" blocks. Wraps the base block model and remaps
 * quad UVs on the fly based on which neighbors connect.
 *
 * <p>Connection data is computed in {@link #getModelData} (called once per chunk section
 * rebuild) and stored in {@link ModelData} for retrieval in {@link #getQuads}.
 */
public class ConnectingBakedModel extends BakedModelWrapper<net.minecraft.client.resources.model.BakedModel> {

    /**
     * Stores per-rule connection masks: {@code masks[ruleIndex][face.ordinal()]} = 8-bit mask.
     */
    public static final ModelProperty<int[][]> CTM_MASKS = new ModelProperty<>();

    // ---- face-to-neighbor-offset tables ----------------------------------------

    /**
     * For each face direction, 8 neighbor offsets in texture-space order:
     * T, TR, R, BR, B, BL, L, TL.
     */
    private static final BlockPos[][] NEIGHBOR_OFFSETS = buildNeighborOffsets();

    private static BlockPos[][] buildNeighborOffsets() {
        BlockPos[][] offsets = new BlockPos[6][8];
        // Horizontal faces
        buildOffsets(offsets, Direction.UP,    Direction.NORTH, Direction.EAST);
        buildOffsets(offsets, Direction.DOWN,  Direction.SOUTH, Direction.EAST);
        // Vertical faces — "top" = UP; "right" defined by which side is rightward when
        // viewed from outside the block
        buildOffsets(offsets, Direction.NORTH, Direction.UP, Direction.EAST);
        buildOffsets(offsets, Direction.SOUTH, Direction.UP, Direction.WEST);
        buildOffsets(offsets, Direction.WEST,  Direction.UP, Direction.SOUTH);
        buildOffsets(offsets, Direction.EAST,  Direction.UP, Direction.NORTH);
        return offsets;
    }

    private static void buildOffsets(BlockPos[][] offsets, Direction face, Direction topDir, Direction rightDir) {
        Direction botDir  = topDir.getOpposite();
        Direction leftDir = rightDir.getOpposite();

        BlockPos top   = BlockPos.ZERO.relative(topDir);
        BlockPos right = BlockPos.ZERO.relative(rightDir);
        BlockPos bot   = BlockPos.ZERO.relative(botDir);
        BlockPos left  = BlockPos.ZERO.relative(leftDir);

        int fi = face.ordinal();
        offsets[fi][0] = top;
        offsets[fi][1] = top.offset(right);
        offsets[fi][2] = right;
        offsets[fi][3] = bot.offset(right);
        offsets[fi][4] = bot;
        offsets[fi][5] = bot.offset(left);
        offsets[fi][6] = left;
        offsets[fi][7] = top.offset(left);
    }

    // ---- fields ----------------------------------------------------------------

    /**
     * Unique rules in order, parallel to the first axis of the masks array.
     */
    private final List<ConnectionRule> ruleList;
    /**
     * Maps each sprite to its rule index within {@link #ruleList}.
     */
    private final Map<TextureAtlasSprite, Integer> spriteToRuleIndex;
    /** Catch-all rule index (used when sprite not found in spriteToRuleIndex), or -1. */
    private final int catchAllRuleIndex;
    /** Atlas layout for this model — controls tile lookup and tile dimensions. */
    private final CtmLayout layout;
    /**
     * Maps CTM atlas sprite → isolated tile sprite.
     * When Domum Ornamentum calls {@link #getQuads}, we return quads using the isolated
     * sprite so DO's {@code ModelSpriteQuadTransformer} maps to just the isolated tile
     * instead of the full 128×128 atlas.
     */
    private final Map<TextureAtlasSprite, TextureAtlasSprite> ctmToIsolated;

    public ConnectingBakedModel(net.minecraft.client.resources.model.BakedModel wrapped,
                                Map<TextureAtlasSprite, ConnectionRule> spriteRules) {
        this(wrapped, spriteRules, Map.of(), CtmLayout.FULL);
    }

    public ConnectingBakedModel(net.minecraft.client.resources.model.BakedModel wrapped,
                                Map<TextureAtlasSprite, ConnectionRule> spriteRules,
                                Map<TextureAtlasSprite, TextureAtlasSprite> ctmToIsolated) {
        this(wrapped, spriteRules, ctmToIsolated, CtmLayout.FULL);
    }

    public ConnectingBakedModel(net.minecraft.client.resources.model.BakedModel wrapped,
                                Map<TextureAtlasSprite, ConnectionRule> spriteRules,
                                Map<TextureAtlasSprite, TextureAtlasSprite> ctmToIsolated,
                                CtmLayout layout) {
        super(wrapped);
        this.layout = layout;

        // Build a deduplicated rule list using identity (rules are stateless value objects)
        Map<ConnectionRule, Integer> ruleIndex = new IdentityHashMap<>();
        List<ConnectionRule> list = new ArrayList<>();
        int catchAll = -1;

        for (Map.Entry<TextureAtlasSprite, ConnectionRule> e : spriteRules.entrySet()) {
            ConnectionRule rule = e.getValue();
            if (!ruleIndex.containsKey(rule)) {
                ruleIndex.put(rule, list.size());
                list.add(rule);
            }
            if (e.getKey() == null) {
                catchAll = ruleIndex.get(rule);
            }
        }

        this.ruleList = list;
        this.catchAllRuleIndex = catchAll;

        Map<TextureAtlasSprite, Integer> s2r = new IdentityHashMap<>();
        for (Map.Entry<TextureAtlasSprite, ConnectionRule> e : spriteRules.entrySet()) {
            if (e.getKey() != null) {
                s2r.put(e.getKey(), ruleIndex.get(e.getValue()));
            }
        }
        this.spriteToRuleIndex = s2r;
        this.ctmToIsolated = new IdentityHashMap<>(ctmToIsolated);
    }

    // ---- ModelData (called per chunk rebuild, before getQuads) -----------------

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData existing) {
        int numRules = ruleList.size();
        if (numRules == 0) return existing;

        int[][] masks = new int[numRules][6];
        for (int ri = 0; ri < numRules; ri++) {
            for (Direction face : Direction.values()) {
                masks[ri][face.ordinal()] = computeMask(level, pos, state, face, ruleList.get(ri));
            }
        }
        return existing.derive().with(CTM_MASKS, masks).build();
    }

    private int computeMask(BlockAndTintGetter level, BlockPos pos, BlockState state,
                            Direction face, ConnectionRule rule) {
        BlockPos[] offsets = NEIGHBOR_OFFSETS[face.ordinal()];

        boolean t  = rule.connects(level, pos, state, pos.offset(offsets[0]));
        boolean r  = rule.connects(level, pos, state, pos.offset(offsets[2]));
        boolean b  = rule.connects(level, pos, state, pos.offset(offsets[4]));
        boolean l  = rule.connects(level, pos, state, pos.offset(offsets[6]));
        // Diagonals only "active" if both adjacent cardinals connect
        boolean tr = t && r && rule.connects(level, pos, state, pos.offset(offsets[1]));
        boolean br = b && r && rule.connects(level, pos, state, pos.offset(offsets[3]));
        boolean bl = b && l && rule.connects(level, pos, state, pos.offset(offsets[5]));
        boolean tl = t && l && rule.connects(level, pos, state, pos.offset(offsets[7]));

        return (t ? 1 : 0) | (tr ? 2 : 0) | (r ? 4 : 0) | (br ? 8 : 0)
             | (b ? 16 : 0) | (bl ? 32 : 0) | (l ? 64 : 0) | (tl ? 128 : 0);
    }

    // ---- getQuads (UV remapping) -----------------------------------------------

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                             @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        // When DO calls us to sample our texture, return quads using the isolated sprite.
        // DO's ModelSpriteQuadTransformer normalises vertex UVs relative to sprite.getU0/U1,
        // so it must receive a sprite that covers exactly the isolated tile — not the full
        // 128×128 CTM atlas.
        if (isCalledByDomumOrnamentum()) {
            List<BakedQuad> base = originalModel.getQuads(state, side, rand, data, renderType);
            if (ctmToIsolated.isEmpty()) return base;
            List<BakedQuad> isolated = new ArrayList<>(base.size());
            for (BakedQuad quad : base) {
                TextureAtlasSprite iso = ctmToIsolated.get(quad.getSprite());
                isolated.add(iso != null ? createIsolatedQuad(quad, iso) : quad);
            }
            return isolated;
        }

        List<BakedQuad> base = originalModel.getQuads(state, side, rand, data, renderType);

        int[][] masks = data.get(CTM_MASKS);
        if (masks == null || side == null || base.isEmpty()) return base;

        int faceOrdinal = side.ordinal();
        List<BakedQuad> result = new ArrayList<>(base.size());
        for (BakedQuad quad : base) {
            result.add(remapQuad(quad, masks, faceOrdinal));
        }
        return result;
    }

    /**
     * Faces whose Minecraft UV U-axis runs opposite to our neighbor-offset "right" convention.
     * For these faces, swap the L/R bits (and their diagonal pairs) before the tile lookup.
     */
    private static final boolean[] FLIP_H = new boolean[6];
    static {
        FLIP_H[Direction.NORTH.ordinal()] = true;
        FLIP_H[Direction.SOUTH.ordinal()] = true;
    }

    /**
     * Mirrors a CTM mask horizontally: swaps L↔R, TR↔TL, BR↔BL; T and B stay.
     * Bit layout: T=0, TR=1, R=2, BR=3, B=4, BL=5, L=6, TL=7
     */
    private static int flipMaskH(int m) {
        int t  = m & 1;
        int tr = (m >> 1) & 1;
        int r  = (m >> 2) & 1;
        int br = (m >> 3) & 1;
        int b  = (m >> 4) & 1;
        int bl = (m >> 5) & 1;
        int l  = (m >> 6) & 1;
        int tl = (m >> 7) & 1;
        return t | (tl << 1) | (l << 2) | (bl << 3) | (b << 4) | (br << 5) | (r << 6) | (tr << 7);
    }

    private BakedQuad remapQuad(BakedQuad quad, int[][] masks, int faceOrdinal) {
        TextureAtlasSprite sprite = quad.getSprite();

        // Find rule index for this sprite
        int ruleIdx = spriteToRuleIndex.getOrDefault(sprite, catchAllRuleIndex);
        if (ruleIdx < 0 || ruleIdx >= masks.length) return quad;

        int mask = masks[ruleIdx][faceOrdinal];
        if (FLIP_H[faceOrdinal]) mask = flipMaskH(mask);
        int[] tile = layout.tile(mask);
        int tileX = tile[0];
        int tileY = tile[1];

        // Tile [0,0] means "no connections" — input UV is already at tile [0,0], nothing to shift
        if (tileX == 0 && tileY == 0) return quad;

        int[] newVerts = Arrays.copyOf(quad.getVertices(), quad.getVertices().length);
        remapUV(newVerts, sprite, tileX, tileY);

        return new BakedQuad(newVerts, quad.getTintIndex(), quad.getDirection(),
                sprite, quad.isShade(), quad.hasAmbientOcclusion());
    }

    /**
     * Creates a copy of {@code quad} where the CTM atlas sprite is replaced by the
     * isolated tile sprite and vertex UVs are remapped to span the isolated sprite's
     * full extent.
     *
     * <p>DO's {@code ModelSpriteQuadTransformer} normalises vertex UVs as:
     * {@code u_rel = (u_vertex - source.getU0()) / (source.getU1() - source.getU0())}
     * and then maps to the target via {@code target.getU(u_rel)}.
     * For the retextured block to display only the isolated tile, the quad we hand to DO
     * must have {@code sprite == isolatedSprite} and vertex UVs spanning
     * {@code [iso.u0, iso.u1]} — so DO's normalisation gives {@code u_rel ∈ [0, 1]}
     * and maps to the full isolated sprite.
     */
    private BakedQuad createIsolatedQuad(BakedQuad quad, TextureAtlasSprite isolatedSprite) {
        TextureAtlasSprite ctmSprite = quad.getSprite();
        float ctmU0 = ctmSprite.getU0();
        float ctmV0 = ctmSprite.getV0();
        float tileW = (ctmSprite.getU1() - ctmU0) / layout.tilesWide;
        float tileH = (ctmSprite.getV1() - ctmV0) / layout.tilesHigh;

        float isoU0 = isolatedSprite.getU0();
        float isoU1 = isolatedSprite.getU1();
        float isoV0 = isolatedSprite.getV0();
        float isoV1 = isolatedSprite.getV1();

        int[] verts = Arrays.copyOf(quad.getVertices(), quad.getVertices().length);
        int stride = IQuadTransformer.STRIDE;
        int uvOffset = IQuadTransformer.UV0;

        for (int v = 0; v < 4; v++) {
            int base = v * stride + uvOffset;
            float u = Float.intBitsToFloat(verts[base]);
            float vv = Float.intBitsToFloat(verts[base + 1]);

            // Normalize within the first CTM tile: [ctmU0, ctmU0+tileW] → [0, 1]
            float uRel = (u - ctmU0) / tileW;
            float vRel = (vv - ctmV0) / tileH;

            // Remap to the full isolated sprite extent
            verts[base]     = Float.floatToRawIntBits(isoU0 + uRel * (isoU1 - isoU0));
            verts[base + 1] = Float.floatToRawIntBits(isoV0 + vRel * (isoV1 - isoV0));
        }

        return new BakedQuad(verts, quad.getTintIndex(), quad.getDirection(),
                isolatedSprite, quad.isShade(), quad.hasAmbientOcclusion());
    }

    private static boolean isCalledByDomumOrnamentum() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            String name = element.getClassName();
            if (name.contains("domumornamentum") || name.contains("ldbc")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remaps UV coordinates in-place from tile [0,0] to tile [tileX, tileY].
     */
    private void remapUV(int[] verts, TextureAtlasSprite sprite, int tileX, int tileY) {
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();
        float tileW = (u1 - u0) / layout.tilesWide;
        float tileH = (v1 - v0) / layout.tilesHigh;

        int stride = IQuadTransformer.STRIDE;
        int uvOffset = IQuadTransformer.UV0;

        for (int v = 0; v < 4; v++) {
            int base = v * stride + uvOffset;
            float u = Float.intBitsToFloat(verts[base]);
            float vv = Float.intBitsToFloat(verts[base + 1]);

            // The input UV already sits within tile [0,0] (first 16px of the sprite).
            // Shift it by tileX/tileY tile-widths to reach the target tile.
            verts[base]     = Float.floatToRawIntBits(u  + tileX * tileW);
            verts[base + 1] = Float.floatToRawIntBits(vv + tileY * tileH);
        }
    }
}
