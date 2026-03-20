package com.otterly76.ott.handler;


import com.otterly76.ott.network.S2CAnvilRepairMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public class ItemInteractionHandler {

    public static boolean isValidRepairItem(BlockState state, ItemStack stack) {
        if (stack.is(Items.IRON_BLOCK)) {
            return state.is(Blocks.DAMAGED_ANVIL) || state.is(Blocks.CHIPPED_ANVIL);
        }

        if (stack.is(Items.COPPER_BLOCK)) {
            return isCopperAnvilOfState(state, "");
        }
        if (stack.is(Items.EXPOSED_COPPER)) {
            return isCopperAnvilOfState(state, "exposed");
        }
        if (stack.is(Items.WEATHERED_COPPER)) {
            return isCopperAnvilOfState(state, "weathered");
        }
        if (stack.is(Items.OXIDIZED_COPPER)) {
            return isCopperAnvilOfState(state, "oxidized");
        }

        return false;
    }

    private static boolean isCopperAnvilOfState(BlockState state, String weatheringState) {
        for (Map.Entry<String, Supplier<? extends net.minecraft.world.level.block.Block>> entry : com.otterly76.ott.block.ModBlocks.COPPER_ANVILS.entrySet()) {
            if (state.is(entry.getValue().get())) {
                String key = entry.getKey();
                if (weatheringState.isEmpty()) {
                    return !key.contains("exposed") && !key.contains("weathered") && !key.contains("oxidized");
                }
                return key.contains(weatheringState);
            }
        }
        return false;
    }

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
        } else if (oldBlockState.is(Blocks.CHIPPED_ANVIL)) {
            newBlockState = Blocks.ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, oldBlockState.getValue(AnvilBlock.FACING));
        } else {
            // Check copper anvils
            for (java.util.Map.Entry<String, java.util.function.Supplier<? extends net.minecraft.world.level.block.Block>> entry : com.otterly76.ott.block.ModBlocks.COPPER_ANVILS.entrySet()) {
                if (oldBlockState.is(entry.getValue().get())) {
                    String key = entry.getKey();
                    String newKey = null;
                    if (key.contains("damaged_")) {
                        // Damaged -> Chipped
                        newKey = key.replace("damaged_", "chipped_");
                    } else if (key.contains("chipped_")) {
                        // Chipped -> Normal
                        newKey = key.replace("chipped_", "");
                    }

                    if (newKey != null) {
                        net.minecraft.world.level.block.Block newBlock = com.otterly76.ott.block.ModBlocks.COPPER_ANVILS.get(newKey).get();
                        return newBlock.defaultBlockState().setValue(AnvilBlock.FACING, oldBlockState.getValue(AnvilBlock.FACING));
                    }
                    return null;
                }
            }
            return null;
        }

        return newBlockState;
    }
}