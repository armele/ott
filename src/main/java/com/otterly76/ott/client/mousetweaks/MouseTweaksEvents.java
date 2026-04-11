package com.otterly76.ott.client.mousetweaks;

import com.otterly76.ott.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class MouseTweaksEvents {

    @SubscribeEvent
    public static void onMouseClickedPre(ScreenEvent.MouseButtonPressed.Pre event) {
        MouseButton button = MouseButton.fromEventButton(event.getButton());
        if (button != null)
            MouseTweaksHandler.onMouseClicked(event.getScreen(), event.getMouseX(), event.getMouseY(), button);
    }

    @SubscribeEvent
    public static void onMouseReleasedPre(ScreenEvent.MouseButtonReleased.Pre event) {
        MouseButton button = MouseButton.fromEventButton(event.getButton());
        if (button != null)
            MouseTweaksHandler.onMouseReleased(event.getScreen(), event.getMouseX(), event.getMouseY(), button);
    }

    @SubscribeEvent
    public static void onMouseScrollPost(ScreenEvent.MouseScrolled.Post event) {
        MouseTweaksHandler.onMouseScrolled(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDeltaY());
    }

    @SubscribeEvent
    public static void onMouseDragPre(ScreenEvent.MouseDragged.Pre event) {
        MouseButton button = MouseButton.fromEventButton(event.getMouseButton());
        if (button != null)
            MouseTweaksHandler.onMouseDrag(event.getScreen(), event.getMouseX(), event.getMouseY(), button);
    }
}
