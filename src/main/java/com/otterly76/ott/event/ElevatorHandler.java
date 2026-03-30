package com.otterly76.ott.event;

import com.otterly76.ott.block.custom.ElevatorBlock;
import com.otterly76.ott.block.entity.ElevatorBlockEntity;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ElevatorHandler {

    // Ticks a player must hold sneak on an elevator before going down
    private static final int SNEAK_TICKS_REQUIRED = 3;
    private static final Map<UUID, Integer> sneakTimer = new HashMap<>();

    /** Called when any living entity jumps. Used to teleport up when on an elevator. */
    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        handleGoUp(player);
    }

    /** Called every server tick per player. Used to detect sneaking on an elevator to go down. */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        BlockPos standingOn = player.blockPosition().below();
        BlockState below = player.level().getBlockState(standingOn);

        if (!(below.getBlock() instanceof ElevatorBlock)) {
            sneakTimer.remove(player.getUUID());
            return;
        }

        if (player.isShiftKeyDown() && !player.isInWater() && !player.isInLava()) {
            int ticks = sneakTimer.merge(player.getUUID(), 1, Integer::sum);
            if (ticks >= SNEAK_TICKS_REQUIRED) {
                sneakTimer.remove(player.getUUID());
                handleGoDown(player);
            }
        } else {
            sneakTimer.remove(player.getUUID());
        }
    }

    private static void handleGoUp(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel level)) return;

        BlockPos currentPos = player.blockPosition();
        BlockPos elevatorPos = currentPos.below();
        BlockState elevatorState = level.getBlockState(elevatorPos);
        if (!(elevatorState.getBlock() instanceof ElevatorBlock sourceBlock)) return;

        int range = OttConfig.ELEVATOR.RANGE.get();
        boolean sameColor = OttConfig.ELEVATOR.SAME_COLOR.get();
        String sourceColor = sourceBlock.getColorName();

        BlockPos target = findElevator(level, elevatorPos, Direction.UP, range, sameColor, sourceColor);
        if (target == null) return;

        teleportPlayer(player, level, target);
    }

    private static void handleGoDown(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel level)) return;

        BlockPos currentPos = player.blockPosition();
        BlockPos elevatorPos = currentPos.below();
        BlockState elevatorState = level.getBlockState(elevatorPos);
        if (!(elevatorState.getBlock() instanceof ElevatorBlock sourceBlock)) return;

        int range = OttConfig.ELEVATOR.RANGE.get();
        boolean sameColor = OttConfig.ELEVATOR.SAME_COLOR.get();
        String sourceColor = sourceBlock.getColorName();

        BlockPos target = findElevator(level, elevatorPos, Direction.DOWN, range, sameColor, sourceColor);
        if (target == null) return;

        teleportPlayer(player, level, target);
    }

    /**
     * Searches in the given direction for an elevator block, up to {@code range} blocks away.
     * The space directly above the target elevator must be open (two air blocks) for the player to land.
     */
    @Nullable
    private static BlockPos findElevator(ServerLevel level, BlockPos from, Direction dir,
            int range, boolean sameColor, String sourceColor) {
        for (int i = 1; i <= range; i++) {
            BlockPos candidate = from.relative(dir, i);
            BlockState candidateState = level.getBlockState(candidate);
            if (!(candidateState.getBlock() instanceof ElevatorBlock candidateBlock)) continue;
            if (sameColor && !candidateBlock.getColorName().equals(sourceColor)) continue;

            // Check that there is room for the player above the target elevator
            int activationRange = OttConfig.ELEVATOR.ACTIVATION_RANGE.get();
            if (!hasRoomAbove(level, candidate, activationRange)) continue;

            return candidate;
        }
        return null;
    }

    private static boolean hasRoomAbove(ServerLevel level, BlockPos elevPos, int height) {
        for (int y = 1; y <= height; y++) {
            if (!level.getBlockState(elevPos.above(y)).isAir()) return false;
        }
        return true;
    }

    private static void teleportPlayer(ServerPlayer player, ServerLevel level, BlockPos targetElevator) {
        double destX = targetElevator.getX() + 0.5;
        double destY = targetElevator.getY() + 1.0;
        double destZ = targetElevator.getZ() + 0.5;

        float yRot = player.getYRot();
        float xRot = player.getXRot();

        // If the target elevator is directional, snap player facing
        if (level.getBlockEntity(targetElevator) instanceof ElevatorBlockEntity be) {
            if (be.isDirectional()) {
                Direction facing = be.getFacing();
                yRot = facing.toYRot();
                if (OttConfig.ELEVATOR.RESET_PITCH_DIRECTIONAL.get()) xRot = 0;
            } else if (OttConfig.ELEVATOR.RESET_PITCH_NORMAL.get()) {
                xRot = 0;
            }
        }

        // Consume XP if configured
        if (OttConfig.ELEVATOR.USE_XP.get()) {
            int cost = OttConfig.ELEVATOR.XP_AMOUNT.get();
            if (player.totalExperience < cost) return;
            player.giveExperiencePoints(-cost);
        }

        player.teleportTo(level, destX, destY, destZ, yRot, xRot);
        // Clear jump velocity so the player lands cleanly on the target elevator
        player.setDeltaMovement(player.getDeltaMovement().multiply(1, 0, 1));

        level.playSound(null, targetElevator, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
    }
}