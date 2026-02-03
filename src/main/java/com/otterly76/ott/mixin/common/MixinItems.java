package com.otterly76.ott.mixin.common;

import com.otterly76.ott.Ott;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Items.class})
public class MixinItems {
    @Inject(
            method = {"<clinit>"},
            at = {@At("RETURN")}
    )
    private static void classInit(CallbackInfo cbi) {
        Ott.fixMC151457();
    }
}