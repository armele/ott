package com.otterly76.ott.afk;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AFKClientStates {
    private static final Set<UUID> AFK_PLAYERS = new HashSet<>();

    public static void setAFK(UUID uuid, boolean afk) {
        if (afk) AFK_PLAYERS.add(uuid);
        else AFK_PLAYERS.remove(uuid);
    }

    public static boolean isAFK(UUID uuid) {
        return AFK_PLAYERS.contains(uuid);
    }
}