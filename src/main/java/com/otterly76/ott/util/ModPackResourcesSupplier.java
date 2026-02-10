package com.otterly76.ott.util;


import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.BuiltInMetadata;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record ModPackResourcesSupplier(PackType packType, PackLocationInfo info, PackResourcesSupplier<AbstractModPackResources> supplier, BuiltInMetadata metadata) implements Pack.ResourcesSupplier {
    public static ModPackResourcesSupplier create(PackType packType, PackLocationInfo info, PackResourcesSupplier<AbstractModPackResources> supplier, Component description) {
        PackMetadataSection metadataSection = new PackMetadataSection(description, SharedConstants.getCurrentVersion().getPackVersion(packType), Optional.empty());
        return new ModPackResourcesSupplier(packType, info, supplier, BuiltInMetadata.of(PackMetadataSection.TYPE, metadataSection));
    }

    public @NotNull PackResources openPrimary(@NotNull PackLocationInfo info) {
        return this.getAndSetupPackResources();
    }

    public @NotNull PackResources openFull(@NotNull PackLocationInfo info, Pack.@NotNull Metadata packMetadata) {
        return this.getAndSetupPackResources();
    }

    private AbstractModPackResources getAndSetupPackResources() {
        return this.supplier.apply(this.packType, this.info, this.metadata);
    }

    @FunctionalInterface
    public interface PackResourcesSupplier<T extends PackResources> {
        T apply(PackType var1, PackLocationInfo var2, BuiltInMetadata var3);
    }
}