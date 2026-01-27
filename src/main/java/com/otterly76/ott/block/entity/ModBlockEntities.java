package com.otterly76.ott.block.entity;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.otterly76.ott.Constants.MOD_ID;

@SuppressWarnings("DataFlowIssue")
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SignBlockEntity>> PALE_OAK_SIGN =
            BLOCK_ENTITIES.register("pale_oak_sign", () -> BlockEntityType.Builder.of(SignBlockEntity::new, ModBlocks.PALE_OAK_SIGN.get(), ModBlocks.PALE_OAK_WALL_SIGN.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CreakingHeartBlockEntity>> CREAKING_HEART =
            BLOCK_ENTITIES.register("creaking_heart", () -> BlockEntityType.Builder.of(CreakingHeartBlockEntity::new, ModBlocks.CREAKING_HEART.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SignBlockEntity>> PALE_OAK_WALL_HANGING_SIGN =
            BLOCK_ENTITIES.register("pale_oak_hanging_sign", () -> BlockEntityType.Builder.of(SignBlockEntity::new, ModBlocks.PALE_OAK_HANGING_SIGN.get(), ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VisualCraftingBlockEntity>> VISUAL_CRAFTING_TABLE =
            BLOCK_ENTITIES.register("visual_crafting_table", () -> BlockEntityType.Builder.of(VisualCraftingBlockEntity::new, Blocks.CRAFTING_TABLE).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VisualAnvilBlockEntity>> VISUAL_ANVIL =
            BLOCK_ENTITIES.register("visual_anvil", () -> BlockEntityType.Builder.of(VisualAnvilBlockEntity::new, Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    public static void registerTileExtensions(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityType.SIGN, ModBlocks.PALE_OAK_SIGN.get(), ModBlocks.PALE_OAK_WALL_SIGN.get());
        event.modify(BlockEntityType.HANGING_SIGN, ModBlocks.PALE_OAK_HANGING_SIGN.get(), ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get());
    }
}