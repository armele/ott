package com.otterly76.ott.mixin.common;

import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.registry.ModAttachmentTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.SleepStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SleepStatus.class)
public class SleepStatusMixin {
    @Shadow private int activePlayers;
    @Shadow private int sleepingPlayers;

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void ott$update(List<ServerPlayer> players, CallbackInfoReturnable<Boolean> cir) {
        if (!OttConfig.afk.EXCLUDE_FROM_SLEEP.get()) return;

        int oldActive = this.activePlayers;
        int oldSleeping = this.sleepingPlayers;
        this.activePlayers = 0;
        this.sleepingPlayers = 0;

        for (ServerPlayer serverplayer : players) {
            var afkState = serverplayer.getData(ModAttachmentTypes.AFK_STATE);
            if (!serverplayer.isSpectator() && !afkState.isAfk()) {
                this.activePlayers++;
                if (serverplayer.isSleeping()) {
                    this.sleepingPlayers++;
                }
            }
        }

        cir.setReturnValue((oldSleeping > 0 || this.sleepingPlayers > 0) && (oldActive != this.activePlayers || oldSleeping != this.sleepingPlayers));
    }
}
