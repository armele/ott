package com.otterly76.ott.neoforge.impl.event;


import com.otterly76.ott.api.core.Constants;
import com.otterly76.ott.neoforge.impl.config.OttConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class LavaWarningHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!OttConfig.GENERAL.ENABLE_LAVA_WARNINGS.get()) {
            return;
        }

        Player player = event.getEntity();
        Level level = player.level();

        if (level.isClientSide || player.isSpectator() || level.getGameTime() % 5 != 0) {
            return;
        }

        BlockPos pos = player.blockPosition();
        Direction forward = player.getDirection();

        // Check urgency levels (1 = closest, 3 = furthest)
        for (int dist = 1; dist <= 3; dist++) {
            if (checkLavaNearby(level, player, pos, forward, dist)) {
                break; // Stop once the most urgent warning is sent
            }
        }
    }

    private static boolean checkLavaNearby(Level level, Player player, BlockPos pos, Direction forward, int dist) {
        // We adjust the math so 'dist 1' = 1 block of safety.

        // Check Above (Head)
        // pos is feet, pos.above(1) is head, pos.above(2) is ceiling.
        // pos.above(3) is the first block of safety above the ceiling.
        if (isLava(level, pos.above(dist + 2))) {
            player.displayClientMessage(Component.translatable("message.ott.lava_warning.head." + dist), true);
            return true;
        }

        // Check Below (Feet)
        // pos.below(1) is the block you stand on.
        // pos.below(2) is the block under your floor.
        if (isLava(level, pos.below(dist + 1))) {
            player.displayClientMessage(Component.translatable("message.ott.lava_warning.feet." + dist), true);
            return true;
        }

        // Check Forward (Face)
        // pos.relative(1) is the block in front of you.
        // pos.relative(2) is the block behind that wall.
        if (isLava(level, pos.relative(forward, dist + 1)) || isLava(level, pos.above().relative(forward, dist + 1))) {
            player.displayClientMessage(Component.translatable("message.ott.lava_warning.face." + dist), true);
            return true;
        }

        return false;
    }

    private static boolean isLava(Level level, BlockPos pos) {
        return level.getFluidState(pos).is(Fluids.LAVA) || level.getFluidState(pos).is(Fluids.FLOWING_LAVA);
    }
}

