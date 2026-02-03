package com.otterly76.ott.events;

import com.otterly76.ott.Constants;
import com.otterly76.ott.util.FluidLanternManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class FluidLanternEvents {

    @SubscribeEvent
    public static void onFluidPlace(BlockEvent.FluidPlaceBlockEvent event) {
        BlockPos pos = event.getPos();
        BlockState newState = event.getNewState();
        FluidState fluidState = newState.getFluidState();
        if (fluidState.getType() == Fluids.WATER || fluidState.getType() == Fluids.FLOWING_WATER) {
            if (FluidLanternManager.isWaterProtected(pos)) {
                if (newState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    event.setNewState(newState.setValue(BlockStateProperties.WATERLOGGED, false));
                } else {
                    event.setNewState(Blocks.AIR.defaultBlockState());
                }
            }
        } else if (fluidState.getType() == Fluids.LAVA || fluidState.getType() == Fluids.FLOWING_LAVA) {
            if (FluidLanternManager.isLavaProtected(pos)) {
                if (newState.getFluidState().isSource() || newState.getBlock() instanceof net.minecraft.world.level.block.LiquidBlock) {
                    event.setNewState(Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

}