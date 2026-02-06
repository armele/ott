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
        ListTag list = new ListTag();
        for (Map.Entry<BlockPos, Integer> entry : LanternManager.getRawData().entrySet()) {
            CompoundTag wrapper = new CompoundTag();
            wrapper.put("p", NbtUtils.writeBlockPos(entry.getKey()));
            wrapper.putInt("r", entry.getValue());
            list.add(wrapper);
        }
        tag.put("ProtectiveLanterns", list);
        return tag;
    }

    public static LanternSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        LanternSavedData data = new LanternSavedData();
        if (tag.contains("ProtectiveLanterns")) {
            ListTag list = tag.getList("ProtectiveLanterns", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag wrapper = list.getCompound(i);
                int range = wrapper.getInt("r");
                NbtUtils.readBlockPos(wrapper, "p").ifPresent(pos -> LanternManager.addLantern(pos, range));
            }
        } else if (tag.contains("LanternRanges")) {
            // Legacy loading for old format
            CompoundTag rangesTag = tag.getCompound("LanternRanges");
            for (String key : rangesTag.getAllKeys()) {
                try {
                    int range = Integer.parseInt(key);
                    ListTag list = rangesTag.getList(key, 10);
                    for (int j = 0; j < list.size(); j++) {
                        CompoundTag wrapper = list.getCompound(j);
                        NbtUtils.readBlockPos(wrapper, "p").ifPresent(pos -> LanternManager.addLantern(pos, range));
                    }
                } catch (NumberFormatException ignored) {}
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
