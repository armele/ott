package com.otterly76.ott.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignEditScreen.class)
public abstract class UnlockedTypingSignEditMixin {

    @Unique
    private static final int OTT$OFFSET = 100;

    @Shadow
    @Final
    private SignBlockEntity sign;

    @Shadow
    @Final
    private String[] messages;

    @Shadow
    protected abstract Vector3f getSignTextScale();

    @Inject(at = @At("TAIL"), method = "renderSignText")
    private void ott$renderPreformattedText(GuiGraphics guiGraphics, CallbackInfo ci) {
        Font font = Minecraft.getInstance().font;
        String helperTitleStr = Component.translatable("screen.ott.unlockedTyping.preformattedTextTitle").getString();
        FormattedCharSequence helperTitle = FormattedCharSequence.forward(helperTitleStr, Style.EMPTY.withUnderlined(true));

        guiGraphics.pose().translate(0f, 0f, 4f);
        Vector3f scale = getSignTextScale();
        guiGraphics.pose().scale(scale.x, scale.y, scale.z);
        int textColor = DyeColor.LIGHT_GRAY.getTextColor();
        int yOffset = 4 * this.sign.getTextLineHeight() / 2;

        guiGraphics.drawString(font, helperTitle,
                -font.width(helperTitleStr) / 2 + OTT$OFFSET, yOffset - 51, textColor, false);

        for (int i = 0; i < this.messages.length; ++i) {
            String message = this.messages[i];
            if (message != null) {
                if (font.isBidirectional()) {
                    message = font.bidirectionalShaping(message);
                }
                FormattedCharSequence seq = FormattedCharSequence.forward(message, Style.EMPTY);
                guiGraphics.drawString(font, seq,
                        -font.width(message) / 2 + OTT$OFFSET, i * this.sign.getTextLineHeight() - yOffset,
                        textColor, false);
            }
        }
    }
}
