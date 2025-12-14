package com.otterly76.ott.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTreeDecoratorTypes {
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_TYPES = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, "minecraft");

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<PaleMossDecorator>> PALE_MOSS =
            TREE_DECORATOR_TYPES.register("pale_moss", () -> new TreeDecoratorType<>(PaleMossDecorator.CODEC));

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<CreakingHeartDecorator>> CREAKING_HEART =
            TREE_DECORATOR_TYPES.register("creaking_heart", () -> new TreeDecoratorType<>(CreakingHeartDecorator.CODEC));

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<GlowBerryDecorator>> GLOW_BERRY =
            TREE_DECORATOR_TYPES.register("glow_berry", () -> new TreeDecoratorType<>(GlowBerryDecorator.CODEC));

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<BranchingLogsDecorator>> BRANCHING_LOGS =
            TREE_DECORATOR_TYPES.register("branching_logs", () -> new TreeDecoratorType<>(BranchingLogsDecorator.CODEC));

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<TrunkTopLeavesDecorator>> TRUNK_TOP_LEAVES =
            TREE_DECORATOR_TYPES.register("trunk_top_leaves", () -> new TreeDecoratorType<>(TrunkTopLeavesDecorator.CODEC));

    public static void register(IEventBus eventBus) {
        TREE_DECORATOR_TYPES.register(eventBus);
    }
}