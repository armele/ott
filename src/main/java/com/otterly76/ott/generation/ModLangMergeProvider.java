package com.otterly76.ott.generation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.otterly76.ott.color.ModColorSets;
import com.otterly76.ott.wood.ModWoodSets;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Merges base lang files and writes generated en_us.json files for both minecraft and ott namespaces.
 */
public class ModLangMergeProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final PackOutput output;

    public ModLangMergeProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
        JsonObject mcBase = readBaseLang("assets/minecraft/lang/en_us_base.json");
        JsonObject ottBase = readBaseLang("assets/ott/lang/en_us_base.json");

        // Add auto-generated wood set entries to OTT base
        for (ModWoodSets.WoodSet set : ModWoodSets.ALL) {
            addWoodSetEntries(ottBase, set.name());
        }

        // Add auto-generated vanilla structural entries to OTT base
        addVanillaStructuralEntries(ottBase);

        // Add auto-generated color set entries to OTT base
        for (ModColorSets.ColorSet set : ModColorSets.ALL) {
            addColorSetEntries(ottBase, set.name());
        }

        // Add auto-generated pattern block entries to OTT base
        for (String pattern : com.otterly76.ott.color.ModPatterns.PATTERNS) {
            for (com.otterly76.ott.color.ModPatterns.ColorInfo color : com.otterly76.ott.color.ModPatterns.ALL_COLORS) {
                addPatternBlockEntries(ottBase, pattern, color.name());
            }
        }

        // Add clay tile item entries (item-only, not blocks)
        for (com.otterly76.ott.color.ModPatterns.ColorInfo color : com.otterly76.ott.color.ModPatterns.ALL_COLORS) {
            String colorCap = Arrays.stream(color.name().split("_"))
                    .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                    .collect(Collectors.joining(" "));
            ottBase.addProperty("item.ott." + color.name() + "_clay_tile", colorCap + " Clay Tile");
        }

        // Add auto-generated elevator block entries to OTT base
        for (com.otterly76.ott.color.ModPatterns.ColorInfo color : com.otterly76.ott.color.ModPatterns.ALL_COLORS) {
            addElevatorBlockEntries(ottBase, color.name());
        }

        // Add auto-generated stone set entries to OTT base
        for (com.otterly76.ott.block.stone.ModStoneVariants.StoneVariant v : com.otterly76.ott.block.stone.ModStoneVariants.ALL) {
            addStoneSetEntries(ottBase, v.name());
        }

        // Add wood door variant entries
        com.otterly76.ott.block.ModBlocks.WOOD_DOORS.forEach((wood, styleMap) -> {
            String woodCap = Arrays.stream(wood.split("_"))
                    .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                    .collect(Collectors.joining(" "));
            styleMap.keySet().forEach(style -> {
                String styleCap = Arrays.stream(style.split("_"))
                        .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                        .collect(Collectors.joining(" "));
                ottBase.addProperty("block.ott." + style + "_" + wood + "_door", styleCap + " " + woodCap + " Door");
            });
        });

        // Add creative tab titles
        ottBase.addProperty("itemGroup.ott.blocks", "New Otterhome Blocks");
        ottBase.addProperty("itemGroup.ott.color_sets", "New Otterhome Color Sets");
        ottBase.addProperty("itemGroup.ott.wood_sets", "New Otterhome Wood Sets");
        ottBase.addProperty("ott.creative_category.stone_vanilla",             "Vanilla Plus: Stone");
        ottBase.addProperty("ott.creative_category.stone_custom",              "Custom Stone");
        ottBase.addProperty("ott.creative_category.stone_mosaic_traditional",  "Mosaic Traditional");

        Path mcOut = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve("minecraft/lang/en_us.json");
        Path ottOut = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve("ott/lang/en_us.json");

        return CompletableFuture.allOf(
                DataProvider.saveStable(cachedOutput, mcBase, mcOut),
                DataProvider.saveStable(cachedOutput, ottBase, ottOut)
        );
    }

    private void addWoodSetEntries(JsonObject json, String name) {
        String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1);
        json.addProperty("block.ott." + name + "_button", capitalized + " Button");
        json.addProperty("block.ott." + name + "_door", capitalized + " Door");
        json.addProperty("block.ott." + name + "_fence", capitalized + " Fence");
        json.addProperty("block.ott." + name + "_fence_gate", capitalized + " Fence Gate");
        json.addProperty("block.ott." + name + "_hanging_sign", capitalized + " Hanging Sign");
        json.addProperty("block.ott." + name + "_leaves", capitalized + " Leaves");
        json.addProperty("block.ott." + name + "_log", capitalized + " Log");
        json.addProperty("block.ott." + name + "_planks", capitalized + " Planks");
        json.addProperty("block.ott." + name + "_pressure_plate", capitalized + " Pressure Plate");
        json.addProperty("block.ott." + name + "_sapling", capitalized + " Sapling");
        json.addProperty("block.ott." + name + "_sign", capitalized + " Sign");
        json.addProperty("block.ott." + name + "_slab", capitalized + " Slab");
        json.addProperty("block.ott." + name + "_stairs", capitalized + " Stairs");
        json.addProperty("block.ott." + name + "_trapdoor", capitalized + " Trapdoor");
        json.addProperty("block.ott." + name + "_wood", capitalized + " Wood");
        json.addProperty("block.ott.stripped_" + name + "_log", "Stripped " + capitalized + " Log");
        json.addProperty("block.ott.stripped_" + name + "_wood", "Stripped " + capitalized + " Wood");
        json.addProperty("block.ott.potted_" + name + "_sapling", "Potted " + capitalized + " Sapling");
        json.addProperty("block.ott." + name + "_beam",         capitalized + " Beam");
        json.addProperty("block.ott." + name + "_pergola",      capitalized + " Pergola");
        json.addProperty("block.ott." + name + "_planks_plate", capitalized + " Planks Plate");
        json.addProperty("block.ott." + name + "_planks_edge",  capitalized + " Planks Edge");
        json.addProperty("block.ott." + name + "_bannister",     capitalized + " Bannister");
        json.addProperty("block.ott." + name + "_support_slab", capitalized + " Support Slab");
        json.addProperty("block.ott." + name + "_support_beam", capitalized + " Support Beam");
        json.addProperty("block.ott." + name + "_beehive",      capitalized + " Beehive");
        json.addProperty("block.ott." + name + "_shelf",        capitalized + " Shelf");
    }

    private void addVanillaStructuralEntries(JsonObject json) {
        com.otterly76.ott.block.ModBlocks.VANILLA_STRUCTURAL_SETS.forEach((name, set) -> {
            String capitalized = Arrays.stream(name.split("_"))
                    .map(w -> Character.toUpperCase(w.charAt(0)) + w.substring(1))
                    .collect(Collectors.joining(" "));
            json.addProperty("block.ott." + name + "_beam",         capitalized + " Beam");
            json.addProperty("block.ott." + name + "_pergola",      capitalized + " Pergola");
            json.addProperty("block.ott." + name + "_planks_plate", capitalized + " Planks Plate");
            json.addProperty("block.ott." + name + "_planks_edge",  capitalized + " Planks Edge");
            json.addProperty("block.ott." + name + "_bannister",     capitalized + " Bannister");
            json.addProperty("block.ott." + name + "_support_slab", capitalized + " Support Slab");
            json.addProperty("block.ott." + name + "_support_beam", capitalized + " Support Beam");
        });
    }

    private void addColorSetEntries(JsonObject json, String name) {
        String capitalized = Arrays.stream(name.split("_"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));

        json.addProperty("block.ott." + name + "_candle", capitalized + " Candle");
        json.addProperty("block.ott." + name + "_concrete", capitalized + " Concrete");
        json.addProperty("block.ott." + name + "_concrete_powder", capitalized + " Concrete Powder");
        json.addProperty("block.ott." + name + "_glazed_terracotta", capitalized + " Glazed Terracotta");
        json.addProperty("block.ott." + name + "_shulker_box", capitalized + " Shulker Box");
        json.addProperty("block.ott." + name + "_stained_glass", capitalized + " Stained Glass");
        json.addProperty("block.ott." + name + "_stained_glass_pane", capitalized + " Stained Glass Pane");
        json.addProperty("block.ott." + name + "_terracotta", capitalized + " Terracotta");
        json.addProperty("block.ott." + name + "_wool", capitalized + " Wool");
        json.addProperty("block.ott." + name + "_bed", capitalized + " Bed");
        json.addProperty("block.ott." + name + "_carpet", capitalized + " Carpet");
        json.addProperty("block.ott." + name + "_banner", capitalized + " Banner");
        json.addProperty("item.ott." + name + "_dye", capitalized + " Dye");
        json.addProperty("block.ott." + name + "_seaglass",         capitalized + " Seaglass");
        json.addProperty("block.ott." + name + "_bubbles_seaglass",  capitalized + " Bubbles Seaglass");
        json.addProperty("block.ott." + name + "_smooth_seaglass",   capitalized + " Smooth Seaglass");
        json.addProperty("block.ott." + name + "_waves_seaglass",    capitalized + " Waves Seaglass");
        json.addProperty("block.ott." + name + "_futon",             capitalized + " Futon");
        json.addProperty("block.ott." + name + "_plate",             capitalized + " Plate");
        json.addProperty("block.ott." + name + "_edge",              capitalized + " Edge");
        json.addProperty("block.ott." + name + "_beam",              capitalized + " Beam");
        json.addProperty("block.ott." + name + "_pergola",           capitalized + " Pergola");
        json.addProperty("block.ott." + name + "_geometric_window",  capitalized + " Geometric Window");
        json.addProperty("block.ott." + name + "_bannister",         capitalized + " Bannister");
        json.addProperty("block.ott." + name + "_support_slab",      capitalized + " Support Slab");
        json.addProperty("block.ott." + name + "_support_beam",      capitalized + " Support Beam");
    }

    private void addStoneSetEntries(JsonObject json, String name) {
        String capitalized = Arrays.stream(name.split("_"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));

        json.addProperty("block.ott." + name + "_plate",            capitalized + " Plate");
        json.addProperty("block.ott." + name + "_edge",             capitalized + " Edge");
        json.addProperty("block.ott." + name + "_beam",             capitalized + " Beam");
        json.addProperty("block.ott." + name + "_pergola",          capitalized + " Pergola");
        json.addProperty("block.ott." + name + "_geometric_window", capitalized + " Geometric Window");
        json.addProperty("block.ott." + name + "_bannister",        capitalized + " Bannister");
        json.addProperty("block.ott." + name + "_support_slab",     capitalized + " Support Slab");
        json.addProperty("block.ott." + name + "_support_beam",     capitalized + " Support Beam");
    }

    private void addElevatorBlockEntries(JsonObject json, String colorName) {
        String colorCap = Arrays.stream(colorName.split("_"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));
        json.addProperty("block.ott." + colorName + "_elevator", colorCap + " Elevator");
    }

    private void addPatternBlockEntries(JsonObject json, String pattern, String colorName) {
        String colorCap = Arrays.stream(colorName.split("_"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));
        String patternCap = Arrays.stream(pattern.split("_"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));

        String base = "block.ott." + colorName + "_" + pattern;
        String name  = colorCap + " " + patternCap;
        json.addProperty(base, name);
    }

    @Override
    public @NotNull String getName() {
        return "Mod Lang Merge Provider";
    }

    @SuppressWarnings("DuplicatedCode")
    private JsonObject readBaseLang(String path) {
        try (var in = ModLangMergeProvider.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) throw new IllegalStateException("Missing " + path + " on classpath");

            JsonElement el = GSON.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), JsonElement.class);
            if (el == null || !el.isJsonObject()) throw new IllegalStateException(path + " is not a JSON object");
            return el.getAsJsonObject();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed reading base lang file: " + path, e);
        }
    }
}