package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.custom.HappyGhast;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerGamePacketListenerImpl.class})
public class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;
    @Shadow
    private int aboveGroundVehicleTickCount;

    @Inject(
        method = {"tick()V"},
        at = {@At("TAIL")}
    )
    private void vb$preventFlyingKick(CallbackInfo ci) {
        Entity vehicle = this.player.getVehicle();
        if (vehicle instanceof HappyGhast ghast) {
            if (!ghast.isBaby()) {
                this.aboveGroundVehicleTickCount = 0;
            }
        }

    }
}
