package com.otterly76.ott.network;

import com.otterly76.ott.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public record S2CAnvilRepairMessage(BlockPos pos, int stateId) implements CustomPacketPayload {
    public static final Type<S2CAnvilRepairMessage> TYPE = new Type<>(Constants.loc("anvil_repair"));

    public S2CAnvilRepairMessage(BlockPos pos, BlockState state) {
        this(pos, Block.getId(state));
    }

    public static final StreamCodec<FriendlyByteBuf, S2CAnvilRepairMessage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, S2CAnvilRepairMessage::pos,
            ByteBufCodecs.VAR_INT, S2CAnvilRepairMessage::stateId,
            S2CAnvilRepairMessage::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
