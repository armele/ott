package com.otterly76.ott.mixin.common;

import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.regex.Pattern;

@Mixin(ChatFormatting.class)
public class ChatFormattingMixin {

    // Uses codepoint 9999 instead of 167 (§) so that § is preserved in the string after stripping
    @Unique
    private static final Pattern OTT$STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)" + ((char) 9999) + "[0-9A-FK-OR]");

    @Inject(at = @At("TAIL"), method = "stripFormatting", cancellable = true)
    private static void ott$stripFormatting(String text, CallbackInfoReturnable<String> cir) {
        if (text != null) {
            cir.setReturnValue(OTT$STRIP_FORMATTING_PATTERN.matcher(text).replaceAll(""));
        }
    }
}
