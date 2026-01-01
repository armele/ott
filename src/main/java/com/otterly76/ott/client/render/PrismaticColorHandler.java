package com.otterly76.ott.client.render;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.util.Mth;
import java.awt.Color;

public class PrismaticColorHandler {

    public enum Type { FULL_3D, HORIZONTAL, VERTICAL }

    /**
     * Creates a highly customizable prismatic color handler.
     *
     * @param type The spatial mode (FULL_3D, etc.)
     * @param scale How many blocks it takes to complete a full rainbow cycle
     * @param saturation 0.0 (grayscale) to 1.0 (neon vibrant)
     * @param hueMin The start of your color range (0.0 to 1.0)
     * @param hueMax The end of your color range (0.0 to 1.0)
     * @param timeScale Use 0.0f for a static block, or a number (like 5.0f) for pulsing.
     */

    public static BlockColor create(Type type, float scale, float saturation, float hueMin, float hueMax, float timeScale) {
        return (state, level, pos, tintIndex) -> {
            if (pos == null) return -1;

            // 1. Calculate Spatial Hue
            float val = switch (type) {
                case FULL_3D -> (float)pos.getX() + (float)pos.getY() + (float)pos.getZ();
                case HORIZONTAL -> (float)pos.getX() + (float)pos.getZ();
                case VERTICAL -> (float)pos.getY();
            };

            float progress = Mth.frac(val / scale);
            float hue = Mth.lerp(progress, hueMin, hueMax);

            // 2. Animation logic (Only used for items/GUI, as blocks cache this color)
            float brightness = 1.0f;
            if (timeScale > 0.0f) {
                float time = (System.currentTimeMillis() / 1000.0f) / timeScale;
                float seed = (pos.getX() * 7 + pos.getY() * 13 + pos.getZ() * 19) / 100.0f;
                brightness = (float) (Math.sin(time + seed) * 0.1 + 0.9);
            }

            return Color.HSBtoRGB(hue, saturation, brightness);
        };
    }
}