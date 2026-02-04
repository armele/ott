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

public class DamageLanternSavedData extends SavedData {
    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        ListTag list = new ListTag();
        for (Map.Entry<BlockPos, Integer> e : DamageLanternManager.getAll().entrySet()) {
            CompoundTag wrapper = new CompoundTag();
            wrapper.put("p", NbtUtils.writeBlockPos(e.getKey()));
            wrapper.putInt("r", e.getValue());
            list.add(wrapper);
        }
        tag.put("DamageLanterns", list);
        return tag;
    }

    public static DamageLanternSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        DamageLanternSavedData data = new DamageLanternSavedData();
        if (tag.contains("DamageLanterns")) {
            ListTag list = tag.getList("DamageLanterns", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag wrapper = list.getCompound(i);
                int range = wrapper.getInt("r");
                NbtUtils.readBlockPos(wrapper, "p").ifPresent(pos -> DamageLanternManager.add(pos, range));
            }
        }
        return data;
    }

    public static void setDirty(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getDataStorage()
                    .computeIfAbsent(new SavedData.Factory<>(DamageLanternSavedData::new, DamageLanternSavedData::load), "ott_damage_lanterns")
                    .setDirty();
        }
    }

    public static void init(ServerLevel level) {
        level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(DamageLanternSavedData::new, DamageLanternSavedData::load), "ott_damage_lanterns");
    }
}