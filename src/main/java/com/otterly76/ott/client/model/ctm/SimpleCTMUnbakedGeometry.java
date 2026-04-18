package com.otterly76.ott.client.model.ctm;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.client.model.geometry.UnbakedGeometryHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Unbaked geometry for the simplified 4-tile CTM system.
 *
 * <p>During {@link #bake} it pre-bakes all 96 quads (6 faces × 4 quadrants × 4 sprites)
 * so that runtime chunk rebuilds only look up a pre-baked quad — no per-frame allocation.
 */
public class SimpleCTMUnbakedGeometry implements IUnbakedGeometry<SimpleCTMUnbakedGeometry> {

    /**
     * Per-quadrant (left, right, top, bottom) in texture-space [0..1].
     * Index: 0=TL, 1=TR, 2=BL, 3=BR.
     */
    static final float[][] QUAD_LRTB = {
        { 0.0f, 0.5f, 1.0f, 0.5f }, // TL
        { 0.5f, 1.0f, 1.0f, 0.5f }, // TR
        { 0.0f, 0.5f, 0.5f, 0.0f }, // BL
        { 0.5f, 1.0f, 0.5f, 0.0f }, // BR
    };

    private final BlockModel baseModel;
    private final ConnectionRule rule;

    public SimpleCTMUnbakedGeometry(BlockModel baseModel, String ctmPath, ConnectionRule rule) {
        this.baseModel = baseModel;
        this.rule      = rule;
    }

    @Override
    public void resolveParents(@NotNull Function<ResourceLocation, UnbakedModel> modelGetter,
                               @NotNull IGeometryBakingContext context) {
        baseModel.resolveParents(modelGetter);
    }

    @Override
    public @NotNull BakedModel bake(@NotNull IGeometryBakingContext context,
                                    @NotNull ModelBaker baker,
                                    @NotNull Function<Material, TextureAtlasSprite> spriteGetter,
                                    @NotNull ModelState modelState,
                                    @NotNull ItemOverrides overrides) {
        // Bake the base model so SimpleCTMBakedModel can delegate particle icon, etc.
        BakedModel base = baseModel.bake(baker, baseModel, spriteGetter, modelState, true);

        // Resolve the 4 CTM tile sprites that were injected as "_ctm_tile_0".."_ctm_tile_3"
        // by SimpleCTMLoader into the model's texture map, ensuring atlas stitching.
        TextureAtlasSprite[] sprites = new TextureAtlasSprite[4];
        for (int i = 0; i < 4; i++) {
            sprites[i] = spriteGetter.apply(context.getMaterial("_ctm_tile_" + i));
        }

        // Pre-bake 6 × 4 × 4 = 96 quads: [face.ordinal()][quadrant][sprite]
        BakedQuad[][][] prebakedQuads = new BakedQuad[6][4][4];
        for (Direction face : Direction.values()) {
            int fi = face.ordinal();
            for (int q = 0; q < 4; q++) {
                float left   = QUAD_LRTB[q][0];
                float right  = QUAD_LRTB[q][1];
                float top    = QUAD_LRTB[q][2];
                float bottom = QUAD_LRTB[q][3];

                Vector3f start = startPos(face, left, right, top, bottom);
                Vector3f end   = endPos(face, left, right, top, bottom);
                float[]  uv    = faceUVs(start, end, face);

                for (int s = 0; s < 4; s++) {
                    final TextureAtlasSprite sprite = sprites[s];
                    // cullface = outward face direction; the element map key is the opposite.
                    BlockElementFace elementFace =
                            new BlockElementFace(face, -1, "", new BlockFaceUV(uv, 0));
                    BlockElement element = new BlockElement(
                            start, end,
                            new EnumMap<>(Map.of(face.getOpposite(), elementFace)),
                            null, true
                    );
                    List<BakedQuad> baked = UnbakedGeometryHelper.bakeElements(
                            List.of(element), mat -> sprite, BlockModelRotation.X0_Y0);
                    prebakedQuads[fi][q][s] = baked.isEmpty() ? null : baked.getFirst();
                }
            }
        }

        return new SimpleCTMBakedModel(base, prebakedQuads, rule);
    }

    // ── Geometry helpers (adapted from Athena's ForgeAthenaUtils) ─────────────
    // All coordinates are in block-model pixel space (0–16). depth = 0 (flush surface).

    static Vector3f startPos(Direction face, float left, float right, float top, float bottom) {
        return switch (face) {
            case NORTH -> new Vector3f((1 - right) * 16f, top    * 16f, 0f);
            case SOUTH -> new Vector3f(left        * 16f, top    * 16f, 16f);
            case WEST  -> new Vector3f(0f,               top    * 16f, left  * 16f);
            case EAST  -> new Vector3f(16f,              top    * 16f, (1 - right) * 16f);
            case DOWN  -> new Vector3f(left        * 16f, 0f,          top   * 16f);
            case UP    -> new Vector3f(left        * 16f, 16f,         (1 - bottom) * 16f);
        };
    }

    static Vector3f endPos(Direction face, float left, float right, float top, float bottom) {
        return switch (face) {
            case NORTH -> new Vector3f((1 - left) * 16f, bottom * 16f, 0f);
            case SOUTH -> new Vector3f(right      * 16f, bottom * 16f, 16f);
            case WEST  -> new Vector3f(0f,               bottom * 16f, right * 16f);
            case EAST  -> new Vector3f(16f,              bottom * 16f, (1 - left) * 16f);
            case DOWN  -> new Vector3f(right      * 16f, 0f,           bottom * 16f);
            case UP    -> new Vector3f(right      * 16f, 0f,           (1 - top) * 16f);
        };
    }

    /**
     * Returns BlockFaceUV coords [u0, v0, u1, v1] in 0–16 texture-pixel space,
     * matching Minecraft's per-face UV convention (adapted from Athena).
     */
    static float[] faceUVs(Vector3f from, Vector3f to, Direction face) {
        return switch (face) {
            case UP    -> new float[]{ from.x(),       to.z(),        to.x(),        from.z()        };
            case DOWN  -> new float[]{ from.x(),  16f - from.z(),    to.x(),    16f - to.z()        };
            case NORTH -> new float[]{ 16f - from.x(), 16f - to.y(), 16f - to.x(), 16f - from.y()   };
            case SOUTH -> new float[]{ to.x(),    16f - to.y(),      from.x(),  16f - from.y()      };
            case WEST  -> new float[]{ to.z(),    16f - to.y(),      from.z(),  16f - from.y()      };
            case EAST  -> new float[]{ 16f - from.z(), 16f - to.y(), 16f - to.z(), 16f - from.y()   };
        };
    }
}
