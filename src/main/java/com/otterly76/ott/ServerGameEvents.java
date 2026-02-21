package com.otterly76.ott;

import com.otterly76.ott.command.HomeCommand;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.handler.ItemInteractionHandler;
import com.otterly76.ott.mixin.common.ItemInvoker;
import com.otterly76.ott.network.ClientboundSyncNutritionPacket;
import com.otterly76.ott.network.S2COpenNameTagEditorMessage;
import com.otterly76.ott.util.worldgen.FloodingManager;
import com.otterly76.ott.worldgen.surface.SurfaceRuleManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.AnvilRepairEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class ServerGameEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        HomeCommand.register(event.getDispatcher());
        com.otterly76.ott.command.AFKCommand.register(event.getDispatcher());
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
    public static void onAnvilRepair(PlayerInteractEvent.RightClickBlock event) {
        if (!OttConfig.ANVILS.MISC.ANVIL_REPAIRING.get()) return;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();

        if (stack.is(Items.IRON_BLOCK)) {
            if (state.is(net.minecraft.tags.BlockTags.ANVIL) && ItemInteractionHandler.tryRepairAnvil(level, pos, state)) {
                event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
                event.setCanceled(true);

                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
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

    @SubscribeEvent
    public static void onNameTagUse(PlayerInteractEvent.RightClickItem event) {
        if (!OttConfig.ANVILS.MISC.EDIT_NAME_TAGS_NO_ANVIL.get()) return;

        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();

        if (player.isShiftKeyDown() && stack.is(Items.NAME_TAG)) {
            if (!level.isClientSide) {
                PacketDistributor.sendToPlayer((ServerPlayer) player, new S2COpenNameTagEditorMessage(event.getHand(), stack.getHoverName()));
            }
            event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (!OttConfig.ANVILS.MISC.NAME_TAGS_DROP_FROM_MOBS.get()) return;

        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player) && entity.hasCustomName()) {
            ItemStack itemStack = new ItemStack(Items.NAME_TAG);
            itemStack.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, entity.getCustomName());
            ItemEntity itemEntity = new ItemEntity(entity.level(), entity.getX(), entity.getEyeY(), entity.getZ(), itemStack);
            itemEntity.setDefaultPickUpDelay();
            event.getDrops().add(itemEntity);
        }
    }


    @SubscribeEvent
    public static void onAnvilRepairEvent(AnvilRepairEvent event) {
        if (OttConfig.ANVILS.MISC.RISK_FREE_ANVIL_RENAMING.get() && event.getRight().isEmpty()) {
            event.setBreakChance(0.0F);
        } else {
            event.setBreakChance(OttConfig.ANVILS.MISC.ANVIL_BREAK_CHANCE.get().floatValue());
        }
    }
}