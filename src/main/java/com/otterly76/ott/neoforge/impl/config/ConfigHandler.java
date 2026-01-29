package com.otterly76.ott.neoforge.impl.config;


import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.GsonHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

public class ConfigHandler {
    private static ConfigCodec LOADED_CONFIG;

    public static ConfigCodec getConfig() {
        return LOADED_CONFIG;
    }

    public static void load(Path path) {
        if (!Files.isRegularFile(path)) {
            write(path);
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            JsonElement json = JsonParser.parseReader(reader);
            Optional<ConfigCodec> result = ConfigCodec.CODEC.parse(JsonOps.INSTANCE, json).result();
            if (result.isEmpty()) {
                throw new JsonParseException("Invalid codec");
            }

            LOADED_CONFIG = result.get();
        } catch (Exception ignored) {
        }

        write(path);
    }

    private static void write(Path path) {
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                JsonElement element = ConfigCodec.CODEC.encodeStart(JsonOps.INSTANCE, LOADED_CONFIG)
                        .result()
                        .orElseThrow(() -> new IllegalStateException("Failed to encode configuration to JSON"));

                StringWriter stringWriter = new StringWriter();
                JsonWriter jsonWriter = new JsonWriter(stringWriter);
                jsonWriter.setIndent("  ");
                GsonHelper.writeValue(jsonWriter, element, Comparator.naturalOrder());
                writer.write(commentHack(stringWriter.toString()));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String commentHack(String json) {
        return json.replaceAll("\"__.\": \"", "// ").replaceAll("\"...__\": \"", "// ").replace("\",", "");
    }

    static {
        LOADED_CONFIG = ConfigCodec.DEFAULT;
    }
}







