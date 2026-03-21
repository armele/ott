package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.custom.Giraffe;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapItem.class)
public class MapItemMixin {
    @Unique
    private boolean ott$isRidingGiraffe = false;

    @Inject(method = "update", at = @At("HEAD"))
    public void ott$onUpdate(Level level, Entity viewer, MapItemSavedData data, CallbackInfo ci) {
        this.ott$isRidingGiraffe = viewer.getVehicle() instanceof Giraffe;
    }

    @ModifyVariable(method = "update", at = @At("STORE"), ordinal = 5)
    private int ott$modifyRange(int i) {
        if (this.ott$isRidingGiraffe) {
            return (int) (1.5F * i);
        }
        return i;
    }
}