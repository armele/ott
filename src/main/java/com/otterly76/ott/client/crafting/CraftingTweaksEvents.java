package com.otterly76.ott.client.crafting;

import com.otterly76.ott.Constants;
import com.otterly76.ott.client.ClientKeyHandler;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.crafting.CraftingTweaksHelper;
import com.otterly76.ott.mixin.client.MouseTweaksContainerScreenAccessor;
import com.otterly76.ott.network.crafting.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class CraftingTweaksEvents {

    private static boolean ignoreMouseUp = false;

    @SubscribeEvent
    public static void onKeyPressed(ScreenEvent.KeyPressed.Post event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen))
            return;

        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        AbstractContainerMenu menu = containerScreen.getMenu();
        if (CraftingTweaksHelper.getGrid(menu, player) == null)
            return;

        int keyCode = event.getKeyCode();
        int scanCode = event.getScanCode();

        if (ClientKeyHandler.CRAFTING_ROTATE.matches(keyCode, scanCode)) {
            PacketDistributor.sendToServer(new ServerboundCraftingRotatePacket(false));
            event.setCanceled(true);
        } else if (ClientKeyHandler.CRAFTING_ROTATE_CCW.matches(keyCode, scanCode)) {
            PacketDistributor.sendToServer(new ServerboundCraftingRotatePacket(true));
            event.setCanceled(true);
        } else if (ClientKeyHandler.CRAFTING_BALANCE.matches(keyCode, scanCode)) {
            PacketDistributor.sendToServer(new ServerboundCraftingBalancePacket(false));
            event.setCanceled(true);
        } else if (ClientKeyHandler.CRAFTING_SPREAD.matches(keyCode, scanCode)) {
            PacketDistributor.sendToServer(new ServerboundCraftingBalancePacket(true));
            event.setCanceled(true);
        } else if (ClientKeyHandler.CRAFTING_CLEAR.matches(keyCode, scanCode)) {
            PacketDistributor.sendToServer(new ServerboundCraftingClearPacket(false));
            event.setCanceled(true);
        } else if (ClientKeyHandler.CRAFTING_FORCE_CLEAR.matches(keyCode, scanCode)) {
            PacketDistributor.sendToServer(new ServerboundCraftingClearPacket(true));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouseClicked(ScreenEvent.MouseButtonPressed.Pre event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen))
            return;

        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        AbstractContainerMenu menu = containerScreen.getMenu();
        if (CraftingTweaksHelper.getGrid(menu, player) == null)
            return;

        MouseTweaksContainerScreenAccessor accessor = (MouseTweaksContainerScreenAccessor) containerScreen;
        Slot hoveredSlot = accessor.ott$invokeFindSlot(event.getMouseX(), event.getMouseY());

        // Transfer stack: left-click while transfer key held
        if (event.getButton() == 0 && ClientKeyHandler.CRAFTING_TRANSFER.isDown() && hoveredSlot != null && hoveredSlot.hasItem()) {
            PacketDistributor.sendToServer(new ServerboundCraftingTransferPacket(hoveredSlot.index));
            if (Screen.hasShiftDown()) {
                ItemStack target = hoveredSlot.getItem();
                for (Slot slot : menu.slots) {
                    if (slot != hoveredSlot && slot.hasItem() && ItemStack.isSameItemSameComponents(slot.getItem(), target)) {
                        PacketDistributor.sendToServer(new ServerboundCraftingTransferPacket(slot.index));
                    }
                }
            }
            ignoreMouseUp = true;
            event.setCanceled(true);
            return;
        }

        // Right-click crafting: right-click on output slot
        if (event.getButton() == 1 && OttConfig.CRAFTING_TWEAKS.RIGHT_CLICK_CRAFTS_STACK.get()
                && hoveredSlot instanceof ResultSlot) {
            PacketDistributor.sendToServer(new ServerboundCraftingCraftStackPacket(hoveredSlot.index));
            ignoreMouseUp = true;
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouseReleased(ScreenEvent.MouseButtonReleased.Pre event) {
        if (ignoreMouseUp) {
            ignoreMouseUp = false;
            event.setCanceled(true);
        }
    }
}
