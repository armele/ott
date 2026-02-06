package com.otterly76.ott;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.function.Supplier;

public interface PlatformHandler {
    @SuppressWarnings("SameReturnValue")
    Platform getPlatform();

    Path configPath();

    boolean hasPermission(@NotNull CommandSourceStack sourceStack, @NotNull String permission);

    Supplier<SimpleParticleType> registerCreateParticle(String name);

    <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> entry);

    enum Platform {
        NEOFORGE
    }
}
