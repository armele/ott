package com.otterly76.ott.afk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Server-side AFK state for 1.21.1, backed by NeoForge Attachments.
 */
public final class PlayerAFKState {
    public static final Codec<PlayerAFKState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("afk").forGetter(PlayerAFKState::isAfk),
            AFKSource.CODEC.fieldOf("source").forGetter(PlayerAFKState::getSource)
    ).apply(instance, PlayerAFKState::new));

    private boolean afk;
    private AFKSource source;

    // Non-persistent: track idle time since last server tick check
    private long lastActionTime;

    public PlayerAFKState() {
        this(false, AFKSource.LOGIN_APPLIED);
    }

    public PlayerAFKState(boolean afk, AFKSource source) {
        this.afk = afk;
        this.source = source;
        this.lastActionTime = System.currentTimeMillis();
    }

    public boolean isAfk() {
        return afk;
    }

    public void setAfk(boolean afk, AFKSource source) {
        this.afk = afk;
        this.source = source;
    }

    public AFKSource getSource() {
        return source;
    }

    public long getLastActionTime() {
        return lastActionTime;
    }

    public void updateLastActionTime() {
        this.lastActionTime = System.currentTimeMillis();
    }
}
