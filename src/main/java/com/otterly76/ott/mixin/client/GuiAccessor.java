package com.otterly76.ott.mixin.client;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface GuiAccessor {
    @Accessor("leftHeight")
    int getLeftHeight();

    @Accessor("leftHeight")
    void setLeftHeight(int value);

    @Accessor("rightHeight")
    int getRightHeight();

    @Accessor("rightHeight")
    void setRightHeight(int value);
}