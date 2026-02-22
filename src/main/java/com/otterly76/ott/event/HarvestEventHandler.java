package com.otterly76.ott.event;

import com.otterly76.ott.Ott;
import com.otterly76.ott.config.ConfigHandler.Harvest;
import com.otterly76.ott.util.block.HarvestUtils;
import com.otterly76.ott.util.block.HarvestUtils.InteractionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class HarvestEventHandler {
    private static boolean isHarvesting = false;

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ClickResult result = rightClickBlock(event.getEntity(), event.getHand(), event.getPos(), event.getHitVec());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setCancellationResult(result.getInteractionResult());
            event.setUseBlock(TriState.FALSE);
            event.setUseItem(TriState.FALSE);
        }
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!Harvest.safeHarvest()) return;
        Player player = event.getPlayer();
        if (player.isCreative() || player.isShiftKeyDown()) return;

        BlockState state = event.getState();
        if (isImmatureCrop(state)) {
            event.setCanceled(true);
        }
    }

    private static boolean isImmatureCrop(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof CropBlock crop) {
            return !crop.isMaxAge(state);
        }

        // Check if any state of this block is in our harvestable crops map
        for (BlockState harvestableState : Harvest.getCrops().keySet()) {
            if (harvestableState.getBlock() == block) {
                // If current state IS one of the harvestable states, it's not immature
                if (state.equals(harvestableState)) {
                    return false;
                }

                // If block has an age property, check if current age is less than harvestable age
                for (Property<?> prop : state.getProperties()) {
                    if (prop instanceof IntegerProperty intProp && (prop.getName().equals("age") || prop.getName().equals("growth"))) {
                        if (harvestableState.hasProperty(intProp)) {
                            int currentAge = state.getValue(intProp);
                            int harvestAge = harvestableState.getValue(intProp);
                            if (currentAge < harvestAge) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public static ClickResult rightClickBlock(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitResult) {
        if (!player.level().isClientSide() && !isHarvesting && HarvestUtils.playerCanHarvest(player)) {
            isHarvesting = true;
            ClickResult result = getClickResult(player, hand, pos, hitResult);
            isHarvesting = false;
            return result;
        } else {
            return ClickResult.pass();
        }
    }

    private static ClickResult getClickResult(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitResult) {
        if (player != null && hand != null && !player.isSpectator()) {
            if (hitResult.getType() == Type.BLOCK && hitResult.getBlockPos().equals(pos)) {
                Level level = player.level();
                BlockState blockState = level.getBlockState(pos);
                BlockState modifiedState = HarvestUtils.getToolModifiedState(blockState, new UseOnContext(player, hand, hitResult), "hoe_till", true);
                if (modifiedState != null) {
                    return ClickResult.pass();
                } else {
                    ItemStack heldStack = player.getItemInHand(hand);
                    boolean isHoe = HarvestUtils.isHoe(heldStack);
                    if (!Harvest.allowEmptyHand() && !isHoe) {
                        return ClickResult.pass();
                    } else {
                        BlockState above = level.getBlockState(pos.above());
                        int range = 1;
                        if (isHoe) {
                            if (HarvestUtils.getInteractionTypeForBlock(blockState, true) == InteractionType.NONE && HarvestUtils.getInteractionTypeForBlock(above, true) == InteractionType.NONE) {
                                return ClickResult.pass();
                            }

                            range = HarvestUtils.getHoeRange(heldStack);
                        }

                        boolean harvested = false;

                        for(int x = 1 - range; x < range; ++x) {
                            for(int z = 1 - range; z < range; ++z) {
                                BlockPos shiftPos = pos.offset(x, 0, z);
                                if (!tryHarvest(level, shiftPos, player, hand, range > 1)) {
                                    shiftPos = shiftPos.above();
                                    if (tryHarvest(level, shiftPos, player, hand, range > 1)) {
                                        harvested = true;
                                    }
                                } else {
                                    harvested = true;
                                }
                            }
                        }

                        if (!harvested) {
                            return ClickResult.pass();
                        } else if (HarvestUtils.isBlockItem(heldStack)) {
                            return ClickResult.pass();
                        } else {
                            return ClickResult.interrupt();
                        }
                    }
                }
            } else {
                return ClickResult.pass();
            }
        } else {
            return ClickResult.pass();
        }
    }

    private static boolean tryHarvest(Level level, BlockPos pos, @Nullable LivingEntity entity, @Nullable InteractionHand hand, boolean canReach) {
        if (entity instanceof Player player) {
            if (!level.mayInteract(player, pos)) {
                return false;
            }
        }

        Pair<BlockState, Boolean> blockStatePair = HarvestUtils.getModifiedState(level.getBlockState(pos));
        BlockState blockState = blockStatePair.getLeft();
        HarvestUtils.InteractionType interactionType = HarvestUtils.getInteractionTypeForBlock(blockState, canReach);
        if (interactionType != InteractionType.NONE) {
            if (interactionType == InteractionType.HARVEST) {
                if (entity instanceof Player) {
                    return harvestAndReplant(level, pos, blockState, entity, hand, blockStatePair.getRight());
                }
            } else if (interactionType == InteractionType.CLICK && entity instanceof Player) {
                BlockHitResult hitResult = new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, true);
                if (hand == null) {
                    hand = InteractionHand.MAIN_HAND;
                }

                if (entity instanceof ServerPlayer sp) {
                    return sp.gameMode.useItemOn(sp, sp.level(), sp.getItemInHand(hand), hand, hitResult).consumesAction();
                }
            }
        }

        return false;
    }

    private static boolean harvestAndReplant(Level level, BlockPos pos, BlockState blockState, LivingEntity entity, InteractionHand hand, boolean treeCrop) {
        BlockState cropBlockState = Harvest.getCrops().get(blockState);
        BlockState above = level.getBlockState(pos.above());
        BooleanProperty top = BooleanProperty.create("top");
        if (!treeCrop && above.getBlock() instanceof CropBlock) {
            cropBlockState = Harvest.getCrops().get(above);
        }

        if (cropBlockState == null) {
            return false;
        } else {
            if (level instanceof ServerLevel serverLevel) {
                ItemStack heldStack = null;
                ItemStack copy;
                if (entity != null && hand != null) {
                    heldStack = entity.getItemInHand(hand);
                    if (Harvest.isBlacklistHeldItem(heldStack)) {
                        return false;
                    }

                    copy = entity.getItemInHand(hand).copy();
                } else {
                    copy = new ItemStack(Items.STICK);
                }

                MutableBoolean hasTaken = new MutableBoolean(false);
                Item blockItem = blockState.getBlock().asItem();
                boolean dropXp = entity instanceof Player;
                if (!cropBlockState.hasProperty(top)) {
                    Block.getDrops(blockState, serverLevel, pos, level.getBlockEntity(pos), entity, copy).forEach((stack) -> {
                        if (stack.getItem() == blockItem && !hasTaken.getValue()) {
                            stack.shrink(1);
                            hasTaken.setValue(true);
                        }

                        if (!stack.isEmpty()) {
                            Block.popResource(level, pos, stack);
                        }

                    });
                    blockState.spawnAfterBreak(serverLevel, pos, copy, dropXp);
                }

                if (dropXp && Harvest.xpFromHarvestUseRange()) {
                    int xp = ThreadLocalRandom.current().nextInt(Harvest.xpFromHarvestRangeAmount().getLeft(), Harvest.xpFromHarvestRangeAmount().getRight() + 1);
                    if (xp > 0) {
                        ExperienceOrb.award(serverLevel, Vec3.atCenterOf(pos), xp);
                    }
                } else if (dropXp && Ott.RANDOM.nextInt(100) + 1 <= Harvest.xpFromHarvestChance()) {
                    ExperienceOrb.award(serverLevel, Vec3.atCenterOf(pos), Harvest.xpFromHarvestAmount());
                }

                if (cropBlockState.hasProperty(DoublePlantBlock.HALF)) {
                    BlockPos blockPos = pos;
                    if (cropBlockState.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                        blockPos = pos.below();
                    }

                    BlockState doubleBlockHalf = level.getBlockState(pos);
                    level.levelEvent(2001, blockPos, Block.getId(doubleBlockHalf));
                    level.gameEvent(GameEvent.BLOCK_DESTROY, blockPos, Context.of(entity, doubleBlockHalf));
                    level.destroyBlock(blockPos, true, entity);
                    if (Harvest.replantCrops()) {
                        level.setBlock(blockPos, doubleBlockHalf.getBlock().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 0);
                    } else {
                        level.destroyBlock(blockPos, false, entity);
                    }
                } else {
                    level.levelEvent(2001, pos, Block.getId(cropBlockState));
                    if (Harvest.replantCrops()) {
                        level.setBlockAndUpdate(pos, cropBlockState);
                    } else {
                        level.destroyBlock(pos, false, entity);
                    }

                    level.gameEvent(GameEvent.BLOCK_DESTROY, pos, Context.of(entity, blockState));
                }

                if (cropBlockState.hasProperty(top)) {
                    level.destroyBlock(pos, true, entity);
                }

                if (!level.isClientSide && heldStack != null && Harvest.damageTool() && HarvestUtils.isHoe(heldStack)) {
                    heldStack.hurtAndBreak(1, entity, EquipmentSlot.MAINHAND);
                }
            }

            return true;
        }
    }
}