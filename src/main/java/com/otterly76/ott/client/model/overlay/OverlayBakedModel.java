package com.otterly76.ott.client.model.overlay;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
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
 * Baked model for the terrain overlay system.
 *
 * <p>Wraps a vanilla-baked base model for geometry reference and emits up to
 * four overlay quads per face (one per corner) based on 8-bit connection masks
 * stored in {@link ModelData}.  UV coordinates are remapped to the appropriate
 * tile in the 6×3 overlay atlas.
 *
 * <p>This model returns only overlay (cutout) quads; it is intended to be
 * <em>appended</em> on top of a block's normal model by
 * {@link OverlayModifierBakedModel}.
 */
public class OverlayBakedModel implements net.minecraft.client.resources.model.BakedModel {

    /** ModelData property storing per-rule 8-bit connection masks for all 6 faces. */
    public static final ModelProperty<int[][]> OVERLAY_MASKS = new ModelProperty<>();

    /**
     * Tint index used on grass overlay quads.  A BlockColors handler must be registered
     * for all target blocks (dirt, sand, gravel, etc.) that returns the biome grass color
     * for this index.  Any unused tint index works; 100 is chosen to avoid vanilla conflicts.
     */
    public static final int GRASS_OVERLAY_TINT      = 100;
    public static final int WHITE_OPAL_OVERLAY_TINT  = 101;
    public static final int BLACK_OPAL_OVERLAY_TINT  = 102;
    public static final int FIRE_OPAL_OVERLAY_TINT   = 103;

    private static final ChunkRenderTypeSet CUTOUT = ChunkRenderTypeSet.of(RenderType.cutout());

    /**
     * Faces whose Minecraft UV U-axis runs opposite to our neighbour-offset "right" convention.
     * Matches the flip logic in {@link com.otterly76.ott.client.model.ctm.ConnectingBakedModel}.
     */
    private static final boolean[] FLIP_H = new boolean[6];

    static {
        FLIP_H[Direction.NORTH.ordinal()] = true;
        FLIP_H[Direction.SOUTH.ordinal()] = true;
    }

    /** Neighbour offsets in texture space (T, TR, R, BR, B, BL, L, TL) for each face direction. */
    private static final BlockPos[][] NEIGHBOR_OFFSETS = buildNeighborOffsets();

    private static BlockPos[][] buildNeighborOffsets() {
        BlockPos[][] offsets = new BlockPos[6][8];
        buildFaceOffsets(offsets, Direction.UP,    Direction.NORTH, Direction.EAST);
        buildFaceOffsets(offsets, Direction.DOWN,  Direction.SOUTH, Direction.EAST);
        buildFaceOffsets(offsets, Direction.NORTH, Direction.UP,    Direction.EAST);
        buildFaceOffsets(offsets, Direction.SOUTH, Direction.UP,    Direction.WEST);
        buildFaceOffsets(offsets, Direction.WEST,  Direction.UP,    Direction.SOUTH);
        buildFaceOffsets(offsets, Direction.EAST,  Direction.UP,    Direction.NORTH);
        return offsets;
    }

