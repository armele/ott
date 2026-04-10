package com.otterly76.ott.recycling;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RecyclingManager {
    private static final Map<UUID, RecyclingSession> SESSIONS = new HashMap<>();

    public static void createSession(ServerPlayer player) {
        SESSIONS.put(player.getUUID(), new RecyclingSession(player));
    }

    public static RecyclingSession getSession(UUID uuid) {
        return SESSIONS.get(uuid);
    }

    public static void removeSession(UUID uuid) {
        SESSIONS.remove(uuid);
    }

    public static boolean hasSession(UUID uuid) {
        return SESSIONS.containsKey(uuid);
    }
}
