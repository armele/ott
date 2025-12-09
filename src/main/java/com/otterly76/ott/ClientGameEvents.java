package com.otterly76.ott;

import com.mojang.blaze3d.systems.RenderSystem;
import com.otterly76.ott.mixin.GuiAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientGameEvents {

    private static final ResourceLocation ARMOR_EMPTY_SPRITE = ResourceLocation.withDefaultNamespace("hud/armor_empty");
    private static final ResourceLocation ARMOR_HALF_SPRITE = ResourceLocation.withDefaultNamespace("hud/armor_half");
    private static final ResourceLocation ARMOR_FULL_SPRITE = ResourceLocation.withDefaultNamespace("hud/armor_full");

    // TODO: define better colors

    private static final int[] ARMOR_COLORS = new int[]{
            0xFFFFFF, // Deck 0: Standard (Iron/White - No Tint)
            0x33EBCB, // Deck 1: Diamond Blue
            0x2196F3, // Deck 2: Lapis Blue
            0x4CAF50, // Deck 3: Emerald Green
            0xFFC107, // Deck 4: Gold
            0x9C27B0, // Deck 5: Purple
            0xF44336  // Deck 6: Red
    };

    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event) {
        if (VanillaGuiLayers.FOOD_LEVEL.equals(event.getName())) {
            event.setCanceled(true);
        }
        else if (VanillaGuiLayers.AIR_LEVEL.equals(event.getName())) {
            event.getGuiGraphics().pose().pushPose();
            event.getGuiGraphics().pose().translate(0, -10, 0);
        }
        else if (VanillaGuiLayers.ARMOR_LEVEL.equals(event.getName())) {
            event.setCanceled(true);
            renderOverloadedArmor(event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public static void onRenderGuiLayerPost(RenderGuiLayerEvent.Post event) {
        if (VanillaGuiLayers.AIR_LEVEL.equals(event.getName())) {
            event.getGuiGraphics().pose().popPose();
        }
    }

    private static void renderOverloadedArmor(GuiGraphics guiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        int armor = player.getArmorValue();
        if (armor <= 0) return;

        GuiAccessor accessor = (GuiAccessor) mc.gui;
        int leftHeight = accessor.getLeftHeight();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        int x = width / 2 - 91;

        // OFFSET FIX: Pushing the bar DOWN by 10 pixels to close the "empty row" gap.
        // y = height - leftHeight + 10
        int y = height - leftHeight + 9;

        int armorDeck = Math.max(0, (armor - 1) / 20);

        for (int i = 0; i < 10; ++i) {
            int armorValue = (i + 1) * 2;
            int xPos = x + i * 8;

            // Draw Empty Background (Always standard white tint)
            renderTintedSprite(guiGraphics, ARMOR_EMPTY_SPRITE, xPos, y, 0xFFFFFF);

            // Draw "Under" layer (full bar of previous tier color)
            if (armorDeck > 0) {
                // Deck - 1 so the background is always the "completed" previous tier
                int underColor = getArmorColor(armorDeck - 1);
                renderTintedSprite(guiGraphics, ARMOR_FULL_SPRITE, xPos, y, underColor);
            }

            // Draw "Current" layer
            int currentDeckArmor = armor - (armorDeck * 20);

            if (currentDeckArmor > (i * 2)) {
                boolean isHalf = (currentDeckArmor == armorValue - 1);

                // Use the standard deck color logic (ignoring materials)
                int currentColor = getArmorColor(armorDeck);

                ResourceLocation sprite = isHalf ? ARMOR_HALF_SPRITE : ARMOR_FULL_SPRITE;
                renderTintedSprite(guiGraphics, sprite, xPos, y, currentColor);
            }
        }

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        // Important: We still reserve the space for the next element
        accessor.setLeftHeight(leftHeight + 10);
    }

    private static void renderTintedSprite(GuiGraphics guiGraphics, ResourceLocation sprite, int x, int y, int color) {
        RenderSystem.enableBlend();
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        guiGraphics.setColor(r, g, b, 1.0F);
        guiGraphics.blitSprite(sprite, x, y, 9, 9);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    private static int getArmorColor(int deck) {
        // Deck 0 = White (Index 0)
        if (deck <= 0) return ARMOR_COLORS[0];

        // Decks 1+ = Cycle through indices 1 to (Length-1)
        // This keeps Index 0 (White) exclusive to the first layer
        int index = (deck - 1) % (ARMOR_COLORS.length - 1) + 1;
        return ARMOR_COLORS[index];
    }
}