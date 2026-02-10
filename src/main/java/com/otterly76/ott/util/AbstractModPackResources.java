package com.otterly76.ott.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

public abstract class AbstractModPackResources implements PackResources {
    protected final String modLogoPath;
    private @Nullable PackType packType;
    private @Nullable PackLocationInfo info;
    private @Nullable BuiltInMetadata metadata;

    public AbstractModPackResources() {
        this("mod_logo.png");
    }

    public AbstractModPackResources(String modLogoPath) {
        Objects.requireNonNull(modLogoPath, "mod logo path is null");
        this.modLogoPath = modLogoPath;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(@NotNull String @NotNull ... elements) {
        String path = String.join("/", elements);
        if ("pack.png".equals(path)) {
            return ModList.get().getModContainerById(this.getNamespace())
                    .map(container -> container.getModInfo().getOwningFile().getFile().findResource(this.modLogoPath))
                    .filter(java.nio.file.Files::exists)
                    .map(resourcePath -> (IoSupplier<InputStream>) () -> java.nio.file.Files.newInputStream(resourcePath))
                    .orElse(null);
        }
        return null;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(@NotNull PackType packType, @NotNull ResourceLocation location) {
        return null;
    }

    @Override
    public void listResources(@NotNull PackType packType, @NotNull String namespace, @NotNull String path, @NotNull PackResources.ResourceOutput resourceOutput) {
    }

    @Override
    public @NotNull Set<String> getNamespaces(@NotNull PackType type) {
        Objects.requireNonNull(this.packType, "pack type is null");
        return this.packType == type ? Collections.singleton(this.getNamespace()) : Collections.emptySet();
    }

    @Override
    public <T> @Nullable T getMetadataSection(@NotNull MetadataSectionSerializer<T> deserializer) {
        Objects.requireNonNull(this.metadata, "metadata is null");
        return this.metadata.get(deserializer);
    }

    @Override
    public @NotNull PackLocationInfo location() {
        Objects.requireNonNull(this.info, "info is null");
        return this.info;
    }

    @Override
    public void close() {
    }

    public String getNamespace() {
        return ResourceLocation.parse(this.packId()).getNamespace();
    }

    @OverrideOnly
    protected void setup() {
    }

    public static Pack buildPack(PackType packType, ResourceLocation id, Supplier<AbstractModPackResources> factory, Component title, Component description, boolean required, Pack.Position position, boolean fixedPosition, boolean hidden, FeatureFlagSet features) {
        PackLocationInfo info = new PackLocationInfo(id.toString(), title, PackSource.BUILT_IN, Optional.empty());
        Pack.ResourcesSupplier resourcesSupplier = ModPackResourcesSupplier.create(packType, info, createSupplier(factory), description);
        Pack.Metadata metadata = new Pack.Metadata(description, PackCompatibility.COMPATIBLE, features, List.of(), hidden);
        PackSelectionConfig config = new PackSelectionConfig(required, position, fixedPosition);
        return new Pack(info, resourcesSupplier, metadata, config);
    }

    private static ModPackResourcesSupplier.PackResourcesSupplier<AbstractModPackResources> createSupplier(Supplier<AbstractModPackResources> factory) {
        return (packType, info, metadata) -> {
            AbstractModPackResources packResources = factory.get();
            packResources.info = info;
            packResources.metadata = metadata;
            packResources.packType = packType;
            packResources.setup();
            return packResources;
        };
    }
}