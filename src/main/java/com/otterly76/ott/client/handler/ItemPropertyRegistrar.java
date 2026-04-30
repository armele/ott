package com.otterly76.ott.client.handler;

import com.otterly76.ott.event.EatingAnimationTicker;
import com.otterly76.ott.item.ModItems;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;

public class ItemPropertyRegistrar {
    public static void bootstrap() {
        ModItems.BUNDLES.values().forEach(item -> registerBundle(item.get()));
        registerBucketVariant(ModItems.DUMBO_OCTOPUS_BUCKET.get());
        registerBucketVariant(ModItems.SEA_BUNNY_BUCKET.get());
        registerEatingAnimationProperties();
    }

    private static void registerEatingAnimationProperties() {
        ItemProperties.registerGeneric(ResourceLocation.fromNamespaceAndPath("ott", "eating"),
                (stack, level, entity, seed) -> {
                    if (entity == null) return 0f;
                    return (entity.isUsingItem() && entity.getUseItem() == stack
                            && stack.has(DataComponents.FOOD)) ? 1f : 0f;
                });

        ItemProperties.registerGeneric(ResourceLocation.fromNamespaceAndPath("ott", "eat"),
                (stack, level, entity, seed) -> {
                    if (entity == null) return 0f;
                    if (entity instanceof RemotePlayer rp && rp.getTicksUsingItem() > 31) {
                        return EatingAnimationTicker.animationTicks / 30f;
                    }
                    return (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 30.0f;
                });

        ItemProperties.registerGeneric(ResourceLocation.fromNamespaceAndPath("ott", "drinking"),
                (stack, level, entity, seed) -> {
                    if (entity == null) return 0f;
                    return (entity.isUsingItem() && entity.getUseItem() == stack
                            && stack.getItem().getUseAnimation(stack) == UseAnim.DRINK) ? 1f : 0f;
                });

        ItemProperties.registerGeneric(ResourceLocation.fromNamespaceAndPath("ott", "drink"),
                (stack, level, entity, seed) -> {
                    if (entity == null) return 0f;
                    if (entity instanceof RemotePlayer rp && rp.getTicksUsingItem() > 31) {
                        return EatingAnimationTicker.animationTicks / 30f;
                    }
                    return (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 30.0f;
                });
    }

    private static void registerBundle(Item item) {
        ClampedItemPropertyFunction function = ItemPropertyRegistrar::bundleDisplay;
        ItemProperties.register(item, ResourceLocation.withDefaultNamespace("filled"), function);
    }

    private static void registerBucketVariant(Item item) {
        ClampedItemPropertyFunction function = ItemPropertyRegistrar::bucketVariant;
        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath("ott", "variant"), function);
    }

    private static float bundleDisplay(ItemStack stack, ClientLevel level, LivingEntity entity, int i) {
        return BundleItem.getFullnessDisplay(stack);
    }

    private static float bucketVariant(ItemStack stack, ClientLevel level, LivingEntity entity, int seed) {
        CustomData bucketData = stack.get(DataComponents.BUCKET_ENTITY_DATA);
        if (bucketData == null) return 0.0f;
        return bucketData.copyTag().getInt("Variant");
    }
}
