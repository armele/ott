package com.otterly76.ott.client.gui.components;

import com.google.common.collect.Lists;
import com.otterly76.ott.mixin.client.EditBoxAccessor;
import com.otterly76.ott.util.data.ComponentDecomposer;
import com.otterly76.ott.util.data.FormattedStringDecomposer;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FormattableEditBox extends AdvancedEditBox {
    public FormattableEditBox(Font font, int x, int y, int width, int height, Component message) {
        this(font, x, y, width, height, null, message);
    }

    public FormattableEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox editBox, Component message) {
        super(font, x, y, width, height, editBox, message);
        ((EditBoxAccessor) this).ott$setFormatter((formatterValue, position) -> {
            List<FormattedCharSequence> list = Lists.newArrayList();
            FormattedStringDecomposer.LengthLimitedCharSink sink = new FormattedStringDecomposer.LengthLimitedCharSink(formatterValue.length(), position);
            FormattedStringDecomposer.iterateFormatted(((EditBoxAccessor) this).ott$getValue(), Style.EMPTY, (index, style, j) -> {
                if (sink.accept(index, style, j)) {
                    list.add((formattedCharSink) -> formattedCharSink.accept(index, style, j));
                }

                return true;
            });
            return FormattedCharSequence.composite(list);
        });
    }

    public void setValue(@NotNull String text) {
        if (((EditBoxAccessor) this).ott$getFilter().test(text)) {
            int aboveMaxLength = ComponentDecomposer.getStringLength(text) - ((EditBoxAccessor) this).ott$getMaxLength();
            if (aboveMaxLength > 0) {
                ((EditBoxAccessor) this).ott$setValue(ComponentDecomposer.removeLast(text, aboveMaxLength));
            } else {
                ((EditBoxAccessor) this).ott$setValue(text);
            }

            this.moveCursorToEnd(false);
            this.setHighlightPos(((EditBoxAccessor) this).ott$getCursorPos());
            ((EditBoxAccessor) this).ott$invokeOnValueChange(text);
        }

    }

    public void insertText(@NotNull String textToWrite) {
        int i = Math.min(((EditBoxAccessor) this).ott$getCursorPos(), ((EditBoxAccessor) this).ott$getHighlightPos());
        int j = Math.max(((EditBoxAccessor) this).ott$getCursorPos(), ((EditBoxAccessor) this).ott$getHighlightPos());
        String string = FormattedStringDecomposer.filterText(textToWrite);
        String string3 = (new StringBuilder(((EditBoxAccessor) this).ott$getValue())).replace(i, j, string).toString();
        int stringLength = ComponentDecomposer.getStringLength(string3) - ((EditBoxAccessor) this).ott$getMaxLength();
        if (stringLength > 0) {
            string = ComponentDecomposer.removeLast(textToWrite, stringLength);
        }

        String string2 = (new StringBuilder(((EditBoxAccessor) this).ott$getValue())).replace(i, j, string).toString();
        if (((EditBoxAccessor) this).ott$getFilter().test(string2)) {
            ((EditBoxAccessor) this).ott$setValue(string2);
            int l = string.length();
            this.setCursorPosition(i + l);
            this.setHighlightPos(((EditBoxAccessor) this).ott$getCursorPos());
            ((EditBoxAccessor) this).ott$invokeOnValueChange(((EditBoxAccessor) this).ott$getValue());
        }

    }

    public boolean charTyped(char codePoint, int modifiers) {
        if (!this.canConsumeInput()) {
            return false;
        } else if (FormattedStringDecomposer.isAllowedChatCharacter(codePoint)) {
            if (((EditBoxAccessor) this).ott$isEditable()) {
                this.insertText(Character.toString(codePoint));
            }

            return true;
        } else {
            return false;
        }
    }

    public void onClick(double mouseX, double mouseY) {
        int i = Mth.floor(mouseX) - this.getX();
        if (((EditBoxAccessor) this).ott$isBordered()) {
            i -= 4;
        }

        String string = FormattedStringDecomposer.plainHeadByWidth(((EditBoxAccessor) this).ott$getFont(), ((EditBoxAccessor) this).ott$getValue(), ((EditBoxAccessor) this).ott$getDisplayPos(), this.getInnerWidth(), Style.EMPTY);
        this.moveCursorTo(FormattedStringDecomposer.plainHeadByWidth(((EditBoxAccessor) this).ott$getFont(), string, 0, i, Style.EMPTY).length() + ((EditBoxAccessor) this).ott$getDisplayPos(), Screen.hasShiftDown());
        this.updateDoubleClickStatus();
    }

    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        int i = Mth.floor(mouseX) - this.getX();
        if (((EditBoxAccessor) this).ott$isBordered()) {
            i -= 4;
        }

        String string = FormattedStringDecomposer.plainHeadByWidth(((EditBoxAccessor) this).ott$getFont(), ((EditBoxAccessor) this).ott$getValue(), ((EditBoxAccessor) this).ott$getDisplayPos(), this.getInnerWidth(), Style.EMPTY);
        int mousePosition = FormattedStringDecomposer.plainHeadByWidth(((EditBoxAccessor) this).ott$getFont(), string, 0, i, Style.EMPTY).length() + ((EditBoxAccessor) this).ott$getDisplayPos();
        this.handleDragSelection(mousePosition, mouseX, mouseY);
    }

    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.isVisible()) {
            if (this.isBordered()) {
                ResourceLocation resourceLocation = EditBoxAccessor.ott$getSprites().get(this.isActive(), this.isFocused());
                guiGraphics.blitSprite(resourceLocation, this.getX(), this.getY(), this.getWidth(), this.getHeight());
            }

            int i = ((EditBoxAccessor) this).ott$isEditable() ? ((EditBoxAccessor) this).ott$getTextColor() : ((EditBoxAccessor) this).ott$getTextColorUneditable();
            int j = ((EditBoxAccessor) this).ott$getCursorPos() - ((EditBoxAccessor) this).ott$getDisplayPos();
            String string = FormattedStringDecomposer.plainHeadByWidth(((EditBoxAccessor) this).ott$getFont(), ((EditBoxAccessor) this).ott$getValue(), ((EditBoxAccessor) this).ott$getDisplayPos(), this.getInnerWidth(), Style.EMPTY);
            boolean bl = j >= 0 && j <= string.length();
            boolean bl2 = this.isFocused() && (Util.getMillis() - ((EditBoxAccessor) this).ott$getFocusedTime()) / 300L % 2L == 0L && bl;
            int l = ((EditBoxAccessor) this).ott$isBordered() ? this.getX() + 4 : this.getX();
            int m = ((EditBoxAccessor) this).ott$isBordered() ? this.getY() + (this.height - 8) / 2 : this.getY();
            int n = l;
            int k = Mth.clamp(((EditBoxAccessor) this).ott$getHighlightPos() - ((EditBoxAccessor) this).ott$getDisplayPos(), 0, string.length());
            if (k > string.length()) {
                k = string.length();
            }

            if (!string.isEmpty()) {
                String string2 = bl ? string.substring(0, j) : string;
                n = guiGraphics.drawString(((EditBoxAccessor) this).ott$getFont(), ((EditBoxAccessor) this).ott$getFormatter().apply(string2, ((EditBoxAccessor) this).ott$getDisplayPos()), l, m, i);
            }

            boolean bl3 = ((EditBoxAccessor) this).ott$getCursorPos() < ((EditBoxAccessor) this).ott$getValue().length() || ComponentDecomposer.getStringLength(((EditBoxAccessor) this).ott$getValue()) >= ((EditBoxAccessor) this).ott$getMaxLength();
            int o = n;
            if (!bl) {
                o = j > 0 ? l + this.width : l;
            } else if (!string.isEmpty()) {
                o = n - 1;
                --n;
            }

            if (!string.isEmpty() && bl && j < string.length()) {
                guiGraphics.drawString(((EditBoxAccessor) this).ott$getFont(), ((EditBoxAccessor) this).ott$getFormatter().apply(string.substring(j), ((EditBoxAccessor) this).ott$getCursorPos()), n, m, i);
            }

            if (((EditBoxAccessor) this).ott$getHint() != null && string.isEmpty() && !this.isFocused()) {
                guiGraphics.drawString(((EditBoxAccessor) this).ott$getFont(), ((EditBoxAccessor) this).ott$getHint(), m, l, i);
            }

            if (!bl3 && ((EditBoxAccessor) this).ott$getSuggestion() != null) {
                guiGraphics.drawString(((EditBoxAccessor) this).ott$getFont(), ((EditBoxAccessor) this).ott$getSuggestion(), o - 1, m, -8355712);
            }

            if (bl2 && k == j) {
                if (!string.isEmpty()) {
                    guiGraphics.fill(RenderType.guiOverlay(), o, m - 1, o + 1, m + 1 + 9, -3092272);
                } else {
                    guiGraphics.drawString(((EditBoxAccessor) this).ott$getFont(), "_", o, m, i);
                }
            }

            if (k != j) {
                int p = l + FormattedStringDecomposer.stringWidth(((EditBoxAccessor) this).ott$getFont(), ((EditBoxAccessor) this).ott$getValue().substring(0, ((EditBoxAccessor) this).ott$getHighlightPos()), ((EditBoxAccessor) this).ott$getDisplayPos());
                ((EditBoxAccessor) this).ott$invokeRenderHighlight(guiGraphics, o, m - 1, p - 1, m + 1 + 9);
            }
        }

    }

    protected void scrollTo(int position) {
        int i = ((EditBoxAccessor) this).ott$getValue().length();
        if (((EditBoxAccessor) this).ott$getDisplayPos() > i) {
            ((EditBoxAccessor) this).ott$setDisplayPos(i);
        }

        int j = this.getInnerWidth();
        String string = FormattedStringDecomposer.plainHeadByWidth(((EditBoxAccessor) this).ott$getFont(), ((EditBoxAccessor) this).ott$getValue(), ((EditBoxAccessor) this).ott$getDisplayPos(), j, Style.EMPTY);
        int k = string.length() + ((EditBoxAccessor) this).ott$getDisplayPos();
        if (position == ((EditBoxAccessor) this).ott$getDisplayPos()) {
            ((EditBoxAccessor) this).ott$setDisplayPos(((EditBoxAccessor) this).ott$getDisplayPos() - FormattedStringDecomposer.plainTailByWidth(((EditBoxAccessor) this).ott$getFont(), ((EditBoxAccessor) this).ott$getValue(), j, Style.EMPTY).length());
        }

        if (position > k) {
            ((EditBoxAccessor) this).ott$setDisplayPos(((EditBoxAccessor) this).ott$getDisplayPos() + position - k);
        } else if (position <= ((EditBoxAccessor) this).ott$getDisplayPos()) {
            ((EditBoxAccessor) this).ott$setDisplayPos(((EditBoxAccessor) this).ott$getDisplayPos() - (((EditBoxAccessor) this).ott$getDisplayPos() - position));
        }

        ((EditBoxAccessor) this).ott$setDisplayPos(Mth.clamp(((EditBoxAccessor) this).ott$getDisplayPos(), 0, i));
    }

    public int getScreenX(int charNum) {
        return charNum > ((EditBoxAccessor) this).ott$getValue().length() ? this.getX() : this.getX() + FormattedStringDecomposer.stringWidth(((EditBoxAccessor) this).ott$getFont(), ((EditBoxAccessor) this).ott$getValue().substring(0, charNum), 0);
    }
}