    private static void buildFaceOffsets(BlockPos[][] offsets, Direction face,
                                         Direction topDir, Direction rightDir) {
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

    private final net.minecraft.client.resources.model.BakedModel baseModel;
    private final List<OverlayConnectionRule> ruleList;
    private final Map<TextureAtlasSprite, Integer> spriteToRuleIndex;
    private final int catchAllRuleIndex;
    /** Tint index applied to overlay quads, or -1 for no tint. */
    private final int tintIndex;
    /** When true, overlay quads are stamped with FULL_BRIGHT lightmap so they glow. */
    private final boolean emissive;

    public OverlayBakedModel(net.minecraft.client.resources.model.BakedModel baseModel,
                              Map<TextureAtlasSprite, OverlayConnectionRule> spriteRules,
                              int tintIndex,
                              boolean emissive) {
        this.baseModel = baseModel;

        Map<OverlayConnectionRule, Integer> ruleIndex = new IdentityHashMap<>();
        List<OverlayConnectionRule> list = new ArrayList<>();
        int catchAll = -1;

        for (Map.Entry<TextureAtlasSprite, OverlayConnectionRule> e : spriteRules.entrySet()) {
            OverlayConnectionRule rule = e.getValue();
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
        this.tintIndex = tintIndex;
        this.emissive = emissive;

        Map<TextureAtlasSprite, Integer> s2r = new IdentityHashMap<>();
        for (Map.Entry<TextureAtlasSprite, OverlayConnectionRule> e : spriteRules.entrySet()) {
            if (e.getKey() != null) {
                s2r.put(e.getKey(), ruleIndex.get(e.getValue()));
            }
        }
        this.spriteToRuleIndex = s2r;
    }

    // ---- ModelData (computed once per chunk section rebuild) ----------------

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos,
                                           @NotNull BlockState state, @NotNull ModelData existing) {
        int numRules = ruleList.size();
        if (numRules == 0) return existing;

        int[][] masks = new int[numRules][6];
        for (int ri = 0; ri < numRules; ri++) {
            for (Direction face : Direction.values()) {
                masks[ri][face.ordinal()] = computeMask(level, pos, state, face, ruleList.get(ri));
            }
        }
        return existing.derive().with(OVERLAY_MASKS, masks).build();
    }

    private int computeMask(BlockAndTintGetter level, BlockPos pos, BlockState state,
                             Direction face, OverlayConnectionRule rule) {
        BlockPos[] off = NEIGHBOR_OFFSETS[face.ordinal()];
        boolean t  = rule.connects(level, pos, state, face, pos.offset(off[0]));
        boolean r  = rule.connects(level, pos, state, face, pos.offset(off[2]));
        boolean b  = rule.connects(level, pos, state, face, pos.offset(off[4]));
        boolean l  = rule.connects(level, pos, state, face, pos.offset(off[6]));
        // Diagonals checked independently — outer-corner tiles require diagonal-only connection
        boolean tr = rule.connects(level, pos, state, face, pos.offset(off[1]));
        boolean br = rule.connects(level, pos, state, face, pos.offset(off[3]));
        boolean bl = rule.connects(level, pos, state, face, pos.offset(off[5]));
        boolean tl = rule.connects(level, pos, state, face, pos.offset(off[7]));
        return (t ? 1 : 0) | (tr ? 2 : 0) | (r ? 4 : 0) | (br ? 8 : 0)
             | (b ? 16 : 0) | (bl ? 32 : 0) | (l ? 64 : 0) | (tl ? 128 : 0);
    }

    /** Mirrors a CTM mask horizontally: swaps L↔R, TR↔TL, BR↔BL; T and B unchanged. */
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

    // ---- getQuads (the overlay quad emitter) --------------------------------

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                             @NotNull RandomSource rand, @NotNull ModelData data,
                                             @Nullable RenderType renderType) {
        // Only produce quads for block rendering on a specific face in the cutout pass.
        // Overlays always render in CUTOUT, even for translucent originals (e.g. ice) — this keeps
        // the overlay depth-buffered ahead of the translucent pass, preventing coplanar z-fighting.
        if (side == null) return List.of();
        if (renderType != null && renderType != RenderType.cutout()) return List.of();

        // Fetch base geometry using the cutout render type.
        List<BakedQuad> base = baseModel.getQuads(state, side, rand, data, RenderType.cutout());
        if (base.isEmpty()) return List.of();

        int[][] masks = data.get(OVERLAY_MASKS);
        if (masks == null) return List.of();

        int faceOrd = side.ordinal();
        int tintIndex = this.tintIndex;
        List<BakedQuad> result = new ArrayList<>(base.size() * 4);

        for (BakedQuad quad : base) {
            TextureAtlasSprite sprite = quad.getSprite();
            int ruleIdx = spriteToRuleIndex.getOrDefault(sprite, catchAllRuleIndex);
            if (ruleIdx < 0 || ruleIdx >= masks.length) continue;

            int mask = masks[ruleIdx][faceOrd];
            if (mask == 0) continue; // no connections on this face

            if (FLIP_H[faceOrd]) mask = flipMaskH(mask);

            for (int corner = 0; corner < 4; corner++) {
                int tile = OverlayLayout.getTile(corner, mask);
                if (tile >= 0) {
                    result.add(remapToTile(quad, sprite, tile & 0xF, (tile >> 4) & 0xF, tintIndex, emissive));
                }
            }
        }
        return result;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                             @NotNull RandomSource rand) {
        return List.of();
    }

    /**
     * Creates a copy of {@code base} with UV remapped to tile (tileX, tileY) in the 6x3 atlas.
     * {@code tintIndex} is forwarded to the new BakedQuad so the chunk renderer applies
     * the registered BlockColors handler (e.g. biome grass color) at render time.
     */
    private static BakedQuad remapToTile(BakedQuad base, TextureAtlasSprite sprite,
                                          int tileX, int tileY, int tintIndex, boolean emissive) {
        float u0 = sprite.getU0(), u1 = sprite.getU1();
        float v0 = sprite.getV0(), v1 = sprite.getV1();
        float uSpan = u1 - u0;
        float vSpan = v1 - v0;

        int[] verts = Arrays.copyOf(base.getVertices(), base.getVertices().length);
        int stride = IQuadTransformer.STRIDE;
        int uvOff  = IQuadTransformer.UV0;

        for (int v = 0; v < 4; v++) {
            int off = v * stride + uvOff;
            float u  = Float.intBitsToFloat(verts[off]);
            float vv = Float.intBitsToFloat(verts[off + 1]);
            verts[off]     = Float.floatToRawIntBits(u0 + (tileX * uSpan + (u - u0)) / OverlayLayout.TILES_WIDE);
            verts[off + 1] = Float.floatToRawIntBits(v0 + (tileY * vSpan + (vv - v0)) / OverlayLayout.TILES_HIGH);
            if (emissive) {
                verts[v * stride + IQuadTransformer.UV2] = LightTexture.FULL_BRIGHT;
            }
        }

        return new BakedQuad(verts, tintIndex, base.getDirection(), sprite, base.isShade(), base.hasAmbientOcclusion());
    }

    // ---- BakedModel boilerplate ---------------------------------------------

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state,
                                                      @NotNull RandomSource rand,
                                                      @NotNull ModelData data) {
        return CUTOUT;
    }

    @Override
    public boolean useAmbientOcclusion() { return false; }

    @Override
    public boolean isGui3d() { return false; }

    @Override
    public boolean usesBlockLight() { return false; }

    @Override
    public boolean isCustomRenderer() { return false; }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() { return baseModel.getParticleIcon(); }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return baseModel.getParticleIcon(data);
    }

    @Override
    public @NotNull ItemOverrides getOverrides() { return ItemOverrides.EMPTY; }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull ItemTransforms getTransforms() { return baseModel.getTransforms(); }
}
