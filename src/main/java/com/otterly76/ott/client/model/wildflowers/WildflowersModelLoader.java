package com.otterly76.ott.client.model.wildflowers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Geometry loader registered as {@code ott:wildflowers}.
 * Reads a {@code "variants"} JSON array and an optional {@code "layer"} integer,
 * then produces a {@link WildflowersDynamicModel} that picks one variant per
 * block position with layer-offset randomisation so each flower slot draws a
 * different texture.
 */
public class WildflowersModelLoader implements IGeometryLoader<WildflowersModelLoader.Geometry> {

    public static final WildflowersModelLoader INSTANCE = new WildflowersModelLoader();

    private WildflowersModelLoader() {}

    @Override
    public @NotNull Geometry read(@NotNull JsonObject json,
                                  @NotNull JsonDeserializationContext context) throws JsonParseException {
        JsonArray arr = json.getAsJsonArray("variants");
        List<ResourceLocation> locs = new ArrayList<>();
        for (JsonElement el : arr) {
            locs.add(ResourceLocation.parse(el.getAsString()));
        }
        int layer = json.has("layer") ? json.get("layer").getAsInt() : 0;
        return new Geometry(locs, layer);
    }

    public static class Geometry implements IUnbakedGeometry<Geometry> {

        private final List<ResourceLocation> variantLocations;
        private final int layer;

        Geometry(List<ResourceLocation> variantLocations, int layer) {
            this.variantLocations = variantLocations;
            this.layer = layer;
        }

        @Override
        public void resolveParents(@NotNull Function<ResourceLocation, UnbakedModel> modelGetter,
                                   @NotNull IGeometryBakingContext context) {
            for (ResourceLocation loc : variantLocations) {
                modelGetter.apply(loc).resolveParents(modelGetter);
            }
        }

        @Override
        public @NotNull BakedModel bake(@NotNull IGeometryBakingContext context, @NotNull ModelBaker baker,
                                        @NotNull Function<Material, TextureAtlasSprite> spriteGetter,
                                        @NotNull ModelState modelState,
                                        @NotNull ItemOverrides overrides) {
            List<BakedModel> baked = variantLocations.stream()
                    .map(loc -> baker.bake(loc, modelState, spriteGetter))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (baked.isEmpty()) {
                throw new IllegalStateException("No wildflowers variants baked for " + variantLocations);
            }
            return new WildflowersDynamicModel(baked, layer);
        }
    }
}
