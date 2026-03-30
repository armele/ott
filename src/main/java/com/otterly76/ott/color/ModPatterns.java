package com.otterly76.ott.color;

import com.otterly76.ott.Constants;
import net.minecraft.world.item.DyeColor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

    private static List<String> findPatterns() {
        List<String> patterns = new ArrayList<>();
        // In IDE, we can check the physical path
        try {
            Path assetsPath = Paths.get("src", "main", "resources", "assets", Constants.MOD_ID, "textures", "block", "patterns");
            if (Files.exists(assetsPath) && Files.isDirectory(assetsPath)) {
                try (Stream<Path> files = Files.list(assetsPath)) {
                    files.filter(f -> f.toString().endsWith(".png"))
                            .map(f -> f.getFileName().toString().replace(".png", ""))
                            .forEach(patterns::add);
                }
            }
        } catch (Exception ignored) {
        }

        // Fallback for when running in production/JAR if no patterns were found
        if (patterns.isEmpty()) {
            patterns.add("dyed_cobblestone");
        }
        return patterns;
    }

    private ModPatterns() {}
}