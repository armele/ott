package com.otterly76.ott.client.mousetweaks;

import com.mojang.blaze3d.platform.InputConstants;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

class MouseTweaksHandler {

    private static Screen openScreen = null;
    private static IGuiScreenHandler handler = null;
    private static boolean disableWheelForThisContainer = false;
    private static Slot oldSelectedSlot = null;
    private static double accumulatedScrollDelta = 0;
    private static boolean canDoLMBDrag = false;
    private static boolean canDoRMBDrag = false;
    private static boolean rmbTweakLeftOriginalSlot = false;

    private static void updateScreen(Screen newScreen) {
        if (newScreen == openScreen)
            return;

        openScreen = newScreen;
        handler = null;
        oldSelectedSlot = null;
        accumulatedScrollDelta = 0;
        canDoLMBDrag = false;
        canDoRMBDrag = false;
        rmbTweakLeftOriginalSlot = false;

        if (openScreen != null) {
            handler = findHandler(openScreen);
            if (handler != null) {
                boolean disableForThisContainer = handler.isMouseTweaksDisabled();
                disableWheelForThisContainer = handler.isWheelTweakDisabled();
                if (disableForThisContainer)
                    handler = null;
            }
        }
    }

    static void onMouseClicked(Screen screen, double x, double y, MouseButton button) {
        updateScreen(screen);
        if (handler == null)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        oldSelectedSlot = handler.getSlotUnderMouse(x, y);
        ItemStack stackOnMouse = mc.player.containerMenu.getCarried();

        if (button == MouseButton.LEFT) {
            if (stackOnMouse.isEmpty())
                canDoLMBDrag = true;
        } else if (button == MouseButton.RIGHT) {
            if (stackOnMouse.isEmpty())
                return;
            if (!OttConfig.MOUSE_TWEAKS.RMB_TWEAK.get())
                return;
            canDoRMBDrag = true;
            rmbTweakLeftOriginalSlot = false;
        }
    }

    static void onMouseReleased(Screen screen, double x, double y, MouseButton button) {
        updateScreen(screen);
        if (handler == null)
            return;

        if (button == MouseButton.LEFT)
            canDoLMBDrag = false;
        else if (button == MouseButton.RIGHT)
            canDoRMBDrag = false;
    }

    static void onMouseDrag(Screen screen, double x, double y, MouseButton button) {
        updateScreen(screen);
        if (handler == null)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        Slot selectedSlot = handler.getSlotUnderMouse(x, y);

        if (selectedSlot == oldSelectedSlot)
            return;

        ItemStack stackOnMouse = mc.player.containerMenu.getCarried();

        if (canDoRMBDrag && button == MouseButton.RIGHT && !rmbTweakLeftOriginalSlot) {
            rmbTweakLeftOriginalSlot = true;
            handler.disableRMBDraggingFunctionality();
            rmbTweakMaybeClickSlot(oldSelectedSlot, stackOnMouse);
        }

        oldSelectedSlot = selectedSlot;

        if (selectedSlot == null)
            return;
        if (handler.isIgnored(selectedSlot))
            return;

        if (button == MouseButton.LEFT) {
            if (!canDoLMBDrag)
                return;

            ItemStack selectedSlotStack = selectedSlot.getItem();
            if (selectedSlotStack.isEmpty())
                return;

            boolean shiftIsDown = InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)
                    || InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT);

