package com.otterly76.ott.network.elevator;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.entity.ElevatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ElevatorSetFacingPacket(BlockPos pos, Direction facing) implements CustomPacketPayload {

    public static final Type<ElevatorSetFacingPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "elevator_set_facing"));

    private static final StreamCodec<FriendlyByteBuf, Direction> DIRECTION_CODEC =
            StreamCodec.of(
                    (buf, dir) -> buf.writeByte(dir.get3DDataValue()),
                    buf -> Direction.from3DDataValue(buf.readByte() & 0xFF)
            );

    public static final StreamCodec<FriendlyByteBuf, ElevatorSetFacingPacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, ElevatorSetFacingPacket::pos,
                    DIRECTION_CODEC, ElevatorSetFacingPacket::facing,
                    ElevatorSetFacingPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ElevatorSetFacingPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.level().getBlockEntity(packet.pos()) instanceof ElevatorBlockEntity be) {
                    Direction dir = packet.facing();
                    if (dir.getAxis() != Direction.Axis.Y) {
                        be.setFacing(dir);
                        player.level().sendBlockUpdated(packet.pos(), be.getBlockState(), be.getBlockState(), 3);
                    }
                }
            }
        });
    }
}
