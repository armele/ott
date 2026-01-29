package com.otterly76.ott.neoforge.impl.event;

import com.otterly76.ott.Ott;

import com.otterly76.ott.feature.home.HomeCommand;
import com.otterly76.ott.mixin.common.ItemInvoker;
import com.otterly76.ott.neoforge.impl.network.ClientboundSyncNutritionPacket;
import com.otterly76.ott.util.FloodingManager;
import com.otterly76.ott.worldgen.surface.SurfaceRuleManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = com.otterly76.ott.api.core.Constants.MOD_ID)
public class ServerGameEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        HomeCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        SurfaceRuleManager.applySurfaceRules(event.getServer());
    }

    @SubscribeEvent
    public static void onWorldTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            FloodingManager.tick(serverLevel);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();

        // Check horizontal neighbors for water to trigger flooding
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getFluidState(pos.relative(direction)).is(Fluids.WATER)) {
                FloodingManager.scheduleFlooding(level, pos, 0);
                break;
            }
        }
    }

    @SubscribeEvent
    public static void onBucketUse(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();

        if (!level.isClientSide && stack.is(Items.BUCKET)) {
            // Using Invoker to bypass 'protected' access
            BlockHitResult hitResult = ItemInvoker.callGetPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                FluidState fluidState = level.getFluidState(pos);

                if (fluidState.is(Fluids.WATER) && fluidState.isSource()) {
                    // Instantly schedule a flood to refill the source we just took
                    FloodingManager.scheduleFlooding(level, pos, 0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (!event.getEntity().getType().is(Ott.TRAMPLING_ENTITIES)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new ClientboundSyncNutritionPacket(
                    serverPlayer.getFoodData().getSaturationLevel(),
                    serverPlayer.getFoodData().getExhaustionLevel()
            ));
        }
    }
}




