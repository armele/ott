package com.otterly76.ott.recycling;

public enum RecyclingStatus {
    NO_RECIPE_FOUND(0, "screen.ott.recycling.no_recipe_found", 0xFFff615c),
    NO_SUITABLE_OUTPUT_SLOT(1, "screen.ott.recycling.no_suitable_output_slot", 0xFFfc8b49),
    NOT_ENOUGH_EXP(2, "screen.ott.recycling.not_enough_exp", 0xFFf2ff7a),
    NOT_ENOUGH_INPUT_ITEM(3, "screen.ott.recycling.not_enough_input", 0xFFffef40),
    NOT_EMPTY_SHULKER(4, "screen.ott.recycling.not_empty_shulker", 0xFFe48aff),
    RESTRICTED_BY_CONFIG(5, "screen.ott.recycling.restricted_by_config", 0xFF4f4f4f),
    DAMAGED_ITEM(6, "screen.ott.recycling.damaged_item", 0xFFff615c),
    ENCHANTED_ITEM(7, "screen.ott.recycling.enchanted_item", 0xFFff615c),
    BLANK(-1, "screen.ott.recycling.blank", -1);

    private final int index;
    private final String translationKey;
    private final int overlay;

    RecyclingStatus(int index, String translationKey, int overlay) {
        this.index = index;
        this.translationKey = translationKey;
        this.overlay = overlay;
    }

    public int getIndex() {
        return index;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public int getOverlay() {
        return overlay;
    }

    public static RecyclingStatus byIndex(int index) {
        for (RecyclingStatus status : RecyclingStatus.values()) {
            if (status.index == index) {
                return status;
            }
        }
        return BLANK;
    }
}
