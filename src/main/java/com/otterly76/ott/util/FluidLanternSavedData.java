package com.otterly76.ott.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FluidLanternSavedData extends SavedData {

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        // Save water lanterns
        ListTag waterList = new ListTag();
        for (Map.Entry<BlockPos, Integer> entry : FluidLanternManager.getWaterLanterns().entrySet()) {
            CompoundTag wrapper = new CompoundTag();
            wrapper.put("p", NbtUtils.writeBlockPos(entry.getKey()));
            wrapper.putInt("r", entry.getValue());
            waterList.add(wrapper);
        }
        tag.put("WaterLanterns", waterList);

        // Save lava lanterns
        ListTag lavaList = new ListTag();
        for (Map.Entry<BlockPos, Integer> entry : FluidLanternManager.getLavaLanterns().entrySet()) {
            CompoundTag wrapper = new CompoundTag();
            wrapper.put("p", NbtUtils.writeBlockPos(entry.getKey()));
            wrapper.putInt("r", entry.getValue());
            lavaList.add(wrapper);
        }
        tag.put("LavaLanterns", lavaList);
        return tag;
    }

    public static FluidLanternSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        FluidLanternSavedData data = new FluidLanternSavedData();

        ListTag waterList = tag.getList("WaterLanterns", 10);
        for (int i = 0; i < waterList.size(); i++) {
            CompoundTag wrapper = waterList.getCompound(i);
            int radius = wrapper.contains("r") ? wrapper.getInt("r") : 32; // Default to 2 chunks if missing
            NbtUtils.readBlockPos(wrapper, "p").ifPresent(pos ->
                    FluidLanternManager.addWaterLantern(pos, radius)
            );
        }

        ListTag lavaList = tag.getList("LavaLanterns", 10);
        for (int i = 0; i < lavaList.size(); i++) {
            CompoundTag wrapper = lavaList.getCompound(i);
            int radius = wrapper.contains("r") ? wrapper.getInt("r") : 32; // Default to 2 chunks if missing
            NbtUtils.readBlockPos(wrapper, "p").ifPresent(pos ->
                    FluidLanternManager.addLavaLantern(pos, radius)
            );
        }
        return data;
    }

    public static void setDirty(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getDataStorage().computeIfAbsent(new SavedData.Factory<>(FluidLanternSavedData::new, FluidLanternSavedData::load), "ott_fluid_lanterns").setDirty();
        }
    }

    public static void init(ServerLevel level) {
        level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(FluidLanternSavedData::new, FluidLanternSavedData::load), "ott_fluid_lanterns");
    }
}