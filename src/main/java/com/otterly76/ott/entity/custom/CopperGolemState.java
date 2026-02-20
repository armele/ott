package com.otterly76.ott.entity.custom;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import com.mojang.serialization.Codec;
import net.minecraft.util.ByIdMap;
import net.minecraft.network.codec.ByteBufCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum CopperGolemState implements StringRepresentable {
    IDLE("idle", 0),
    GETTING_ITEM("getting_item", 1),
    GETTING_NO_ITEM("getting_no_item", 2),
    DROPPING_ITEM("dropping_item", 3),
    DROPPING_NO_ITEM("dropping_no_item", 4),
    PRESSING_BUTTON("pressing_button", 5);

    public static final Codec<CopperGolemState> CODEC = StringRepresentable.fromEnum(CopperGolemState::values);
    private static final IntFunction<CopperGolemState> BY_ID = ByIdMap.sparse(CopperGolemState::id, CopperGolemState.values(), IDLE);
    public static final StreamCodec<ByteBuf, CopperGolemState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CopperGolemState::id);

    private final String name;
    private final int id;

    CopperGolemState(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public int id() {
        return this.id;
    }
}