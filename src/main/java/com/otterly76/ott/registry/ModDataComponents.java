package com.otterly76.ott.registry;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.variant.ChickenVariant;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<DataComponentType<?>> MINECRAFT_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, "minecraft");

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceKey<ChickenVariant>>> CHICKEN_VARIANT = registerMinecraft("chicken/variant", (builder) -> builder.persistent(ResourceKey.codec(OttRegistryKeys.CHICKEN_VARIANT)).networkSynchronized(ResourceKey.streamCodec(OttRegistryKeys.CHICKEN_VARIANT)));

    public static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return COMPONENTS.register(name, () -> operator.apply(DataComponentType.builder()).build());
    }

    public static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> registerMinecraft(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return MINECRAFT_COMPONENTS.register(name, () -> operator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        COMPONENTS.register(eventBus);
        MINECRAFT_COMPONENTS.register(eventBus);
    }
}