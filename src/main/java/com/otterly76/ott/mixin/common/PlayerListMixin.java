package com.otterly76.ott.mixin.common;


import com.otterly76.ott.neoforge.impl.config.OttConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "broadcastAll(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void ott$onBroadcastAll(net.minecraft.network.protocol.Packet<?> packet, CallbackInfo ci) {
        if (OttConfig.ACCESSIBILITY.LOCAL_GLOBAL_SOUNDS.get() && packet instanceof ClientboundLevelEventPacket levelEventPacket) {
            int type = levelEventPacket.getType();
            // 1023: Wither spawn, 1028: Ender Dragon death, 1038: Wither death
            if (type == 1023 || type == 1028 || type == 1038) {
                BlockPos pos = levelEventPacket.getPos();
                PlayerList self = (PlayerList) (Object) this;
                
                double radius = 64.0; // Standard local sound radius
                
                for (ServerPlayer player : self.getPlayers()) {
                    double dx = pos.getX() - player.getX();
                    double dy = pos.getY() - player.getY();
                    double dz = pos.getZ() - player.getZ();
                    if (dx * dx + dy * dy + dz * dz < radius * radius) {
                        player.connection.send(packet);
                    }
                }
                ci.cancel();
            }
        }
    }
}

