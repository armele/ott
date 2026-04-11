package com.otterly76.ott.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookEditScreen.class)
public abstract class UnlockedTypingBookEditMixin extends Screen {

    protected UnlockedTypingBookEditMixin() {
        super(Component.empty());
    }

    @Shadow
    private boolean isSigning;

    @Shadow
    private Component pageMsg;

    @Shadow
    private String title;

    @Shadow
    protected abstract BookEditScreen.DisplayCache getDisplayCache();

    @Unique
    private static final int OTT$OFFSET = 140;

    @SuppressWarnings("all")
    @ModifyConstant(method = "lambda$new$3", constant = @Constant(intValue = 16), require = 0)
    private static int ott$titleLengthVerification(int constant) {
        return 30;
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void ott$renderPreformattedText(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.isSigning) {
            ott$renderSigningDisplay(guiGraphics);
        } else {
            ott$renderWritingDisplay(guiGraphics);
        }
    }

    @Unique
    private void ott$renderSigningDisplay(GuiGraphics guiGraphics) {
        var font = Minecraft.getInstance().font;
        int i = (this.width - 192) / 2;
        FormattedCharSequence titleSeq = FormattedCharSequence.forward(this.title, Style.EMPTY);
        String helperTitle = Component.translatable("screen.ott.unlockedTyping.preformattedTextTitle").getString();
        guiGraphics.drawString(font, FormattedCharSequence.forward(helperTitle, Style.EMPTY.withUnderlined(true)),
                (i + 36 + (114 - 90) / 2) + OTT$OFFSET, 39, DyeColor.LIGHT_GRAY.getTextColor(), false);
        guiGraphics.drawString(font, titleSeq,
                (i + 36 + (114 - 90) / 2) + OTT$OFFSET, 50, DyeColor.LIGHT_GRAY.getTextColor(), false);
        guiGraphics.drawString(font, Component.literal(this.title),
                (i + 36 + (114 - 90) / 2) + OTT$OFFSET, 68, 0xFFFFFFFF, false);
    }

    @Unique
    private void ott$renderWritingDisplay(GuiGraphics guiGraphics) {
        var font = Minecraft.getInstance().font;
        int i = (this.width - 192) / 2;
        int n = font.width(this.pageMsg);
        String helperTitle = Component.translatable("screen.ott.unlockedTyping.preformattedTextTitle").getString();
        guiGraphics.drawString(font, FormattedCharSequence.forward(helperTitle, Style.EMPTY.withUnderlined(true)),
                (i + 192 - 44) + 27, 18, DyeColor.LIGHT_GRAY.getTextColor(), false);
        guiGraphics.drawString(font, this.pageMsg, i - n + 192 - 44, 18, 0, false);

        BookEditDisplayCacheAccessor cacheAccessor = (BookEditDisplayCacheAccessor) getDisplayCache();
        for (BookEditScreen.LineInfo lineInfo : cacheAccessor.ott$getLines()) {
            BookEditLineInfoAccessor lineAccessor = (BookEditLineInfoAccessor) lineInfo;
            Component original = lineAccessor.ott$getAsComponent();
            FormattedCharSequence seq = FormattedCharSequence.forward(original.getString(), Style.EMPTY);
            guiGraphics.drawString(font, seq,
                    lineAccessor.ott$getX() + OTT$OFFSET, lineAccessor.ott$getY(),
                    DyeColor.LIGHT_GRAY.getTextColor(), false);
        }
    }
}
