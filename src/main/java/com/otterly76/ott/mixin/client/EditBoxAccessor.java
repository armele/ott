package com.otterly76.ott.mixin.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Consumer;
import net.minecraft.util.FormattedCharSequence;

@Mixin(EditBox.class)
public interface EditBoxAccessor {
    @Accessor("cursorPos")
    int ott$getCursorPos();

    @Accessor("cursorPos")
    void ott$setCursorPos(int cursorPos);

    @Accessor("value")
    String ott$getValue();

    @Accessor("value")
    void ott$setValue(String value);

    @Accessor("highlightPos")
    int ott$getHighlightPos();

    @Accessor("highlightPos")
    void ott$setHighlightPos(int highlightPos);

    @Accessor("bordered")
    boolean ott$isBordered();

    @Accessor("font")
    Font ott$getFont();

    @Accessor("displayPos")
    int ott$getDisplayPos();

    @Accessor("displayPos")
    void ott$setDisplayPos(int displayPos);

    @Accessor("formatter")
    BiFunction<String, Integer, FormattedCharSequence> ott$getFormatter();

    @Accessor("formatter")
    void ott$setFormatter(BiFunction<String, Integer, FormattedCharSequence> formatter);

    @Accessor("filter")
    Predicate<String> ott$getFilter();

    @Accessor("maxLength")
    int ott$getMaxLength();

    @Accessor("maxLength")
    void ott$setMaxLength(int maxLength);

    @Accessor("responder")
    Consumer<String> ott$getResponder();

    @Accessor("isEditable")
    boolean ott$isEditable();

    @Accessor("textColor")
    int ott$getTextColor();

    @Accessor("textColorUneditable")
    int ott$getTextColorUneditable();

    @Accessor("focusedTime")
    long ott$getFocusedTime();

    @Accessor("hint")
    Component ott$getHint();

    @Accessor("suggestion")
    String ott$getSuggestion();

    @Accessor("SPRITES")
    static WidgetSprites ott$getSprites() {
        throw new UnsupportedOperationException();
    }

    @org.spongepowered.asm.mixin.gen.Invoker("onValueChange")
    void ott$invokeOnValueChange(String value);

    @org.spongepowered.asm.mixin.gen.Invoker("renderHighlight")
    void ott$invokeRenderHighlight(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2);
}
