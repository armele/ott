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

public class LanternSavedData extends SavedData {

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        CompoundTag rangesTag = new CompoundTag();
        for (Map.Entry<Integer, java.util.Set<BlockPos>> entry : LanternManager.getRawData().entrySet()) {
            ListTag list = new ListTag();
            for (BlockPos pos : entry.getValue()) {
                // Wrap the position in a small object with a key so readBlockPos is happy
                CompoundTag posWrapper = new CompoundTag();
                posWrapper.put("p", NbtUtils.writeBlockPos(pos));
                list.add(posWrapper);
            }
            rangesTag.put(entry.getKey().toString(), list);
        }
        tag.put("LanternRanges", rangesTag);
        return tag;
    }

    public static LanternSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        LanternSavedData data = new LanternSavedData();
        CompoundTag rangesTag = tag.getCompound("LanternRanges");

        for (String key : rangesTag.getAllKeys()) {
            try {
                int range = Integer.parseInt(key);
                ListTag list = rangesTag.getList(key, 10); // 10 = CompoundTag

                for (int i = 0; i < list.size(); i++) {
                    CompoundTag wrapper = list.getCompound(i);
                    // Now we provide the tag and the key "p" to match the 2-argument requirement
                    NbtUtils.readBlockPos(wrapper, "p").ifPresent(pos -> LanternManager.addLantern(pos, range));
                }
            } catch (NumberFormatException e) {
                // Skip non-numeric keys
            }
        }
        return data;
    }

    public static void setDirty(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getDataStorage().computeIfAbsent(new SavedData.Factory<>(LanternSavedData::new, LanternSavedData::load), "ott_lanterns").setDirty();
        }
    }

    public static void init(ServerLevel level) {
        level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(LanternSavedData::new, LanternSavedData::load), "ott_lanterns");
    }
}