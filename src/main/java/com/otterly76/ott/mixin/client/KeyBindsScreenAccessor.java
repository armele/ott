package com.otterly76.ott.mixin.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBindsScreen.class)
public interface KeyBindsScreenAccessor {

    @Accessor("keyBindsList")
    KeyBindsList ott$getKeyBindsList();

    @Accessor("keyBindsList")
    void ott$setKeyBindsList(KeyBindsList newList);

    @Accessor("resetButton")
    Button ott$getResetButton();

    @Accessor("resetButton")
    void ott$setResetButton(Button resetButton);
}
