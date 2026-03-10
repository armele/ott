package com.otterly76.ott.block.entity;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.shelf.ShelfBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.otterly76.ott.Constants.MOD_ID;

@SuppressWarnings("DataFlowIssue")
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> MINECRAFT_BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, "minecraft");

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CreakingHeartBlockEntity>> CREAKING_HEART =
            MINECRAFT_BLOCK_ENTITIES.register("creaking_heart", () -> BlockEntityType.Builder.of(CreakingHeartBlockEntity::new, ModBlocks.CREAKING_HEART.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AnvilBlockEntity>> ANVIL_BLOCK_ENTITY_TYPE =
            BLOCK_ENTITIES.register("anvil", () -> BlockEntityType.Builder.of(AnvilBlockEntity::new).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperChestBlockEntity>> COPPER_CHEST =
            BLOCK_ENTITIES.register("copper_chest", () -> BlockEntityType.Builder.of(CopperChestBlockEntity::new,
                    ModBlocks.COPPER_CHEST.get(), ModBlocks.EXPOSED_COPPER_CHEST.get(), ModBlocks.WEATHERED_COPPER_CHEST.get(), ModBlocks.OXIDIZED_COPPER_CHEST.get(),
                    ModBlocks.WAXED_COPPER_CHEST.get(), ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get(), ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get(), ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ShelfBlockEntity>> SHELF =
            BLOCK_ENTITIES.register("shelf", () -> BlockEntityType.Builder.of(ShelfBlockEntity::new,
                    ModBlocks.SHELVES.stream().map(net.neoforged.neoforge.registries.DeferredHolder::get).toArray(Block[]::new)).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperGolemStatueBlockEntity>> COPPER_GOLEM_STATUE =
            BLOCK_ENTITIES.register("copper_golem_statue", () -> BlockEntityType.Builder.of(CopperGolemStatueBlockEntity::new,
                    ModBlocks.COPPER_GOLEM_STATUES.values().stream().map(Supplier::get).toArray(Block[]::new)).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
        MINECRAFT_BLOCK_ENTITIES.register(eventBus);
    }

    public static void registerTileExtensions(BlockEntityTypeAddBlocksEvent event) {
        // Handle Pale Oak (backported)
        event.modify(BlockEntityType.SIGN, ModBlocks.PALE_OAK_SIGN.get(), ModBlocks.PALE_OAK_WALL_SIGN.get());
        event.modify(BlockEntityType.HANGING_SIGN, ModBlocks.PALE_OAK_HANGING_SIGN.get(), ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get());
        event.modify(BlockEntityType.SKULL, ModBlocks.DRAGON_SKULL.get(), ModBlocks.DRAGON_WALL_SKULL.get());

        // Handle Copper Hoppers
        ModBlocks.COPPER_HOPPERS.values().forEach(hopper -> event.modify(BlockEntityType.HOPPER, hopper.get()));

        // Handle other wood sets (starlight, midnight, etc.)
        ModBlocks.WOOD_SETS.values().forEach(woodSet -> {
            event.modify(BlockEntityType.SIGN, woodSet.sign().get(), woodSet.wallSign().get());
            event.modify(BlockEntityType.HANGING_SIGN, woodSet.hangingSign().get(), woodSet.wallHangingSign().get());
        });

        event.modify(ANVIL_BLOCK_ENTITY_TYPE.get(), Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL);
    }
}