package com.otterly76.ott.mixin.common;

import net.minecraft.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StringUtil.class)
public class StringUtilMixin {

    @Inject(at = @At("TAIL"), method = "isAllowedChatCharacter", cancellable = true)
    private static void ott$isAllowedChatCharacter(char character, CallbackInfoReturnable<Boolean> cir) {
        if (character == '§') {
            cir.setReturnValue(true);
        }
    }
}
