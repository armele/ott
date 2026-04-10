package com.otterly76.ott.searchables.api.autcomplete;

import com.otterly76.ott.searchables.api.TokenRange;
import net.minecraft.network.chat.Component;

public record CompletionSuggestion(String suggestion, Component display, String suffix, TokenRange replacementRange) {

    public String toInsert() {
        return suggestion + suffix;
    }

    public String replaceIn(final String string) {
        return replacementRange.replace(string, toInsert());
    }
}