            if (stackOnMouse.isEmpty()) {
                if (!OttConfig.MOUSE_TWEAKS.LMB_TWEAK_WITHOUT_ITEM.get() || !shiftIsDown)
                    return;
                handler.clickSlot(selectedSlot, MouseButton.LEFT, true);
            } else {
                if (!OttConfig.MOUSE_TWEAKS.LMB_TWEAK_WITH_ITEM.get())
                    return;
                if (!areStacksCompatible(selectedSlotStack, stackOnMouse))
                    return;

                if (shiftIsDown) {
                    handler.clickSlot(selectedSlot, MouseButton.LEFT, true);
                } else {
                    if (stackOnMouse.getCount() + selectedSlotStack.getCount() > stackOnMouse.getMaxStackSize())
                        return;
                    handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                    if (!handler.isCraftingOutput(selectedSlot))
                        handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                }
            }
        } else if (button == MouseButton.RIGHT) {
            if (!canDoRMBDrag)
                return;
            rmbTweakMaybeClickSlot(selectedSlot, stackOnMouse);
        }
    }

    static void onMouseScrolled(Screen screen, double x, double y, double scrollDelta) {
        updateScreen(screen);
        if (handler == null || disableWheelForThisContainer || !OttConfig.MOUSE_TWEAKS.WHEEL_TWEAK.get())
            return;

        Slot selectedSlot = handler.getSlotUnderMouse(x, y);
        if (selectedSlot == null || handler.isIgnored(selectedSlot))
            return;

        ItemStack selectedSlotStack = selectedSlot.getItem();
        if (selectedSlotStack.getItem() instanceof BundleItem)
            return;

        double scaledDelta = OttConfig.MOUSE_TWEAKS.SCROLL_ITEM_SCALING.get().scale(scrollDelta);
        if (accumulatedScrollDelta != 0 && Math.signum(scaledDelta) != Math.signum(accumulatedScrollDelta))
            accumulatedScrollDelta = 0;

        accumulatedScrollDelta += scaledDelta;
        int delta = (int) accumulatedScrollDelta;
        accumulatedScrollDelta -= delta;

        if (delta == 0)
            return;

        List<Slot> slots = handler.getSlots();
        int numItemsToMove = Math.abs(delta);
        boolean pushItems = delta < 0;

        if (OttConfig.MOUSE_TWEAKS.WHEEL_SCROLL_DIRECTION.get().isPositionAware() && otherInventoryIsAbove(selectedSlot, slots))
            pushItems = !pushItems;
        if (OttConfig.MOUSE_TWEAKS.WHEEL_SCROLL_DIRECTION.get().isInverted())
            pushItems = !pushItems;

        if (selectedSlotStack.isEmpty())
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        ItemStack stackOnMouse = mc.player.containerMenu.getCarried();

        if (handler.isCraftingOutput(selectedSlot)) {
            if (!areStacksCompatible(selectedSlotStack, stackOnMouse))
                return;

            if (stackOnMouse.isEmpty()) {
                if (!pushItems)
                    return;

                while (numItemsToMove-- > 0) {
                    List<Slot> targetSlots = findPushSlots(slots, selectedSlot, selectedSlotStack.getCount(), true);
                    if (targetSlots == null)
                        break;

                    handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                    for (int i = 0; i < targetSlots.size(); i++) {
                        Slot slot = targetSlots.get(i);
                        if (i == targetSlots.size() - 1) {
                            handler.clickSlot(slot, MouseButton.LEFT, false);
                        } else {
                            int clickTimes = slot.getMaxStackSize(slot.getItem()) - slot.getItem().getCount();
                            while (clickTimes-- > 0)
                                handler.clickSlot(slot, MouseButton.RIGHT, false);
                        }
                    }
                }
            } else {
                while (numItemsToMove-- > 0)
                    handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
            }

            return;
        }

        if (!stackOnMouse.isEmpty() && areStacksCompatible(selectedSlotStack, stackOnMouse))
            return;

        if (pushItems) {
            if (!stackOnMouse.isEmpty() && !selectedSlot.mayPlace(stackOnMouse))
                return;

            numItemsToMove = Math.min(numItemsToMove, selectedSlotStack.getCount());
            List<Slot> targetSlots = findPushSlots(slots, selectedSlot, numItemsToMove, false);
            assert targetSlots != null;

            if (targetSlots.isEmpty())
                return;

            handler.clickSlot(selectedSlot, MouseButton.LEFT, false);

            for (Slot slot : targetSlots) {
                int clickTimes = slot.getMaxStackSize(slot.getItem()) - slot.getItem().getCount();
                clickTimes = Math.min(clickTimes, numItemsToMove);
                numItemsToMove -= clickTimes;
                while (clickTimes-- > 0)
                    handler.clickSlot(slot, MouseButton.RIGHT, false);
            }

            handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
            return;
        }

        int maxItemsToMove = selectedSlot.getMaxStackSize(selectedSlotStack) - selectedSlotStack.getCount();
        numItemsToMove = Math.min(numItemsToMove, maxItemsToMove);

        while (numItemsToMove > 0) {
            Slot targetSlot = findPullSlot(slots, selectedSlot);
            if (targetSlot == null)
                break;

            int numItemsInTargetSlot = targetSlot.getItem().getCount();

            if (handler.isCraftingOutput(targetSlot)) {
                if (maxItemsToMove < numItemsInTargetSlot)
                    break;

                maxItemsToMove -= numItemsInTargetSlot;
                numItemsToMove = Math.min(numItemsToMove - 1, maxItemsToMove);

                if (!stackOnMouse.isEmpty() && !selectedSlot.mayPlace(stackOnMouse))
                    break;

                handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                handler.clickSlot(targetSlot, MouseButton.LEFT, false);
                handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                continue;
            }

            int numItemsToMoveFromTargetSlot = Math.min(numItemsToMove, numItemsInTargetSlot);
            maxItemsToMove -= numItemsToMoveFromTargetSlot;
            numItemsToMove -= numItemsToMoveFromTargetSlot;

            if (!stackOnMouse.isEmpty() && !targetSlot.mayPlace(stackOnMouse))
                break;

            handler.clickSlot(targetSlot, MouseButton.LEFT, false);

            if (numItemsToMoveFromTargetSlot == numItemsInTargetSlot) {
                handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
            } else {
                for (int i = 0; i < numItemsToMoveFromTargetSlot; i++)
                    handler.clickSlot(selectedSlot, MouseButton.RIGHT, false);
            }

            handler.clickSlot(targetSlot, MouseButton.LEFT, false);
        }
    }

    private static void rmbTweakMaybeClickSlot(@Nullable Slot slot, ItemStack stackOnMouse) {
        if (slot == null || stackOnMouse.isEmpty() || handler.isIgnored(slot) || handler.isCraftingOutput(slot))
            return;

        if (!(stackOnMouse.getItem() instanceof BundleItem)) {
            ItemStack selectedSlotStack = slot.getItem();
            if (!areStacksCompatible(selectedSlotStack, stackOnMouse))
                return;
            if (selectedSlotStack.getCount() == slot.getMaxStackSize(selectedSlotStack))
                return;
        }

        handler.clickSlot(slot, MouseButton.RIGHT, false);
    }

    private static boolean otherInventoryIsAbove(Slot selectedSlot, List<Slot> slots) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return false;
        boolean selectedIsInPlayerInventory = selectedSlot.container == mc.player.getInventory();

        int otherInventorySlotsAbove = 0, otherInventorySlotsBelow = 0;
        for (Slot slot : slots) {
            if ((slot.container == mc.player.getInventory()) != selectedIsInPlayerInventory) {
                if (slot.y < selectedSlot.y)
                    otherInventorySlotsAbove++;
                else
                    otherInventorySlotsBelow++;
            }
        }

        return otherInventorySlotsAbove > otherInventorySlotsBelow;
    }

    private static @Nullable IGuiScreenHandler findHandler(Screen currentScreen) {
        if (currentScreen instanceof CreativeModeInventoryScreen creative) {
            return new GuiContainerCreativeHandler(creative);
        } else if (currentScreen instanceof AbstractContainerScreen<?> container) {
            return new GuiContainerHandler(container);
        }
        return null;
    }

    private static boolean areStacksCompatible(ItemStack a, ItemStack b) {
        return a.isEmpty() || b.isEmpty() || (ItemStack.isSameItem(a, b) && ItemStack.isSameItemSameComponents(a, b));
    }

    private static @Nullable Slot findPullSlot(List<Slot> slots, Slot selectedSlot) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return null;
        WheelSearchOrder order = OttConfig.MOUSE_TWEAKS.WHEEL_SEARCH_ORDER.get();
        int startIndex = order == WheelSearchOrder.FIRST_TO_LAST ? 0 : slots.size() - 1;
        int endIndex = order == WheelSearchOrder.FIRST_TO_LAST ? slots.size() : -1;
        int direction = order == WheelSearchOrder.FIRST_TO_LAST ? 1 : -1;

        ItemStack selectedSlotStack = selectedSlot.getItem();
        boolean findInPlayerInventory = selectedSlot.container != mc.player.getInventory();

        for (int i = startIndex; i != endIndex; i += direction) {
            Slot slot = slots.get(i);
            if (handler.isIgnored(slot))
                continue;
            if ((slot.container == mc.player.getInventory()) != findInPlayerInventory)
                continue;
            ItemStack stack = slot.getItem();
            if (stack.isEmpty() || !areStacksCompatible(selectedSlotStack, stack))
                continue;
            return slot;
        }

        return null;
    }

    private static @Nullable List<Slot> findPushSlots(List<Slot> slots, Slot selectedSlot, int itemCount, boolean mustDistributeAll) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return mustDistributeAll ? null : new ArrayList<>();
        ItemStack selectedSlotStack = selectedSlot.getItem();
        boolean findInPlayerInventory = selectedSlot.container != mc.player.getInventory();

        List<Slot> rv = new ArrayList<>();
        List<Slot> goodEmptySlots = new ArrayList<>();

        for (int i = 0; i != slots.size() && itemCount > 0; i++) {
            Slot slot = slots.get(i);
            if (handler.isIgnored(slot))
                continue;
            if ((slot.container == mc.player.getInventory()) != findInPlayerInventory)
                continue;
            if (handler.isCraftingOutput(slot))
                continue;

            ItemStack stack = slot.getItem();
            if (stack.isEmpty()) {
                if (slot.mayPlace(selectedSlotStack))
                    goodEmptySlots.add(slot);
            } else if (areStacksCompatible(selectedSlotStack, stack) && stack.getCount() < slot.getMaxStackSize(stack)) {
                rv.add(slot);
                itemCount -= Math.min(itemCount, slot.getMaxStackSize(stack) - stack.getCount());
            }
        }

        for (int i = 0; i != goodEmptySlots.size() && itemCount > 0; i++) {
            Slot slot = goodEmptySlots.get(i);
            rv.add(slot);
            itemCount -= Math.min(itemCount, slot.getMaxStackSize());
        }

        if (mustDistributeAll && itemCount > 0)
            return null;

        return rv;
    }
}
