package com.otterly76.ott.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
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

            float val = switch (type) {
                case FULL_3D -> (float)pos.getX() + (float)pos.getY() + (float)pos.getZ();
                case HORIZONTAL -> (float)pos.getX() + (float)pos.getZ();
                case VERTICAL -> (float)pos.getY();
            };

            float wave = (float) Math.sin((val / scale) * Math.PI);
            float progress = wave * 0.5f + 0.5f;

            float hue = Mth.lerp(progress, hueMin, hueMax);

            float brightness = 1.0f;
            if (timeScale > 0.0f) {
                float time = (System.currentTimeMillis() / 1000.0f) / timeScale;
                float seed = (pos.getX() * 7 + pos.getY() * 13 + pos.getZ() * 19) / 100.0f;
                brightness = (float) (Math.sin(time + seed) * 0.1 + 0.9);
            }

            return Color.HSBtoRGB(hue, saturation, brightness);
        };
    }

    /**
     * Creates an ItemColor that mirrors the block color handler at the player's current position.
     * The item in the hotbar/inventory will show the hue the block would have at that location,
     * rather than cycling through a random rainbow.
     *
     * @param type       The spatial mode (must match the BlockColor handler for this block)
     * @param scale      Blocks per full rainbow cycle (must match the BlockColor handler)
     * @param saturation 0.0 (grayscale) to 1.0 (vibrant)
     * @param hueMin     Start of color range (must match the BlockColor handler)
     * @param hueMax     End of color range (must match the BlockColor handler)
     */
    public static ItemColor createItemColor(Type type, float scale, float saturation, float hueMin, float hueMax) {
        return (stack, tintIndex) -> {
            if (tintIndex != 0) return -1;
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return -1;

            float val = switch (type) {
                case FULL_3D   -> mc.player.getBlockX() + mc.player.getBlockY() + mc.player.getBlockZ();
                case HORIZONTAL -> mc.player.getBlockX() + mc.player.getBlockZ();
                case VERTICAL  -> mc.player.getBlockY();
            };

            float wave    = (float) Math.sin((val / scale) * Math.PI);
            float progress = wave * 0.5f + 0.5f;
            float hue     = Mth.lerp(progress, hueMin, hueMax);
            return Color.HSBtoRGB(hue, saturation, 1.0f);
        };
    }
}
