package com.otterly76.ott.afk;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/**
 * Source of an AFK state change. Mirrors the original 1.20.1 enum semantics.
 */
public enum AFKSource implements StringRepresentable {
    SELF_APPLY("self_apply"),
    OPERATOR_APPLIED("operator_applied"),
    LOGIN_APPLIED("login_applied"),
    IDLED_TOO_LONG("idled_too_long");

    public static final Codec<AFKSource> CODEC = StringRepresentable.fromEnum(AFKSource::values);
    private final String name;

    AFKSource(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}