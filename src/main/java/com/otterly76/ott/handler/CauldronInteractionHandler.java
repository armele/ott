package com.otterly76.ott.handler;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;
import java.util.function.Supplier;

public class CauldronInteractionHandler {
    public static final CauldronInteraction.InteractionMap COPPER_EMPTY = CauldronInteraction.newInteractionMap("copper_empty");
    public static final CauldronInteraction.InteractionMap COPPER_WATER = CauldronInteraction.newInteractionMap("copper_water");
    public static final CauldronInteraction.InteractionMap COPPER_LAVA = CauldronInteraction.newInteractionMap("copper_lava");
    public static final CauldronInteraction.InteractionMap COPPER_POWDER_SNOW = CauldronInteraction.newInteractionMap("copper_powder_snow");

    public static void bootstrap() {
        // 1. Support copper buckets on vanilla cauldrons
        addCopperBucketsToVanilla();

        // 2. Setup Copper Empty Cauldron interactions
        setupCopperEmptyInteractions();

        // 3. Setup Copper Filled Cauldron interactions
        setupCopperWaterInteractions();
        setupCopperLavaInteractions();
        setupCopperPowderSnowInteractions();
    }

    private static void addCopperBucketsToVanilla() {
        CauldronInteraction.EMPTY.map().put(ModItems.COPPER_WATER_BUCKET.get(), CauldronInteraction.FILL_WATER);
        CauldronInteraction.EMPTY.map().put(ModItems.COPPER_LAVA_BUCKET.get(), CauldronInteraction.FILL_LAVA);
        CauldronInteraction.EMPTY.map().put(ModItems.COPPER_POWDER_SNOW_BUCKET.get(), CauldronInteraction.FILL_POWDER_SNOW);

        CauldronInteraction.WATER.map().put(ModItems.COPPER_BUCKET.get(), (state, level, pos, player, hand, stack) ->
                fillBucket(state, level, pos, player, hand, stack, new ItemStack(ModItems.COPPER_WATER_BUCKET.get()), s -> s.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL, Blocks.CAULDRON));

        CauldronInteraction.LAVA.map().put(ModItems.COPPER_BUCKET.get(), (state, level, pos, player, hand, stack) ->
                fillBucket(state, level, pos, player, hand, stack, new ItemStack(ModItems.COPPER_LAVA_BUCKET.get()), s -> true, SoundEvents.BUCKET_FILL_LAVA, Blocks.CAULDRON));

        CauldronInteraction.POWDER_SNOW.map().put(ModItems.COPPER_BUCKET.get(), (state, level, pos, player, hand, stack) ->
                fillBucket(state, level, pos, player, hand, stack, new ItemStack(ModItems.COPPER_POWDER_SNOW_BUCKET.get()), s -> s.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_POWDER_SNOW, Blocks.CAULDRON));
    }

    private static void setupCopperEmptyInteractions() {
        // Water
        CauldronInteraction waterFill = (state, level, pos, player, hand, stack) -> {
            Block filled = getMatchingBlock(state.getBlock(), ModBlocks.COPPER_CAULDRONS, ModBlocks.COPPER_WATER_CAULDRONS);
            return emptyBucket(level, pos, player, hand, stack, filled.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY);
        };
        COPPER_EMPTY.map().put(Items.WATER_BUCKET, waterFill);
        COPPER_EMPTY.map().put(ModItems.COPPER_WATER_BUCKET.get(), waterFill);

        // Lava
        CauldronInteraction lavaFill = (state, level, pos, player, hand, stack) -> {
            Block filled = getMatchingBlock(state.getBlock(), ModBlocks.COPPER_CAULDRONS, ModBlocks.COPPER_LAVA_CAULDRONS);
            return emptyBucket(level, pos, player, hand, stack, filled.defaultBlockState(), SoundEvents.BUCKET_EMPTY_LAVA);
        };
        COPPER_EMPTY.map().put(Items.LAVA_BUCKET, lavaFill);
        COPPER_EMPTY.map().put(ModItems.COPPER_LAVA_BUCKET.get(), lavaFill);

        // Powder Snow
        CauldronInteraction snowFill = (state, level, pos, player, hand, stack) -> {
            Block filled = getMatchingBlock(state.getBlock(), ModBlocks.COPPER_CAULDRONS, ModBlocks.COPPER_POWDER_SNOW_CAULDRONS);
            return emptyBucket(level, pos, player, hand, stack, filled.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY_POWDER_SNOW);
        };
        COPPER_EMPTY.map().put(Items.POWDER_SNOW_BUCKET, snowFill);
        COPPER_EMPTY.map().put(ModItems.COPPER_POWDER_SNOW_BUCKET.get(), snowFill);
    }

