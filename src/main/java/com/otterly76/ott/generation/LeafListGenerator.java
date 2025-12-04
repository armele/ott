package com.otterly76.ott.generation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class LeafListGenerator {
    public static void main(String[] args) throws IOException {
        Path leavesDir = Paths.get("src/main/resources/assets/ott/textures/block/leaves");

        try (Stream<Path> paths = Files.walk(leavesDir)) {
            paths.filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .filter(filename -> filename.endsWith(".png"))
                    .map(filename -> filename.replace(".png", ""))
                    .sorted()
                    .forEach(name -> {
                        // Generate the field name (uppercase)
                        String fieldName = name.toUpperCase();
                        System.out.println("    public static final DeferredBlock<Block> " + fieldName +
                                " = registerLeaf(\"" + name + "\", () -> new Block(Properties.of().strength(0.2f).sound(SoundType.GRASS).noOcclusion()));");
                    });
        }
    }
}