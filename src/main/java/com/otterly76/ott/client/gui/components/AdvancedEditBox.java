package com.otterly76.ott.client.gui.components;

import com.otterly76.ott.mixin.client.EditBoxAccessor;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class AdvancedEditBox extends EditBox {
    protected long lastClickTime;
    protected boolean doubleClick;
    protected int doubleClickHighlightPos;
    protected int doubleClickCursorPos;

    public AdvancedEditBox(Font font, int x, int y, int width, int height, Component message) {
        super(font, x, y, width, height, message);
    }

    public AdvancedEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox editBox, Component message) {
        super(font, x, y, width, height, editBox, message);
    }

    protected void deleteText(int charCount) {
        if (Screen.hasControlDown()) {
            if (charCount < 0) {
                this.deleteChars(-((EditBoxAccessor) this).ott$getCursorPos());
            }
        } else if (Screen.hasAltDown()) {
            this.deleteWords(charCount);
        } else {
            this.deleteChars(charCount);
        }

    }

    @Override
    public int getWordPosition(int numWords) {
        return this.getWordPosition(numWords, this.getCursorPosition());
    }

    protected int getWordPosition(int numWords, int pos) {
        int i = pos;
        boolean backwards = numWords < 0;
        int skippedWords = Math.abs(numWords);

        for (int k = 0; k < skippedWords; ++k) {
            if (!backwards) {
                int l = ((EditBoxAccessor) this).ott$getValue().length();
                while (i < l && isWordChar(((EditBoxAccessor) this).ott$getValue().charAt(i))) {
                    ++i;
                }
            } else {
                while (i > 0 && isWordChar(((EditBoxAccessor) this).ott$getValue().charAt(i - 1))) {
                    --i;
                }
            }
        }

        return i;
    }

    private static boolean isWordChar(char charAt) {
        return charAt == '_' || Character.isAlphabetic(charAt) || Character.isDigit(charAt);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.isActive() && this.isFocused()) {
            switch (keyCode) {
                case 262: // Right
                case 263: // Left
                    boolean right = keyCode == 262;
                    boolean allowedToMove = true;
                    if (!Screen.hasShiftDown() && ((EditBoxAccessor) this).ott$getHighlightPos() != ((EditBoxAccessor) this).ott$getCursorPos()) {
                        int pos = right ? Math.max(this.getCursorPosition(), ((EditBoxAccessor) this).ott$getHighlightPos()) : Math.min(this.getCursorPosition(), ((EditBoxAccessor) this).ott$getHighlightPos());
                        this.setCursorPosition(pos);
                        this.setHighlightPos(this.getCursorPosition());
                        allowedToMove = false;
                    }

                    if (Screen.hasControlDown()) {
                        if (right) this.moveCursorToEnd(Screen.hasShiftDown()); else this.moveCursorToStart(Screen.hasShiftDown());
                    } else if (Screen.hasAltDown()) {
                        this.moveCursorTo(this.getWordPosition(right ? 1 : -1), Screen.hasShiftDown());
                    } else if (allowedToMove) {
                        this.moveCursor(right ? 1 : -1, Screen.hasShiftDown());
                    }

                    return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        this.updateDoubleClickStatus();
    }

    protected void updateDoubleClickStatus() {
        long millis = Util.getMillis();
        boolean tripleClick = this.doubleClick;
        this.doubleClick = millis - this.lastClickTime < 250L;
        if (this.doubleClick) {
            if (tripleClick) {
                this.moveCursorToEnd(false);
                this.setHighlightPos(0);
            } else {
                this.doubleClickHighlightPos = this.getWordPosition(1, this.getCursorPosition());
                this.moveCursorTo(this.doubleClickHighlightPos, false);
                this.doubleClickCursorPos = this.getWordPosition(-1, this.getCursorPosition());
                this.moveCursorTo(this.doubleClickCursorPos, true);
            }
        }

        this.lastClickTime = millis;
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        int i = Mth.floor(mouseX) - this.getX();
        if (((EditBoxAccessor) this).ott$isBordered()) {
            i -= 4;
        }

        String string = ((EditBoxAccessor) this).ott$getFont().plainSubstrByWidth(((EditBoxAccessor) this).ott$getValue().substring(((EditBoxAccessor) this).ott$getDisplayPos()), this.getInnerWidth());
        int mousePosition = ((EditBoxAccessor) this).ott$getFont().plainSubstrByWidth(string, i).length() + ((EditBoxAccessor) this).ott$getDisplayPos();
        this.handleDragSelection(mousePosition, mouseX, mouseY);
    }

    protected void handleDragSelection(int mousePosition, double mouseX, double mouseY) {
        if (this.doubleClick) {
            if (this.clicked(mouseX, mouseY)) {
                int rightBoundary = this.getWordPosition(1, mousePosition);
                this.moveCursorTo(Math.max(this.doubleClickHighlightPos, rightBoundary), false);
                int leftBoundary = this.getWordPosition(-1, mousePosition);
                this.moveCursorTo(Math.min(this.doubleClickCursorPos, leftBoundary), true);
            } else {
                if (mousePosition > this.doubleClickHighlightPos) {
                    this.moveCursorToEnd(false);
                } else {
                    this.moveCursorTo(this.doubleClickHighlightPos, false);
                }

                if (mousePosition < this.doubleClickCursorPos) {
                    this.moveCursorToStart(true);
                } else {
                    this.moveCursorTo(this.doubleClickCursorPos, true);
                }
            }
        } else if (this.clicked(mouseX, mouseY)) {
            this.moveCursorTo(mousePosition, true);
        } else if (((EditBoxAccessor) this).ott$getHighlightPos() < mousePosition) {
            this.moveCursorToEnd(true);
        } else {
            this.moveCursorToStart(true);
        }
    }
}