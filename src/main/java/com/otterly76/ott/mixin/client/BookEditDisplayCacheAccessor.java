package com.otterly76.ott.mixin.client;

import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BookEditScreen.DisplayCache.class)
public interface BookEditDisplayCacheAccessor {
    @Accessor("lines")
    BookEditScreen.LineInfo[] ott$getLines();
}
