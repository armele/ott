package com.otterly76.ott.block.entity;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    /**
     * Block entity types should live in your mod namespace.
     * (Your Pale Oak *blocks/items* can still be in the "minecraft" namespace.)
     */
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    @SuppressWarnings("ConstantConditions")
    public static final Supplier<BlockEntityType<CreakingHeartBlockEntity>> CREAKING_HEART =
            BLOCK_ENTITIES.register("creaking_heart",
                    () -> BlockEntityType.Builder.of(CreakingHeartBlockEntity::new, ModBlocks.CREAKING_HEART.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    /**
     * This is the critical piece for the crash:
     * make vanilla SIGN/HANGING_SIGN block entity types accept our sign blocks (minecraft: pale oak + ott wood sets).
     */
    public static void registerTileExtensions(BlockEntityTypeAddBlocksEvent event) {
        // Pale Oak backport signs (minecraft namespace blocks)
        event.modify(BlockEntityType.SIGN, ModBlocks.PALE_OAK_SIGN.get(), ModBlocks.PALE_OAK_WALL_SIGN.get());
        event.modify(BlockEntityType.HANGING_SIGN, ModBlocks.PALE_OAK_HANGING_SIGN.get(), ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get());

        // Datagenerated ott wood set signs (ott namespace blocks)
        ModBlocks.WOOD_SETS.forEach((setName, set) -> {
            event.modify(BlockEntityType.SIGN, set.sign().get(), set.wallSign().get());
            event.modify(BlockEntityType.HANGING_SIGN, set.hangingSign().get(), set.wallHangingSign().get());
        });
    }
}