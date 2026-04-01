package com.otterly76.ott.mixin.client;

import com.mojang.blaze3d.audio.Library;
import org.lwjgl.openal.ALC10;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.IntBuffer;

/**
 * Increases the OpenAL sound source pool from ~247 to 512.
 * Vanilla computes: staticChannels = clamp(channelCount - streamingChannels, 8, 255)
 * With OpenAL Soft typically reporting 255 hardware channels, this yields 247 static + 8 streaming.
 * We do two things:
 * 1. Request 512 mono sources from OpenAL Soft at context creation (ALC_MONO_SOURCES).
 *    After alcMakeContextCurrent, getChannelCount() re-reads the granted value from OpenAL.
 * 2. Raise the 255 hard cap to 512 so the calculation uses the full granted count.
 * Result: ~504 static + 8 streaming = 512 concurrent sound sources.
 */
@Mixin(value = Library.class, remap = false)
public class LibraryMixin {

    @Unique private static final int TARGET_POOL_SIZE = 512;

    // ALC constants (OpenAL Soft)
    @Unique private static final int ALC_OUTPUT_LIMITER_SOFT = 0x199A; // 6554 — preserve original context attr
    @Unique private static final int ALC_MONO_SOURCES        = 0x1010; // 4112
    @Unique private static final int ALC_STEREO_SOURCES      = 0x1011; // 4113

    /**
     * Redirects the OpenAL context creation call to request more audio sources.
     * The original attribute list is {ALC_OUTPUT_LIMITER_SOFT, 1, end}.
     * We rebuild it with ALC_MONO_SOURCES and ALC_STEREO_SOURCES added.
     */
    @Redirect(
        method = "init",
        at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/openal/ALC10;alcCreateContext(JLjava/nio/IntBuffer;)J"
        )
    )
    private long ott$createContextWithMoreSources(long device, IntBuffer originalAttrs) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer attrs = stack.callocInt(7)
                    .put(ALC_OUTPUT_LIMITER_SOFT).put(1)          // preserve original: enable output limiter
                    .put(ALC_MONO_SOURCES).put(TARGET_POOL_SIZE)  // request 512 mono (positional 3D) sources
                    .put(ALC_STEREO_SOURCES).put(64)              // request 64 stereo (streaming) sources
                    .put(0)                                        // end of attribute list
                    .flip();
            return ALC10.alcCreateContext(device, attrs);
        }
    }

    /**
     * Raises the hard cap on static (positional) channels from 255 to 512
     * so Minecraft actually uses the extra sources OpenAL Soft granted above.
     */
    @ModifyConstant(method = "init", constant = @Constant(intValue = 255))
    private int ott$raiseStaticChannelCap(int original) {
        return TARGET_POOL_SIZE;
    }
}
