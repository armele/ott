package com.otterly76.ott.client.handler;

import com.otterly76.ott.item.ModItems;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemPropertyRegistrar {
    public static void bootstrap() {
        ModItems.BUNDLES.values().forEach(item -> registerBundle(item.get()));
    }

    private static void registerBundle(Item item) {
        @SuppressWarnings("deprecation")
        ItemPropertyFunction function = ItemPropertyRegistrar::bundleDisplay;
        ItemProperties.register(item, ResourceLocation.withDefaultNamespace("filled"), function);
    }

    private static float bundleDisplay(ItemStack stack, ClientLevel level, LivingEntity entity, int i) {
        return BundleItem.getFullnessDisplay(stack);
    }
}
