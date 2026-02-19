package com.otterly76.ott.mixin.access;

import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Boat.class)
public interface BoatAccessor {
    @Accessor("lastYd")
    double getLastYd();

    @Accessor("lastYd")
    void setLastYd(double lastYd);

    @Accessor("status")
    Boat.Status getStatus();
}