    private static void setupCopperWaterInteractions() {
        COPPER_WATER.map().putAll(CauldronInteraction.WATER.map());

        CauldronInteraction waterEmpty = (state, level, pos, player, hand, stack) -> {
            Block empty = getMatchingBlock(state.getBlock(), ModBlocks.COPPER_WATER_CAULDRONS, ModBlocks.COPPER_CAULDRONS);
            ItemStack result = stack.is(ModItems.COPPER_BUCKET.get()) ? new ItemStack(ModItems.COPPER_WATER_BUCKET.get()) : new ItemStack(Items.WATER_BUCKET);
            return fillBucket(state, level, pos, player, hand, stack, result, s -> s.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL, empty);
        };
        COPPER_WATER.map().put(Items.BUCKET, waterEmpty);
        COPPER_WATER.map().put(ModItems.COPPER_BUCKET.get(), waterEmpty);

        // We should also override interactions that lower level to ensure they return to copper empty cauldron
        CauldronInteraction glassBottle = (state, level, pos, player, hand, stack) -> {
            if (!level.isClientSide) {
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, net.minecraft.world.item.alchemy.PotionContents.createItemStack(Items.POTION, net.minecraft.world.item.alchemy.Potions.WATER)));
                player.awardStat(Stats.ITEM_USED.get(item));
                lowerFillLevel(state, level, pos);
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        };
        COPPER_WATER.map().put(Items.GLASS_BOTTLE, glassBottle);
    }

    private static void setupCopperLavaInteractions() {
        COPPER_LAVA.map().putAll(CauldronInteraction.LAVA.map());

        CauldronInteraction lavaEmpty = (state, level, pos, player, hand, stack) -> {
            Block empty = getMatchingBlock(state.getBlock(), ModBlocks.COPPER_LAVA_CAULDRONS, ModBlocks.COPPER_CAULDRONS);
            ItemStack result = stack.is(ModItems.COPPER_BUCKET.get()) ? new ItemStack(ModItems.COPPER_LAVA_BUCKET.get()) : new ItemStack(Items.LAVA_BUCKET);
            return fillBucket(state, level, pos, player, hand, stack, result, s -> true, SoundEvents.BUCKET_FILL_LAVA, empty);
        };
        COPPER_LAVA.map().put(Items.BUCKET, lavaEmpty);
        COPPER_LAVA.map().put(ModItems.COPPER_BUCKET.get(), lavaEmpty);
    }

    private static void setupCopperPowderSnowInteractions() {
        COPPER_POWDER_SNOW.map().putAll(CauldronInteraction.POWDER_SNOW.map());

        CauldronInteraction snowEmpty = (state, level, pos, player, hand, stack) -> {
            Block empty = getMatchingBlock(state.getBlock(), ModBlocks.COPPER_POWDER_SNOW_CAULDRONS, ModBlocks.COPPER_CAULDRONS);
            ItemStack result = stack.is(ModItems.COPPER_BUCKET.get()) ? new ItemStack(ModItems.COPPER_POWDER_SNOW_BUCKET.get()) : new ItemStack(Items.POWDER_SNOW_BUCKET);
            return fillBucket(state, level, pos, player, hand, stack, result, s -> s.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_POWDER_SNOW, empty);
        };
        COPPER_POWDER_SNOW.map().put(Items.BUCKET, snowEmpty);
        COPPER_POWDER_SNOW.map().put(ModItems.COPPER_BUCKET.get(), snowEmpty);
    }

    private static ItemInteractionResult emptyBucket(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, BlockState newState, SoundEvent sound) {
        if (!level.isClientSide) {
            Item item = stack.getItem();
            ItemStack emptyStack = stack.is(ModItems.COPPER_WATER_BUCKET.get()) || stack.is(ModItems.COPPER_LAVA_BUCKET.get()) || stack.is(ModItems.COPPER_POWDER_SNOW_BUCKET.get()) || stack.is(ModItems.COPPER_MILK_BUCKET.get()) 
                    ? new ItemStack(ModItems.COPPER_BUCKET.get()) : new ItemStack(Items.BUCKET);
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, emptyStack));
            player.awardStat(Stats.ITEM_USED.get(item));
            level.setBlockAndUpdate(pos, newState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
            level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private static ItemInteractionResult fillBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, ItemStack result, java.util.function.Predicate<BlockState> canFill, SoundEvent sound, Block emptyBlock) {
        if (!canFill.test(state)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        } else {
            if (!level.isClientSide) {
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, result));
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(pos, emptyBlock.defaultBlockState());
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, emptyBlock.defaultBlockState()));
                level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    private static void lowerFillLevel(BlockState state, Level level, BlockPos pos) {
        int i = state.getValue(LayeredCauldronBlock.LEVEL) - 1;
        BlockState newState = i == 0 ? getMatchingBlock(state.getBlock(), ModBlocks.COPPER_WATER_CAULDRONS, ModBlocks.COPPER_CAULDRONS).defaultBlockState() : state.setValue(LayeredCauldronBlock.LEVEL, i);
        level.setBlockAndUpdate(pos, newState);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
    }

    public static Block getMatchingBlock(Block current, Map<String, Supplier<? extends Block>> fromMap, Map<String, Supplier<? extends Block>> toMap) {
        for (Map.Entry<String, Supplier<? extends Block>> entry : fromMap.entrySet()) {
            if (entry.getValue().get() == current) {
                String state = entry.getKey();
                return toMap.get(state).get();
            }
        }
        return toMap.get("").get(); // Fallback to unaffected
    }
}