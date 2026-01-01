package com.otterly76.ott.client.render;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.util.Mth;
import java.awt.Color;

public class PrismaticColorHandler {

    // Enum to choose which coordinates to use
    public enum Type { FULL_3D, HORIZONTAL, VERTICAL }

    /**
     * Creates a highly customizable prismatic color handler.
     *
     * @param type The spatial mode (FULL_3D, etc.)
     * @param scale How many blocks it takes to complete a full rainbow cycle
     * @param saturation 0.0 (grayscale) to 1.0 (neon vibrant)
     * @param hueMin The start of your color range (0.0 to 1.0)
     * @param hueMax The end of your color range (0.0 to 1.0)
     */
    public static BlockColor create(Type type, float scale, float saturation, float hueMin, float hueMax) {
        return (state, level, pos, tintIndex) -> {
            if (pos == null) return -1;

            // 1. Calculate the raw spatial value
            float val = switch (type) {
                case FULL_3D -> (float)pos.getX() + (float)pos.getY() + (float)pos.getZ();
                case HORIZONTAL -> (float)pos.getX() + (float)pos.getZ();
                case VERTICAL -> (float)pos.getY();
            };

            // 2. Map the coordinate to a 0.0-1.0 progress value
            float progress = Mth.frac(val / scale);

            // 3. Constrain that progress to your custom Hue Range
            // (e.g. if hueMin=0.5 and hueMax=0.7, it only stays in the Blues/Purples)
            float hue = Mth.lerp(progress, hueMin, hueMax);

            return Color.HSBtoRGB(hue, saturation, 1.0f);
        };
    }
}