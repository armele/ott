package com.otterly76.ott.util.data;

import com.mojang.serialization.Codec;
import com.otterly76.ott.util.color.ColorUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class AdditionalCodecs {
    public static final Codec<Integer> RGB_COLOR_CODEC;
    public static final StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC;

    static {
        RGB_COLOR_CODEC = Codec.withAlternative(Codec.INT, ExtraCodecs.VECTOR3F, (vector) -> ColorUtils.colorFromFloat(1.0F, vector.x(), vector.y(), vector.z()));
        VEC3_STREAM_CODEC = new StreamCodec<>() {
            public @NotNull Vec3 decode(@NotNull ByteBuf byteBuf) {
                return (new FriendlyByteBuf(byteBuf)).readVec3();
            }

            public void encode(@NotNull ByteBuf byteBuf, @NotNull Vec3 vec3) {
                (new FriendlyByteBuf(byteBuf)).writeVec3(vec3);
            }
        };
    }
}
