package com.otterly76.ott.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeSavedData extends SavedData {
    private static final String DATA_NAME = "ott_homes";
    private final Map<UUID, Map<String, HomePos>> playerHomes = new HashMap<>();

    public record HomePos(BlockPos pos, ResourceKey<Level> dimension) {
        public CompoundTag toNbt() {
            CompoundTag tag = new CompoundTag();
            tag.putLong("pos", pos.asLong());
            tag.putString("dim", dimension.location().toString());
            return tag;
        }

        public static HomePos fromNbt(CompoundTag tag) {
            BlockPos pos = BlockPos.of(tag.getLong("pos"));
            ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString("dim")));
            return new HomePos(pos, dim);
        }
    }

    public void setHome(UUID playerUuid, String homeName, BlockPos pos, ResourceKey<Level> dimension) {
        playerHomes.computeIfAbsent(playerUuid, k -> new HashMap<>()).put(homeName.toLowerCase(), new HomePos(pos, dimension));
        setDirty();
    }

    public @Nullable HomePos getHome(UUID playerUuid, String homeName) {
        Map<String, HomePos> homes = playerHomes.get(playerUuid);
        return homes != null ? homes.get(homeName.toLowerCase()) : null;
    }

    public void deleteHome(UUID playerUuid, String homeName) {
        Map<String, HomePos> homes = playerHomes.get(playerUuid);
        if (homes != null) {
            if (homes.remove(homeName.toLowerCase()) != null) {
                setDirty();
            }
        }
    }

    public Map<String, HomePos> getHomes(UUID playerUuid) {
        return playerHomes.getOrDefault(playerUuid, Map.of());
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        CompoundTag playersTag = new CompoundTag();
        playerHomes.forEach((uuid, homes) -> {
            CompoundTag homesTag = new CompoundTag();
            homes.forEach((name, homePos) -> homesTag.put(name, homePos.toNbt()));
            playersTag.put(uuid.toString(), homesTag);
        });
        tag.put("players", playersTag);
        return tag;
    }

    public static HomeSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        HomeSavedData data = new HomeSavedData();
        CompoundTag playersTag = tag.getCompound("players");
        for (String uuidStr : playersTag.getAllKeys()) {
            UUID uuid = UUID.fromString(uuidStr);
            CompoundTag homesTag = playersTag.getCompound(uuidStr);
            Map<String, HomePos> homes = new HashMap<>();
            for (String homeName : homesTag.getAllKeys()) {
                homes.put(homeName, HomePos.fromNbt(homesTag.getCompound(homeName)));
            }
            data.playerHomes.put(uuid, homes);
        }
        return data;
    }

    public static HomeSavedData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(new SavedData.Factory<>(HomeSavedData::new, HomeSavedData::load), DATA_NAME);
    }
}
