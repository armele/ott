package com.otterly76.ott.util.data;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.flag.FeatureFlagSet;
import net.neoforged.fml.ModList;

import java.util.List;
import java.util.function.Supplier;

public final class PackResourcesHelper {
    private PackResourcesHelper() {
    }

    public static Component getPackTitle(PackType packType) {
        return Component.literal("Generated " + (packType == PackType.CLIENT_RESOURCES ? "Resource" : "Data") + " Pack");
    }

    public static Component getPackDescription(String modId) {
        return ModList.get().getModContainerById(modId)
                .map(container -> (Component) Component.literal("Resources for " + container.getModInfo().getDisplayName()))
                .orElseGet(() -> Component.literal("Resources (" + modId + ")"));
    }

    public static ResourceLocation getBuiltInPack(ResourceLocation resourceLocation, PackType packType) {
        String var10001 = packType.getDirectory();
        return resourceLocation.withPrefix(var10001 + "/" + resourceLocation.getNamespace() + "/" + (packType == PackType.CLIENT_RESOURCES ? "resourcepacks" : "datapacks") + "/");
    }

    public static RepositorySource buildClientPack(ResourceLocation id, Supplier<AbstractModPackResources> factory, boolean hidden) {
        return buildClientPack(id, factory, true, Position.TOP, hidden, hidden);
    }

    public static RepositorySource buildClientPack(ResourceLocation id, Supplier<AbstractModPackResources> factory, boolean required, Pack.Position position, boolean fixedPosition, boolean hidden) {
        return (consumer) -> consumer.accept(AbstractModPackResources.buildPack(PackType.CLIENT_RESOURCES, id, factory, getPackTitle(PackType.CLIENT_RESOURCES), getPackDescription(id.getNamespace()), required, position, fixedPosition, hidden, FeatureFlagSet.of()));
    }

    public static RepositorySource buildClientPack(ResourceLocation id, Supplier<AbstractModPackResources> factory, Component title, Component description, boolean required, Pack.Position position, boolean fixedPosition, boolean hidden) {
        return (consumer) -> consumer.accept(AbstractModPackResources.buildPack(PackType.CLIENT_RESOURCES, id, factory, title, description, required, position, fixedPosition, hidden, FeatureFlagSet.of()));
    }

    public static RepositorySource buildServerPack(ResourceLocation id, Supplier<AbstractModPackResources> factory, boolean hidden) {
        return buildServerPack(id, factory, true, Position.TOP, hidden, hidden);
    }

    public static RepositorySource buildServerPack(ResourceLocation id, Supplier<AbstractModPackResources> factory, boolean required, Pack.Position position, boolean fixedPosition, boolean hidden) {
        return (consumer) -> consumer.accept(AbstractModPackResources.buildPack(PackType.SERVER_DATA, id, factory, getPackTitle(PackType.SERVER_DATA), getPackDescription(id.getNamespace()), required, position, fixedPosition, hidden, FeatureFlagSet.of()));
    }

    public static RepositorySource buildServerPack(ResourceLocation id, Supplier<AbstractModPackResources> factory, Component title, Component description, boolean required, Pack.Position position, boolean fixedPosition, boolean hidden) {
        return (consumer) -> consumer.accept(AbstractModPackResources.buildPack(PackType.SERVER_DATA, id, factory, title, description, required, position, fixedPosition, hidden, FeatureFlagSet.of()));
    }

    public static Pack.Metadata createPackInfo(PackType packType, Component descriptionComponent, FeatureFlagSet featureFlagSet) {
        return new Pack.Metadata(descriptionComponent, PackCompatibility.COMPATIBLE, featureFlagSet, List.of(), false);
    }
}
