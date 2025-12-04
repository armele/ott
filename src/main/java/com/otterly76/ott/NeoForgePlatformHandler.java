package com.otterly76.ott;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.function.Supplier;

public class NeoForgePlatformHandler implements PlatformHandler {
    public static final NeoForgePlatformHandler PLATFORM_HANDLER = new NeoForgePlatformHandler();
    private static final int DEFAULT_PERMISSION_LEVEL = 4;
    private NeoForgePlatformHandler() {
    }
    @Override
    public PlatformHandler.Platform getPlatform() {
        return PlatformHandler.Platform.NEOFORGE;
    }
    @Override
    public Path configPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean hasPermission(@NotNull CommandSourceStack sourceStack, @NotNull String permission) {
        return sourceStack.hasPermission(DEFAULT_PERMISSION_LEVEL);
    }

    @Override
    public Supplier<SimpleParticleType> registerCreateParticle(String name) {
        return () -> new SimpleParticleType(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> entry) {
        if (BuiltInRegistries.BLOCK.equals(registry)) {
            // Cast to raw Supplier to bypass generic bounds check for the specific register call
            return (Supplier<T>) ModBlocks.BLOCKS.register(name, (Supplier) entry);
        } else if (BuiltInRegistries.ITEM.equals(registry)) {
            return (Supplier<T>) ModItems.ITEMS.register(name, (Supplier) entry);
        } else if (BuiltInRegistries.PARTICLE_TYPE.equals(registry)) {
            // Example: Add support for particles if you have a ModParticles DeferredRegister
            // return (Supplier<T>) ModParticles.PARTICLES.register(name, (Supplier) entry);
        }

        throw new UnsupportedOperationException("NeoForgePlatformHandler#register is not implemented for registry: " + registry.key());
    }
}