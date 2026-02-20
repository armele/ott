package com.otterly76.ott.client.handler;

import com.otterly76.ott.client.util.ItemSlotMouseAction;
import com.otterly76.ott.client.util.ScrollWheelHandler;
import com.otterly76.ott.network.ServerboundSelectBundleItemPacket;
import com.otterly76.ott.util.item.BundleFeatures;
import com.otterly76.ott.util.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.joml.Vector2i;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = com.otterly76.ott.Constants.MOD_ID)
public class BundleMouseActions implements ItemSlotMouseAction {
    public static final BundleMouseActions INSTANCE = new BundleMouseActions();
    private final ScrollWheelHandler scrollWheelHandler = new ScrollWheelHandler();

    @SubscribeEvent
    public static void onMouseScrolled(ScreenEvent.MouseScrolled.Pre event) {
        if (!BundleFeatures.onBundleUpdate()) {
            return;
        }

        Screen screen = event.getScreen();
        if (screen instanceof AbstractContainerScreen<?> container) {
            Slot slot = container.getSlotUnderMouse();
            if (slot != null && slot.hasItem()) {
                if (INSTANCE.matches(slot) && INSTANCE.onMouseScrolled(event.getScrollDeltaY(), slot.index, slot.getItem())) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @Override
    public boolean matches(Slot slot) {
        return slot.getItem().is(ModTags.ItemTags.BUNDLES);
    }

    @Override
    public boolean onMouseScrolled(double scrollDelta, int slotId, ItemStack stack) {
        int itemsToShow = BundleFeatures.getNumberOfItemsToShow(stack);
        if (itemsToShow == 0) {
            return false;
        } else {
            Vector2i scroll = this.scrollWheelHandler.onMouseScroll(scrollDelta);
            int delta = scroll.y == 0 ? -scroll.x : scroll.y;
            if (delta != 0) {
                int selectedItem = BundleFeatures.getSelectedItem(stack);
                int selectedItemIndex = ScrollWheelHandler.getNextScrollWheelSelection(delta, selectedItem, itemsToShow);
                if (selectedItem != selectedItemIndex) {
                    this.toggleSelectedBundleItem(stack, slotId, selectedItemIndex);
                }
            }

            return true;
        }
    }

    @Override
    public void onStopHovering(Slot slot) {
        this.unselectedBundleItem(slot.getItem(), slot.index);
    }

    @Override
    public void onSlotClicked(Slot slot, ClickType clickType) {
        if (clickType == ClickType.QUICK_MOVE || clickType == ClickType.SWAP) {
            this.unselectedBundleItem(slot.getItem(), slot.index);
        }
    }

    private void toggleSelectedBundleItem(ItemStack stack, int slotId, int selectedItemIndex) {
        if (selectedItemIndex < BundleFeatures.getNumberOfItemsToShow(stack)) {
            BundleFeatures.toggleSelectedItem(stack, selectedItemIndex);
            var connection = Minecraft.getInstance().getConnection();
            if (connection != null) {
                connection.send(new ServerboundSelectBundleItemPacket(slotId, selectedItemIndex));
            }
        }
    }

    public void unselectedBundleItem(ItemStack stack, int slotId) {
        this.toggleSelectedBundleItem(stack, slotId, -1);
    }
}