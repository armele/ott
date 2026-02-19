package com.otterly76.ott.entity.vehicle;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PaleOakChestBoat extends ChestBoat implements PaleOakBoatBehavior {
    public PaleOakChestBoat(EntityType<? extends Boat> type, Level level) {
        super(type, level);
    }

    public PaleOakChestBoat(Level level, double x, double y, double z) {
        this(ModEntities.PALE_OAK_CHEST_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    public @NotNull Item getDropItem() {
        return ModItems.PALE_OAK_CHEST_BOAT.get();
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
        this.fall(this, y, onGround);
    }
}
