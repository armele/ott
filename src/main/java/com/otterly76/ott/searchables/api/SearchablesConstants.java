package com.otterly76.ott.searchables.api;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;
import java.util.function.Predicate;

public class SearchablesConstants {

    public static final String STRING_CHARACTERS = "'\"`";

    public static final Component COMPONENT_SEARCH = Component.translatable("options.search");

    public static final Predicate<String> VALID_SUGGESTION = s -> {
        int quoteCount = 0;
        for (int i = 0; i < SearchablesConstants.STRING_CHARACTERS.length(); i++) {
            if (StringUtils.contains(s, SearchablesConstants.STRING_CHARACTERS.charAt(i))) {
                quoteCount++;
            }
        }
        return quoteCount < 3;
    };

    public static final Function<String, String> QUOTE = Util.memoize(s -> {
        if (StringUtils.containsNone(s, SearchablesConstants.STRING_CHARACTERS + " ")) {
            return s;
        }
        char quoteChar = '"';
        while (StringUtils.contains(s, quoteChar)) {
            quoteChar = switch (quoteChar) {
                case '"' -> '\'';
                case '\'' -> '`';
                default -> throw new IllegalStateException("Unable to nicely wrap {" + s + "}!");
            };
        }
        return StringUtils.wrap(s, quoteChar);
    });
}
