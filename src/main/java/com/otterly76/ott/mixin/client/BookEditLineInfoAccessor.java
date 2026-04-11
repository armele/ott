package com.otterly76.ott.mixin.client;

import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BookEditScreen.LineInfo.class)
public interface BookEditLineInfoAccessor {
    @Accessor("x")
    int ott$getX();

    @Accessor("y")
    int ott$getY();

    @Accessor("asComponent")
    Component ott$getAsComponent();
}
