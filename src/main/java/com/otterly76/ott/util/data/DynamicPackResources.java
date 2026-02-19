package com.otterly76.ott.util.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.otterly76.ott.generation.DataProviderContext;
import com.otterly76.ott.generation.RegistriesDataProvider;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DynamicPackResources extends AbstractModPackResources {
    public static final Map<String, PackType> PATHS_FOR_TYPE = Arrays.stream(PackType.values()).collect(ImmutableMap.toImmutableMap(PackType::getDirectory, Function.identity()));
    protected final DataProviderContext.Factory[] factories;
    private Map<PackType, Map<ResourceLocation, IoSupplier<InputStream>>> paths;

    protected DynamicPackResources(DataProviderContext.Factory... factories) {
        this.factories = factories;
    }

    public static Supplier<AbstractModPackResources> create(DataProviderContext.Factory... factories) {
        return () -> new DynamicPackResources(factories);
    }

    public static Map<PackType, Map<ResourceLocation, IoSupplier<InputStream>>> generatePathsFromProviders(String modId, DataProviderContext.Factory... factories) {
        try {
            Map<PackType, Map<ResourceLocation, IoSupplier<InputStream>>> paths = Arrays.stream(PackType.values()).collect(Collectors.toMap(Function.identity(), ($) -> new java.util.concurrent.ConcurrentHashMap<>()));
            DataProviderContext context = DataProviderContext.fromModId(modId);

            for(DataProviderContext.Factory factory : factories) {
                DataProvider dataProvider = factory.apply(context);
                if (dataProvider instanceof RegistriesDataProvider registriesDataProvider) {
                    context = context.withRegistries(registriesDataProvider.getRegistries());
                }

                dataProvider.run((filePath, data, hashCode) -> {
                    List<String> strings = FileUtil.decomposePath(filePath.normalize().toString().replace(File.separator, "/")).result().filter((list) -> list.size() >= 2).orElse(null);
                    if (strings != null) {
                        PackType packType = PATHS_FOR_TYPE.get(strings.getFirst());
                        Objects.requireNonNull(packType, () -> "pack type for directory %s is null".formatted(strings.getFirst()));
                        String path = strings.stream().skip(2L).collect(Collectors.joining("/"));
                        ResourceLocation resourceLocation = ResourceLocation.tryBuild(strings.get(1), path);
                        if (resourceLocation != null) {
                            paths.get(packType).put(resourceLocation, () -> new ByteArrayInputStream(data));
                        }
                    }

                }).join();
            }

            Iterator<Map.Entry<PackType, Map<ResourceLocation, IoSupplier<InputStream>>>> iterator = paths.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry<PackType, Map<ResourceLocation, IoSupplier<InputStream>>> entry = iterator.next();
                if (!entry.getValue().isEmpty()) {
                    entry.setValue(ImmutableMap.copyOf(entry.getValue()));
                } else {
                    iterator.remove();
                }
            }

            return Maps.immutableEnumMap(paths);
        } catch (Throwable throwable) {
            return Collections.emptyMap();
        }
    }

    protected Map<ResourceLocation, IoSupplier<InputStream>> getPathsForType(PackType packType) {
        Map<PackType, Map<ResourceLocation, IoSupplier<InputStream>>> paths = this.paths;
        if (paths == null) {
            paths = this.paths = this.generatePathsFromProviders();
        }

        return paths.getOrDefault(packType, Collections.emptyMap());
    }

    protected Map<PackType, Map<ResourceLocation, IoSupplier<InputStream>>> generatePathsFromProviders() {
        return generatePathsFromProviders(this.getNamespace(), this.factories);
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(@NotNull PackType packType, @NotNull ResourceLocation location) {
        return this.getPathsForType(packType).get(location);
    }

    @Override
    public void listResources(@NotNull PackType packType, @NotNull String namespace, @NotNull String path, @NotNull PackResources.ResourceOutput resourceOutput) {
        this.getPathsForType(packType).entrySet().stream().filter((entry) -> entry.getKey().getNamespace().equals(namespace) && entry.getKey().getPath().startsWith(path)).forEach((entry) -> resourceOutput.accept(entry.getKey(), entry.getValue()));
    }

    @Override
    public @NotNull Set<String> getNamespaces(@NotNull PackType packType) {
        return this.getPathsForType(packType).keySet().stream().map(ResourceLocation::getNamespace).collect(Collectors.toSet());
    }
}
