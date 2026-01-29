package com.otterly76.ott.entity;


import com.otterly76.ott.neoforge.impl.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class OttWoodSetBoatEntity extends Boat {
    private static final EntityDataAccessor<String> SET_NAME =
            SynchedEntityData.defineId(OttWoodSetBoatEntity.class, EntityDataSerializers.STRING);

    public OttWoodSetBoatEntity(EntityType<? extends Boat> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SET_NAME, "");
    }

    public void setWoodSetName(@NotNull String setName) {
        this.entityData.set(SET_NAME, setName);
    }

    public @NotNull String getWoodSetName() {
        return this.entityData.get(SET_NAME);
    }

    @Override
    public @NotNull Item getDropItem() {
        var setName = getWoodSetName();
        var item = ModItems.WOOD_SET_BOATS.get(setName);
        return item != null ? item.get() : ModItems.OTTER.get(); // fallback: should never happen
    }

    @Override
    public @NotNull ItemStack getPickResult() {
        return new ItemStack(this.getDropItem());
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("OttWoodSet", getWoodSetName());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("OttWoodSet")) {
            setWoodSetName(tag.getString("OttWoodSet"));
        }
    }
}



