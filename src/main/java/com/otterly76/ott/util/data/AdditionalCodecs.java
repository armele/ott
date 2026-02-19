package com.otterly76.ott.util.data;

import com.mojang.datafixers.util.Either;
import com.otterly76.ott.util.color.ColorUtils;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;

public class AdditionalCodecs {
    public static final Codec<Integer> RGB_COLOR_CODEC;
    public static final StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC;

    private static <T, U> Codec<T> withAlternative(Codec<T> primary, Codec<U> alternative, Function<U, T> converter) {
        return Codec.either(primary, alternative).xmap((either) -> either.map((t) -> t, converter), Either::left);
    }

    static {
        RGB_COLOR_CODEC = withAlternative(Codec.INT, ExtraCodecs.VECTOR3F, (vector) -> ColorUtils.colorFromFloat(1.0F, vector.x(), vector.y(), vector.z()));
        VEC3_STREAM_CODEC = new StreamCodec<ByteBuf, Vec3>() {
            public Vec3 decode(ByteBuf byteBuf) {
                return (new FriendlyByteBuf(byteBuf)).readVec3();
            }

            public void encode(ByteBuf byteBuf, Vec3 vec3) {
                (new FriendlyByteBuf(byteBuf)).writeVec3(vec3);
            }
        };
    }
}