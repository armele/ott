package com.otterly76.ott.worldgen;

import com.otterly76.ott.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTreeDecoratorTypes {
    // Register for things you want to look like vanilla
    public static final DeferredRegister<TreeDecoratorType<?>> MINECRAFT_DECORATORS =
            DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, "minecraft");

    // Register for your custom OTT content
    public static final DeferredRegister<TreeDecoratorType<?>> OTT_DECORATORS =
            DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, Constants.MOD_ID);

    // --- MINECRAFT NAMESPACE ---
    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<PaleMossDecorator>> PALE_MOSS =
            MINECRAFT_DECORATORS.register("pale_moss", () -> new TreeDecoratorType<>(PaleMossDecorator.CODEC));

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<CreakingHeartDecorator>> CREAKING_HEART =
            MINECRAFT_DECORATORS.register("creaking_heart", () -> new TreeDecoratorType<>(CreakingHeartDecorator.CODEC));

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<AttachedToLogsDecorator>> ATTACHED_TO_LOGS =
            MINECRAFT_DECORATORS.register("attached_to_logs", () -> new TreeDecoratorType<>(AttachedToLogsDecorator.CODEC));

    // --- OTT NAMESPACE ---
    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<VerdantForestFruitDecorator>> VERDANT_FRUIT =
            OTT_DECORATORS.register("verdant_forest_tree_fruit_decorator", () -> new TreeDecoratorType<>(VerdantForestFruitDecorator.CODEC));

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<VerdantForestLeaveDecorator>> VERDANT_LEAVES =
            OTT_DECORATORS.register("verdant_forest_tree_leave_decorator", () -> new TreeDecoratorType<>(VerdantForestLeaveDecorator.CODEC));

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<VerdantForestTrunkDecorator>> VERDANT_TRUNK =
            OTT_DECORATORS.register("verdant_forest_tree_trunk_decorator", () -> new TreeDecoratorType<>(VerdantForestTrunkDecorator.CODEC));

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<PlaceOnGroundDecorator>> PLACE_ON_GROUND =
            MINECRAFT_DECORATORS.register("place_on_ground", () -> new TreeDecoratorType<>(PlaceOnGroundDecorator.CODEC));

    public static void register(IEventBus eventBus) {
        MINECRAFT_DECORATORS.register(eventBus);
        OTT_DECORATORS.register(eventBus);
    }
}
