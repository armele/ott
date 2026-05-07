package com.otterly76.ott.client.model.overlay;

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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Unbaked geometry for a terrain overlay model ({@code "loader": "ott:overlay"}).
 * Stores a vanilla {@link BlockModel} for geometry and a map of
 * texture-variable-name → {@link OverlayConnectionRule} for connection logic.
 */
public class OverlayUnbakedGeometry implements IUnbakedGeometry<OverlayUnbakedGeometry> {

    private final BlockModel baseModel;
    /**
     * Maps texture variable name → connection rule.
     * Use key {@code "*"} as a catch-all for all texture variables.
     */
    private final Map<String, OverlayConnectionRule> rulesByTexVar;
    private final int tintIndex;

    public OverlayUnbakedGeometry(BlockModel baseModel,
                                   Map<String, OverlayConnectionRule> rulesByTexVar,
                                   int tintIndex) {
        this.baseModel = baseModel;
        this.rulesByTexVar = rulesByTexVar;
        this.tintIndex = tintIndex;
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
        BakedModel base = baseModel.bake(baker, baseModel, spriteGetter, modelState, true);

        Map<TextureAtlasSprite, OverlayConnectionRule> spriteRules = new HashMap<>();

        if (rulesByTexVar.containsKey("*")) {
            // Single catch-all rule → stored under null key in OverlayBakedModel
            spriteRules.put(null, rulesByTexVar.get("*"));
        } else {
            // Per-texture-variable rules: resolve each tex-var name to its atlas sprite
            for (Map.Entry<String, OverlayConnectionRule> entry : rulesByTexVar.entrySet()) {
                String texVar = entry.getKey();
                if (context.hasMaterial(texVar)) {
                    Material mat = context.getMaterial(texVar);
                    TextureAtlasSprite sprite = spriteGetter.apply(mat);
                    spriteRules.put(sprite, entry.getValue());
                }
            }
        }

        return new OverlayBakedModel(base, spriteRules, tintIndex);
    }
}
