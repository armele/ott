package com.otterly76.ott.client.model.ctm;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Unbaked geometry for a CTM connecting block model.
 * Wraps a standard {@link BlockModel} and adds per-texture connection rules.
 */
public class ConnectingUnbakedGeometry implements IUnbakedGeometry<ConnectingUnbakedGeometry> {

    private final BlockModel baseModel;
    /**
     * Maps texture variable name → connection rule.
     * Use key "*" as a catch-all for all textures.
     */
    private final Map<String, ConnectionRule> rulesByTexVar;
    /**
     * Maps texture variable name → isolated tile ResourceLocation.
     * When present, the isolated sprite is provided to Domum Ornamentum so it sees
     * only the single tile rather than the full CTM atlas.
     */
    private final Map<String, ResourceLocation> isolatedTexturePaths;
    /** Atlas layout controlling tile dimensions and mask→tile mapping. */
    private final CtmLayout layout;

    public ConnectingUnbakedGeometry(BlockModel baseModel, Map<String, ConnectionRule> rulesByTexVar) {
        this(baseModel, rulesByTexVar, Collections.emptyMap(), CtmLayout.FULL);
    }

    public ConnectingUnbakedGeometry(BlockModel baseModel, Map<String, ConnectionRule> rulesByTexVar,
                                     Map<String, ResourceLocation> isolatedTexturePaths) {
        this(baseModel, rulesByTexVar, isolatedTexturePaths, CtmLayout.FULL);
    }

    public ConnectingUnbakedGeometry(BlockModel baseModel, Map<String, ConnectionRule> rulesByTexVar,
                                     Map<String, ResourceLocation> isolatedTexturePaths, CtmLayout layout) {
        this.baseModel = baseModel;
        this.rulesByTexVar = rulesByTexVar;
        this.isolatedTexturePaths = isolatedTexturePaths;
        this.layout = layout;
    }

    @Override
    public void resolveParents(@NotNull Function<ResourceLocation, UnbakedModel> modelGetter, @NotNull IGeometryBakingContext context) {
        baseModel.resolveParents(modelGetter);
    }

    @Override
    public @NotNull BakedModel bake(@NotNull IGeometryBakingContext context, @NotNull ModelBaker baker,
                                    @NotNull Function<Material, TextureAtlasSprite> spriteGetter,
                                    @NotNull ModelState modelState, @NotNull ItemOverrides overrides) {
        // Bake the base model normally
        BakedModel base = baseModel.bake(baker, baseModel, spriteGetter, modelState, true);

        // Resolve sprite → ConnectionRule mapping
        // The base model has texture variables; we resolve what sprite each var maps to
        Map<TextureAtlasSprite, ConnectionRule> spriteRules = new HashMap<>();

        if (rulesByTexVar.containsKey("*")) {
            // Single catch-all rule: will be applied to all quads regardless of sprite.
            // Store under a null key as the default.
            ConnectionRule catchAll = rulesByTexVar.get("*");
            // We'll handle this in ConnectingBakedModel by checking for a null-keyed default.
            spriteRules.put(null, catchAll);
        } else {
            // Per-texture-variable rules: resolve each variable to its sprite.
            for (Map.Entry<String, ConnectionRule> entry : rulesByTexVar.entrySet()) {
                String texVar = entry.getKey();
                if (context.hasMaterial(texVar)) {
                    Material mat = context.getMaterial(texVar);
                    TextureAtlasSprite sprite = spriteGetter.apply(mat);
                    spriteRules.put(sprite, entry.getValue());
                }
            }
        }

        // Resolve isolated sprites: CTM atlas sprite → small representative sprite.
        // Used to give Domum Ornamentum a properly-sized sprite (not the full 128px atlas).
        Map<TextureAtlasSprite, TextureAtlasSprite> ctmToIsolated = new HashMap<>();
        for (Map.Entry<String, ResourceLocation> entry : isolatedTexturePaths.entrySet()) {
            String texVar = entry.getKey();
            if (context.hasMaterial(texVar)) {
                Material ctmMat = context.getMaterial(texVar);
                TextureAtlasSprite ctmSprite = spriteGetter.apply(ctmMat);
                // Create a Material for the isolated texture on the same atlas
                Material isolatedMat = new Material(ctmMat.atlasLocation(), entry.getValue());
                TextureAtlasSprite isolatedSprite = spriteGetter.apply(isolatedMat);
                ctmToIsolated.put(ctmSprite, isolatedSprite);
            }
        }

        return new ConnectingBakedModel(base, spriteRules, ctmToIsolated, layout);
    }
}
