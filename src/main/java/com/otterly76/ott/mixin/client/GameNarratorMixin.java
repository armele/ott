package com.otterly76.ott.mixin.client;

import com.mojang.text2speech.Narrator;
import net.minecraft.client.GameNarrator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameNarrator.class)
public class GameNarratorMixin {

    /**
     * Suppresses the spoken "Narrator: On/Off" announcement that fires whenever
     * updateNarratorStatus is called (including on startup when the narrator is
     * initialised). The toast is suppressed separately by NarratorToastMixin.
     */
    @Redirect(
        method = "updateNarratorStatus",
        at = @At(value = "INVOKE", target = "Lcom/mojang/text2speech/Narrator;say(Ljava/lang/String;Z)V")
    )
    private void ott$suppressNarratorStatusSpeech(Narrator narrator, String text, boolean interrupt) {
        // swallow the announcement — narrator still functions normally for all other speech
    }
}
