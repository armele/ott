package com.otterly76.ott.mixin.common;

import com.otterly76.ott.util.entity.LeashDataExtension;
import net.minecraft.world.entity.Leashable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({Leashable.LeashData.class})
public class LeashDataMixin implements LeashDataExtension {
    @Unique
    private double ott$angularMomentum;

    @Override
    public double ott$angularMomentum() {
        return this.ott$angularMomentum;
    }

    @Override
    public void ott$setAngularMomentum(double angularMomentum) {
        this.ott$angularMomentum = angularMomentum;
    }
}
