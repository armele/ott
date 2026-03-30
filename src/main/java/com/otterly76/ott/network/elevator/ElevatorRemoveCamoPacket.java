package com.otterly76.ott.network.elevator;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.entity.ElevatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ElevatorRemoveCamoPacket(BlockPos pos) implements CustomPacketPayload {

    public static final Type<ElevatorRemoveCamoPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "elevator_remove_camo"));

    public static final StreamCodec<FriendlyByteBuf, ElevatorRemoveCamoPacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, ElevatorRemoveCamoPacket::pos,
                    ElevatorRemoveCamoPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ElevatorRemoveCamoPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.level().getBlockEntity(packet.pos()) instanceof ElevatorBlockEntity be) {
                    be.removeCamo();
                    player.level().sendBlockUpdated(packet.pos(), be.getBlockState(), be.getBlockState(), 3);
                }
            }
        });
    }
}
