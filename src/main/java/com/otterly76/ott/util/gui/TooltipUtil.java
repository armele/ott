package com.otterly76.ott.util.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;

import java.util.ArrayList;
import java.util.List;

public class TooltipUtil {
    public static List<Component> splitTooltipLines(Component component) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        int maxWidth = 200;

        List<FormattedCharSequence> lines = font.split(component, maxWidth);
        List<Component> components = new ArrayList<>(lines.size());
        for (FormattedCharSequence fcs : lines) {
            components.add(fromFormattedSequence(fcs));
        }
        return components;
    }

    private static Component fromFormattedSequence(FormattedCharSequence fcs) {
        MutableComponent result = Component.empty();
        StringBuilder sb = new StringBuilder();
        final Style[] currentStyle = new Style[]{Style.EMPTY};

        FormattedCharSink sink = (index, style, codePoint) -> {
            // When style changes, flush previous segment
            if (!style.equals(currentStyle[0]) && !sb.isEmpty()) {
                result.append(Component.literal(sb.toString()).withStyle(currentStyle[0]));
                sb.setLength(0);
            }
            currentStyle[0] = style;
            sb.appendCodePoint(codePoint);
            return true;
        };

        fcs.accept(sink);
        if (!sb.isEmpty()) {
            result.append(Component.literal(sb.toString()).withStyle(currentStyle[0]));
        }
        return result;
    }
}
