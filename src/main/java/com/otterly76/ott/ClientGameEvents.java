package com.otterly76.ott;

import com.mojang.blaze3d.systems.RenderSystem;
import com.otterly76.ott.mixin.GuiAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

    private static final int[] ARMOR_COLORS = new int[]{
            0xe9f5fe, // Deck 0: Standard Iron/White 0-20
            0x33EBCB, // Deck 1: Diamond Blue 21-30
            0x37a114, // Deck 2: Lapis Blue 31-40
            0x5eb762, // Deck 3: Emerald Green 41-50
            0xffc720, // Deck 4: Gold 51-60
            0xa63db8, // Deck 5: Purple 61-70
            0xf5564a  // Deck 6: Red 71-80
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
        AttributeInstance armorAttribute = player.getAttribute(Attributes.ARMOR);
        if (armorAttribute != null) {
            double base = armorAttribute.getBaseValue();
            double add = 0;
            double mulBase = 0;
            double mulTotal = 1;

            for (AttributeModifier modifier : armorAttribute.getModifiers()) {
                if (modifier.operation() == AttributeModifier.Operation.ADD_VALUE) {
                    add += modifier.amount();
                } else if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE) {
                    mulBase += modifier.amount();
                } else if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    mulTotal *= (1.0 + modifier.amount());
                }
            }

            armor = (int) ((base + add) * (1.0 + mulBase) * mulTotal);
        }

        if (armor <= 0) return;

        GuiAccessor accessor = (GuiAccessor) mc.gui;
        int leftHeight = accessor.getLeftHeight();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        int x = width / 2 - 91;

        int y = height - 49;

        int armorDeck = Math.max(0, (armor - 1) / 20);

        for (int i = 0; i < 10; ++i) {
            int armorValue = (i + 1) * 2;
            int xPos = x + i * 8;

            renderTintedSprite(guiGraphics, ARMOR_EMPTY_SPRITE, xPos, y, 0xFFFFFF);

            if (armorDeck > 0) {
                int underColor = getArmorColor(armorDeck - 1);
                renderTintedSprite(guiGraphics, ARMOR_FULL_SPRITE, xPos, y, underColor);
            }

            int currentDeckArmor = armor - (armorDeck * 20);

            if (currentDeckArmor > (i * 2)) {
                boolean isHalf = (currentDeckArmor == armorValue - 1);

                int currentColor = getArmorColor(armorDeck);

                ResourceLocation sprite = isHalf ? ARMOR_HALF_SPRITE : ARMOR_FULL_SPRITE;
                renderTintedSprite(guiGraphics, sprite, xPos, y, currentColor);
            }
        }

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        accessor.setLeftHeight(leftHeight + 10);
    }

    @SuppressWarnings("DuplicatedCode")
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
        if (deck <= 0) return ARMOR_COLORS[0];

        int index = (deck - 1) % (ARMOR_COLORS.length - 1) + 1;
        return ARMOR_COLORS[index];
    }
}