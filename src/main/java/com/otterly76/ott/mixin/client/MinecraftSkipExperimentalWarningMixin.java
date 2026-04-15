package com.otterly76.ott.mixin.client;

import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CreateWorldScreen.class)
public class MinecraftSkipExperimentalWarningMixin {
    @ModifyVariable(method = "tryApplyNewDataPacks", at = @At("HEAD"), argsOnly = true)
    public boolean ott$dontShowWarning(boolean showWarning) {
        return false;
    }
}