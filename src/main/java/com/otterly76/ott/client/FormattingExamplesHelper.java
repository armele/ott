package com.otterly76.ott.client;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.LinkedHashMap;
import java.util.Map;

public class FormattingExamplesHelper {

    public static final Map<String, Component> FORMATTING_EXAMPLES = new LinkedHashMap<>();

    static {
        FORMATTING_EXAMPLES.put("§ ", Component.translatable("screen.ott.unlockedTyping.formatCodesTitle").withStyle(Style.EMPTY.withBold(true)));
        FORMATTING_EXAMPLES.put("§" + 'k', Component.literal("§" + 'k' + "Obfuscated Text"));
        FORMATTING_EXAMPLES.put("§" + 'l', Component.literal("§" + 'l' + "Bold Text"));
        FORMATTING_EXAMPLES.put("§" + 'm', Component.literal("§" + 'm' + "Strikethrough Text"));
        FORMATTING_EXAMPLES.put("§" + 'n', Component.literal("§" + 'n' + "Underlined Text"));
        FORMATTING_EXAMPLES.put("§" + 'o', Component.literal("§" + 'o' + "Italic Text"));
        FORMATTING_EXAMPLES.put("§" + 'r', Component.literal("§" + 'r' + "Reset Text"));
        for (int i = 0; i <= 9; i++) {
            FORMATTING_EXAMPLES.put("§" + i, Component.literal("§" + i + "Colored Text"));
        }
        for (char c = 'a'; c <= 'f'; c++) {
            FORMATTING_EXAMPLES.put("§" + c, Component.literal("§" + c + "Colored Text"));
        }
    }

    public static void renderFormattingExamples(GuiGraphics guiGraphics, Font font) {
        if (!OttConfig.UNLOCKED_TYPING.DISPLAY_FORMATTING_EXAMPLES.get()) return;
        int startY = 20;
        int startX = 20;
        for (Map.Entry<String, Component> entry : FORMATTING_EXAMPLES.entrySet()) {
            guiGraphics.drawString(font, FormattedCharSequence.forward(entry.getKey(), Style.EMPTY), startX, startY, 0xFFFFFFFF);
            guiGraphics.drawString(font, entry.getValue(), startX + 15, startY, 0xFFFFFFFF);
            startY += 9;
        }
    }
}
