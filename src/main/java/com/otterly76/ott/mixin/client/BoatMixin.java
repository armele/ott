package com.otterly76.ott.mixin.client;

import com.otterly76.ott.util.entity.InterpolationHandler;
import com.otterly76.ott.util.entity.LeashExtension;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Boat.class})
public abstract class BoatMixin extends Entity implements LeashExtension {
    @Unique
    private final InterpolationHandler ott$interpolation = new InterpolationHandler(this, 3);

    public BoatMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = {"lerpTo(DDDFFI)V"},
        at = {@At("HEAD")},
        cancellable = true
    )
    private void vb$lerpTo(double x, double y, double z, float yRot, float xRot, int steps, CallbackInfo ci) {
        this.ott$interpolation.interpolateTo(new Vec3(x, y, z), yRot, xRot);
        ci.cancel();
    }

    @Inject(
        method = {"tickLerp()V"},
        at = {@At("HEAD")},
        cancellable = true
    )
    private void vb$tickLerp(CallbackInfo ci) {
        if (this.isControlledByLocalInstance()) {
            this.ott$interpolation.cancel();
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }

        this.ott$interpolation.interpolate();
        ci.cancel();
    }
}
