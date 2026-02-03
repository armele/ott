package com.otterly76.ott.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ConfigCodec(String commentA, String commentB, String commentC, String commentD, boolean breaksSeedParity, boolean logDebugMessages) {
    private static final String COMMENT_A = "If disabled, some mod compatibility features will be turned off to prioritize parity with vanilla seeds.";
    private static final String COMMENT_B = "The following features will break if disabled:";
    private static final String COMMENT_C = "- Custom wood type shipwrecks";
    private static final String COMMENT_D = "- Structure optimizations";
    public static final Codec<ConfigCodec> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.STRING.fieldOf("__A").orElse("If disabled, some mod compatibility features will be turned off to prioritize parity with vanilla seeds.").forGetter(ConfigCodec::commentA), Codec.STRING.fieldOf("__B").orElse("The following features will break if disabled:").forGetter(ConfigCodec::commentB), Codec.STRING.fieldOf("__C").orElse("- Custom wood type shipwrecks").forGetter(ConfigCodec::commentC), Codec.STRING.fieldOf("__D").orElse("- Structure optimizations").forGetter(ConfigCodec::commentD), Codec.BOOL.fieldOf("breaks_seed_parity").orElse(true).forGetter(ConfigCodec::breaksSeedParity), Codec.BOOL.fieldOf("log_debug_messages").orElse(false).forGetter(ConfigCodec::logDebugMessages)).apply(instance, ConfigCodec::new));
    public static final ConfigCodec DEFAULT = new ConfigCodec("If disabled, some mod compatibility features will be turned off to prioritize parity with vanilla seeds.", "The following features will break if disabled:", "- Custom wood type shipwrecks", "- Structure optimizations", true, false);
}