package com.otterly76.ott.worldgen;

import com.otterly76.ott.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class WorldTemplateHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_ID);

    private static final String TEMPLATE_PATH = "world_template/region";

    private static final ResourceKey<Level> TARGET_DIMENSION = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "mine_colonies_schema_dimension")
    );

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();

        Path savePath = server.getWorldPath(LevelResource.ROOT);

        Path dimPath = savePath.resolve("dimensions")
                .resolve(TARGET_DIMENSION.location().getNamespace())
                .resolve(TARGET_DIMENSION.location().getPath())
                .resolve("region");

        try {
            if (!Files.exists(dimPath) || isDirEmpty(dimPath)) {
                LOGGER.info("Ott: Initializing template world for dimension {}", TARGET_DIMENSION.location());
                copyTemplateFiles(dimPath);
            }
        } catch (IOException e) {
            LOGGER.error("Ott: Failed to copy world template", e);
        }
    }

    private static boolean isDirEmpty(Path path) throws IOException {
        if (!Files.isDirectory(path)) return true;
        try (Stream<Path> entries = Files.list(path)) {
            return entries.findFirst().isEmpty();
        }
    }

    private static void copyTemplateFiles(Path targetDir) throws IOException {
        Path sourceDir = ModList.get().getModFileById(Constants.MOD_ID).getFile().findResource(TEMPLATE_PATH);

        if (!Files.exists(sourceDir)) {
            LOGGER.warn("Ott: World template source not found at {}", sourceDir);
            return;
        }

        Files.createDirectories(targetDir);

        try (Stream<Path> stream = Files.walk(sourceDir)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".mca"))
                    .forEach(source -> {
                        try {
                            String fileName = source.getFileName().toString();
                            Path dest = targetDir.resolve(fileName);

                            Files.copy(source, dest);
                            LOGGER.debug("Copied region file: {}", fileName);
                        } catch (IOException e) {
                            LOGGER.error("Failed to copy file {}", source, e);
                        }
                    });
        }
    }
}