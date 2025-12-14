package com.otterly76.ott.wood;

import java.util.List;

/**
 * One source of truth for all ott wood sets.
 * <p>
 * Add a new set by adding its folder under:
 *   assets/ott/textures/block/<set>/
 * and adding the name here.
 */

public final class ModWoodSets {
    private ModWoodSets() {}

    public static final List<WoodSet> ALL = List.of(
            new WoodSet("starlight")
    );

    public record WoodSet(String name) {
        public WoodSet {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("WoodSet name must not be blank");
            }
        }
    }
}