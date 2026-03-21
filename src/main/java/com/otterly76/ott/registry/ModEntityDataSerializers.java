package com.otterly76.ott.registry;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.HedgehogEntity;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Constants.MOD_ID);

    public static final Supplier<EntityDataSerializer<HedgehogEntity.HedgehogState>> HEDGEHOG_STATE = ENTITY_DATA_SERIALIZERS.register("hedgehog_state", () -> EntityDataSerializer.forValueType(HedgehogEntity.HedgehogState.STREAM_CODEC));

    public static void register(IEventBus modEventBus) {
        ENTITY_DATA_SERIALIZERS.register(modEventBus);
    }
}
