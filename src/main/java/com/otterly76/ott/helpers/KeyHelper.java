package com.otterly76.ott.helpers;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

public class KeyHelper {
    public static boolean isCtrlKeyDown() {
        long handle = Minecraft.getInstance().getWindow().getWindow();
        boolean isCtrlKeyDown = InputConstants.isKeyDown(handle, 341) || InputConstants.isKeyDown(handle, 345);
        if (!isCtrlKeyDown && Minecraft.ON_OSX) {
            isCtrlKeyDown = InputConstants.isKeyDown(handle, 343) || InputConstants.isKeyDown(handle, 347);
        }

        return isCtrlKeyDown;
    }

    public static boolean isShiftKeyDown() {
        long handle = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(handle, 340) || InputConstants.isKeyDown(handle, 344);
    }
}