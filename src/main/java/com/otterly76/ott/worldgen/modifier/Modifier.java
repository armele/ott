package com.otterly76.ott.worldgen.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.otterly76.ott.mixin.common.MappedRegistryAccessor;
import com.otterly76.ott.registry.OttRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Optional;
import java.util.function.Function;

public interface Modifier {
    @SuppressWarnings("unchecked")
    Codec<Modifier> CODEC = Codec.lazyInitialized(() -> {
        // Explicitly type the registry to help the compiler resolve the byNameCodec return type
        Registry<MapCodec<? extends Modifier>> registry = (Registry<MapCodec<? extends Modifier>>) BuiltInRegistries.REGISTRY
                .getOptional(OttRegistryKeys.MODIFIER_TYPE.location())
                .orElseThrow(() -> new NullPointerException("Worldgen modifier registry does not exist yet!"));

        return registry.byNameCodec();
    }).dispatch(Modifier::codec, Function.identity());

    MapCodec<Integer> PRIORITY_DEFAULT = Codec.INT.optionalFieldOf("priority", 1000);
    MapCodec<Integer> PRIORITY_REMOVE = Codec.INT.optionalFieldOf("priority", 2000);

    default void applyModifier(RegistryAccess registryAccess) {
        this.applyModifier();
    }

    void applyModifier();

    int priority();

    default boolean internal$modifiesFabricFeatures() {
        return false;
    }

    static <T> void resetRegistrationInfo(Registry<T> registry, Holder<T> holder) {
        holder.unwrapKey().ifPresent(key -> {
            Optional<RegistrationInfo> knownPackInfo = registry.registrationInfo(key);
            // Use (Object) bridge to bypass visibility check on the registry accessor
            if (knownPackInfo.isPresent() && registry instanceof MappedRegistryAccessor) {
                @SuppressWarnings("unchecked")
                MappedRegistryAccessor<T> accessor = (MappedRegistryAccessor<T>) registry;
                try {
                    accessor.ott$getRegistrationInfos().put(key, new RegistrationInfo(Optional.empty(), knownPackInfo.get().lifecycle()));
                } catch (UnsupportedOperationException ignored) {
                }
            }
        });
    }

    MapCodec<? extends Modifier> codec();
}