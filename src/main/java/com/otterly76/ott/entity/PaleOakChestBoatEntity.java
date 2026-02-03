package com.otterly76.ott.entity;

import com.otterly76.ott.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PaleOakChestBoatEntity extends ChestBoat {
    private static final EntityDataAccessor<Boolean> PALE_OAK;

    public PaleOakChestBoatEntity(EntityType<? extends Boat> type, Level level) {
        super(type, level);
    }

    public PaleOakChestBoatEntity(Level level, double x, double y, double z) {
        this(ModEntities.PALE_OAK_CHEST_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PALE_OAK, true);
    }

    public @NotNull Item getDropItem() {
        return ModItems.PALE_OAK_CHEST_BOAT.get();
    }

    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("PaleOak", true);
    }

    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    public @NotNull ItemStack getPickResult() {
        return new ItemStack(this.getDropItem());
    }

    static {
        PALE_OAK = SynchedEntityData.defineId(PaleOakChestBoatEntity.class, EntityDataSerializers.BOOLEAN);
    }
}