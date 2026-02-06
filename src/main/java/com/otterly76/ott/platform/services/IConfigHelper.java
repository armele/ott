package com.otterly76.ott.platform.services;

import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;

public interface IConfigHelper {
    Path getBackwardsCompatiblePath();

    Path getGlobalConfigPath();

    Path getServerConfigPath(MinecraftServer var1);

    boolean isDedicatedServer();
}
