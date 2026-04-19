package com.otterly76.ott.color;

import com.otterly76.ott.Constants;
import net.minecraft.world.item.DyeColor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public final class ModPatterns {
    public static final List<ColorInfo> ALL_COLORS = new ArrayList<>();

    static {
        // Vanilla colors
        for (DyeColor dye : DyeColor.values()) {
            ALL_COLORS.add(new ColorInfo(dye.getName(), 0xFF000000 | dye.getTextureDiffuseColor()));
        }
        // Custom colors
        for (ModColorSets.ColorSet set : ModColorSets.ALL) {
            ALL_COLORS.add(new ColorInfo(set.name(), set.color()));
        }
    }

    public record ColorInfo(String name, int color) {}

    public static final List<String> PATTERNS = findPatterns();
    /** Patterns that have a paired {@code _mask.png} — registered as {@link net.minecraft.world.level.block.RotatedPillarBlock} with a two-layer tinted model. */
    public static final Set<String> PILLAR_PATTERNS = findPillarPatterns();

    private static Path getPatternsPath() {
        String sourceResources = System.getProperty("ott.sourceResources");
        return sourceResources != null
                ? Paths.get(sourceResources, "assets", Constants.MOD_ID, "textures", "block", "patterns")
                : Paths.get("src", "main", "resources", "assets", Constants.MOD_ID, "textures", "block", "patterns");
    }

    private static List<String> findPatterns() {
        List<String> patterns = new ArrayList<>();
        try {
            Path assetsPath = getPatternsPath();
            if (Files.exists(assetsPath) && Files.isDirectory(assetsPath)) {
                try (Stream<Path> files = Files.list(assetsPath)) {
                    files.filter(f -> {
                                String name = f.getFileName().toString();
                                return name.endsWith(".png") && !name.endsWith("_mask.png");
                            })
                            .map(f -> f.getFileName().toString().replace(".png", ""))
                            .forEach(patterns::add);
                }
            }
        } catch (Exception ignored) {
        }

        // Fallback for production JAR
        if (patterns.isEmpty()) {
            patterns.add("dyed_cobblestone");
        }
        return patterns;
    }

    private static Set<String> findPillarPatterns() {
        Set<String> pillars = new HashSet<>();
        try {
            Path assetsPath = getPatternsPath();
            if (Files.exists(assetsPath) && Files.isDirectory(assetsPath)) {
                try (Stream<Path> files = Files.list(assetsPath)) {
                    files.filter(f -> f.getFileName().toString().endsWith("_mask.png"))
                            .map(f -> f.getFileName().toString().replace("_mask.png", ""))
                            .filter(PATTERNS::contains)
                            .forEach(pillars::add);
                }
            }
        } catch (Exception ignored) {
        }
        return pillars;
    }

    private ModPatterns() {}
}