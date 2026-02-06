package com.otterly76.ott.platform.neoforge;

import com.otterly76.ott.platform.services.IConfigHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.nio.file.Path;

public class NeoForgeConfigHelper implements IConfigHelper {
    @Override
    public Path getBackwardsCompatiblePath() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Path getGlobalConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Path getServerConfigPath(MinecraftServer server) {
        return server.getWorldPath(LevelResource.ROOT).resolve("serverconfig");
    }

    @Override
    public boolean isDedicatedServer() {
        return ServerLifecycleHooks.getCurrentServer() != null && ServerLifecycleHooks.getCurrentServer().isDedicatedServer();
    }
}
