package com.otterly76.ott.client.render.layers;

import com.google.common.collect.ImmutableMap;
import com.otterly76.ott.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class GhastHarnessLayer {
    public static final Map<ItemStack, ResourceLocation> TEXTURE_BY_ITEM;

    static {
        TEXTURE_BY_ITEM = (new ImmutableMap.Builder<ItemStack, ResourceLocation>())
                .put(new ItemStack(ModItems.HARNESSES.get("white").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/white_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("orange").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/orange_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("magenta").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/magenta_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("light_blue").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/light_blue_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("yellow").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/yellow_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("lime").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/lime_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("pink").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/pink_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("gray").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/gray_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("light_gray").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/light_gray_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("cyan").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/cyan_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("purple").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/purple_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("blue").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/blue_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("brown").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/brown_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("green").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/green_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("red").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/red_harness.png"))
                .put(new ItemStack(ModItems.HARNESSES.get("black").get()), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/black_harness.png")).build();
    }
}
