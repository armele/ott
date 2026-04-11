package com.otterly76.ott.mixin.client;

import com.otterly76.ott.client.FormattingExamplesHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class UnlockedTypingScreenMixin {

    @Shadow
    protected Font font;

    @Inject(at = @At("TAIL"), method = "render")
    private void ott$render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        Screen self = (Screen) (Object) this;
        if (self instanceof BookEditScreen || self instanceof SignEditScreen) {
            FormattingExamplesHelper.renderFormattingExamples(guiGraphics, this.font);
        }
    }
}
