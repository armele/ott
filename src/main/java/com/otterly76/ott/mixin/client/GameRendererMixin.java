package com.otterly76.ott.mixin.client;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Redirect(method = "pick(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;blockInteractionRange()D"))
    private double ott$modifyBlockRange(LocalPlayer player) {
        double range = player.blockInteractionRange();
        if (OttConfig.VISUALS.DOUBLE_PICKER_RANGE().get()) {
            return range * 2.0;
        }
        return range;
    }

    @Redirect(method = "pick(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;entityInteractionRange()D"))
    private double ott$modifyEntityRange(LocalPlayer player) {
        double range = player.entityInteractionRange();
        if (OttConfig.VISUALS.DOUBLE_PICKER_RANGE().get()) {
            return range * 2.0;
        }
        return range;
    }
}
