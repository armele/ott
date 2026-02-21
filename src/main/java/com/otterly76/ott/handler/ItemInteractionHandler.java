package com.otterly76.ott.handler;


import com.otterly76.ott.network.S2CAnvilRepairMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class ItemInteractionHandler {

    public static boolean tryRepairAnvil(Level level, BlockPos pos, BlockState state) {
        BlockState repairedState = getRepairedState(state);
        if (repairedState != null) {
            if (!level.isClientSide) {
                level.setBlock(pos, repairedState, 2);
                PacketDistributor.sendToPlayersNear((ServerLevel)level, null, pos.getX(), pos.getY(), pos.getZ(), 64.0, new S2CAnvilRepairMessage(pos, repairedState));
            }

            return true;
        } else {
            return false;
        }
    }

    private static @Nullable BlockState getRepairedState(BlockState oldBlockState) {
        BlockState newBlockState;
        if (oldBlockState.is(Blocks.DAMAGED_ANVIL)) {
            newBlockState = Blocks.CHIPPED_ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, oldBlockState.getValue(AnvilBlock.FACING));
        } else {
            if (!oldBlockState.is(Blocks.CHIPPED_ANVIL)) {
                return null;
            }

            newBlockState = Blocks.ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, oldBlockState.getValue(AnvilBlock.FACING));
        }

        return newBlockState;
    }
}