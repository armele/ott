package com.otterly76.ott.client;

import com.mojang.blaze3d.systems.RenderSystem;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import com.otterly76.ott.api.event.HUDOverlayEvent;
import com.otterly76.ott.helpers.HungerHelper;
import com.otterly76.ott.helpers.TextureHelper;
import com.otterly76.ott.helpers.TextureHelper.FoodType;
import com.otterly76.ott.helpers.TextureHelper.HeartType;
import com.otterly76.ott.util.IntPoint;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.LayeredDraw;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class HUDOverlayHandler {
    private static float unclampedFlashAlpha = 0.0F;
    private static float flashAlpha = 0.0F;
    private static byte alphaDir = 1;
    protected static int foodIconsOffset;
    protected static int healthIconsOffset;
    private static final OffsetsCache barOffsets = new OffsetsCache();
    private static final HeldFoodCache heldFood = new HeldFoodCache();
    private static final RandomSource random = RandomSource.create();

    public static void register(RegisterGuiLayersEvent event) {
        event.registerBelow(VanillaGuiLayers.PLAYER_HEALTH, ResourceLocation.fromNamespaceAndPath("ott", "health_offset"), (guiGraphics, deltaTracker) -> healthIconsOffset = Minecraft.getInstance().gui.leftHeight);
        event.registerBelow(VanillaGuiLayers.FOOD_LEVEL, ResourceLocation.fromNamespaceAndPath("ott", "food_offset"), (guiGraphics, deltaTracker) -> foodIconsOffset = Minecraft.getInstance().gui.rightHeight);
        event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH, ResourceLocation.fromNamespaceAndPath("ott", "health_overlay"), new HealthOverlay());
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, ResourceLocation.fromNamespaceAndPath("ott", "hunger_overlay"), new HungerOverlay());
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, ResourceLocation.fromNamespaceAndPath("ott", "saturation_overlay"), new SaturationOverlay());
        event.registerBelow(VanillaGuiLayers.FOOD_LEVEL, ResourceLocation.fromNamespaceAndPath("ott", "exhaustion_overlay"), new ExhaustionOverlay());
        NeoForge.EVENT_BUS.addListener(HUDOverlayHandler::onClientTick);
    }

    public static void drawSaturationOverlay(float saturationGained, float saturationLevel, Player player, GuiGraphics guiGraphics, int right, int top, float alpha, int guiTicks) {
        if (saturationLevel + saturationGained >= 0.0F) {
            List<IntPoint> offsets = barOffsets.foodBarOffsets(guiTicks, player);
            int iconSize = 9;

            enableAlpha(1.0F);
            int endStaticBar = (int) Math.ceil(saturationLevel / 2.0F);
            for (int i = 0; i < endStaticBar; ++i) {
                IntPoint offset = i < offsets.size() ? offsets.get(i) : new IntPoint();
                if (offset != null) {
                    int x = right + offset.x;
                    int y = top + offset.y;
                    int v = 0;
                    int u = 0;
                    float effectiveSaturationOfBar = saturationLevel / 2.0F - (float) i;
                    if (effectiveSaturationOfBar >= 1.0F) {
                        u = 3 * iconSize;
                    } else if (effectiveSaturationOfBar > 0.5F) {
                        u = 2 * iconSize;
                    } else if (effectiveSaturationOfBar > 0.25F) {
                        u = iconSize;
                    }
                    guiGraphics.blit(TextureHelper.MOD_ICONS, x, y, u, v, iconSize, iconSize);
                }
            }
            disableAlpha(1.0F);

            if (saturationGained != 0.0F) {
                enableAlpha(alpha);
                float modifiedSaturation = Math.max(0.0F, Math.min(saturationLevel + saturationGained, 20.0F));
                int startGainedBar = (int) Math.max(saturationLevel / 2.0F, 0.0F);
                int endGainedBar = (int) Math.ceil(modifiedSaturation / 2.0F);

                for (int i = startGainedBar; i < endGainedBar; ++i) {
                    IntPoint offset = i < offsets.size() ? offsets.get(i) : new IntPoint();
                    if (offset != null) {
                        int x = right + offset.x;
                        int y = top + offset.y;
                        int v = 0;
                        int u = 0;
                        float effectiveSaturationOfBar = modifiedSaturation / 2.0F - (float) i;
                        if (effectiveSaturationOfBar >= 1.0F) {
                            u = 3 * iconSize;
                        } else if (effectiveSaturationOfBar > 0.5F) {
                            u = 2 * iconSize;
                        } else if (effectiveSaturationOfBar > 0.25F) {
                            u = iconSize;
                        }
                        guiGraphics.blit(TextureHelper.MOD_ICONS, x, y, u, v, iconSize, iconSize);
                    }
                }
                disableAlpha(alpha);
            }
        }
    }

    public static void drawHungerOverlay(int hungerRestored, int foodLevel, Player player, GuiGraphics guiGraphics, int right, int top, float alpha, boolean useRottenTextures, int guiTicks) {
        if (hungerRestored > 0) {
            enableAlpha(alpha);
            int modifiedFood = Math.max(0, Math.min(20, foodLevel + hungerRestored));
            int startFoodBars = Math.max(0, foodLevel / 2);
            int endFoodBars = (int) Math.ceil((float) modifiedFood / 2.0F);
            int iconStartOffset = 16;
            int iconSize = 9;
            List<IntPoint> offsets = barOffsets.foodBarOffsets(guiTicks, player);
            for (int i = startFoodBars; i < endFoodBars; ++i) {
                IntPoint offset = i < offsets.size() ? offsets.get(i) : new IntPoint();
                if (offset != null) {
                    int x = right + offset.x;
                    int y = top + offset.y;
                    ResourceLocation backgroundSprite = TextureHelper.getFoodTexture(useRottenTextures, FoodType.EMPTY);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha * 0.25F);
                    guiGraphics.blitSprite(backgroundSprite, x, y, iconSize, iconSize);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
                    boolean isHalf = i * 2 + 1 == modifiedFood;
                    ResourceLocation iconSprite = TextureHelper.getFoodTexture(useRottenTextures, isHalf ? FoodType.HALF : FoodType.FULL);
                    guiGraphics.blitSprite(iconSprite, x, y, iconSize, iconSize);
                }
            }

            disableAlpha(alpha);
        }
    }

    public static void drawHealthOverlay(float health, float modifiedHealth, Player player, GuiGraphics guiGraphics, int right, int top, float alpha, int guiTicks) {
        if (modifiedHealth > health) {
            enableAlpha(alpha);
            int fixedModifiedHealth = (int) Math.ceil(modifiedHealth);
            boolean isHardcore = player.level().getLevelData().isHardcore();
            int startHealthBars = (int) Math.max(0.0F, Math.ceil(health) / (double) 2.0F);
            int endHealthBars = (int) Math.max(0.0F, Math.ceil(modifiedHealth / 2.0F));
            int iconSize = 9;
            List<IntPoint> offsets = barOffsets.healthBarOffsets(guiTicks, player);
            for (int i = startHealthBars; i < endHealthBars; ++i) {
                IntPoint offset = i < offsets.size() ? offsets.get(i) : new IntPoint();
                if (offset != null) {
                    int x = right + offset.x;
                    int y = top + offset.y;
                    ResourceLocation backgroundSprite = TextureHelper.getHeartTexture(isHardcore, HeartType.CONTAINER);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha * 0.25F);
                    guiGraphics.blitSprite(backgroundSprite, x, y, iconSize, iconSize);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
                    boolean isHalf = i * 2 + 1 == fixedModifiedHealth;
                    ResourceLocation iconSprite = TextureHelper.getHeartTexture(isHardcore, isHalf ? HeartType.HALF : HeartType.FULL);
                    guiGraphics.blitSprite(iconSprite, x, y, iconSize, iconSize);
                }
            }

            disableAlpha(alpha);
        }
    }

    public static void drawExhaustionOverlay(float exhaustion, Player player, GuiGraphics guiGraphics, int right, int top, float alpha) {
        float maxExhaustion = HungerHelper.getMaxExhaustion(player);
        float ratio = Math.min(1.0F, Math.max(0.0F, exhaustion / maxExhaustion));
        int width = (int) (ratio * 81.0F);
        int height = 9;
        enableAlpha(alpha);
        guiGraphics.blit(TextureHelper.MOD_ICONS, right - width, top, 81 - width, 18, width, height);
        disableAlpha(alpha);
    }

    public static void enableAlpha(float alpha) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.blendFunc(770, 771);
    }

    public static void disableAlpha(float alpha) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        unclampedFlashAlpha += (float) alphaDir * 0.125F;
        if (unclampedFlashAlpha >= 1.5F) {
            alphaDir = -1;
        } else if (unclampedFlashAlpha <= -0.5F) {
            alphaDir = 1;
        }
        flashAlpha = Math.max(0.0F, Math.min(1.0F, unclampedFlashAlpha)) * 0.65F;
    }

    public static void resetFlash() {
        flashAlpha = 0.0F;
        unclampedFlashAlpha = 0.0F;
        alphaDir = 1;
    }

    private static void drawExhaustionOverlay(HUDOverlayEvent.Exhaustion event, Player player) {
        if (event.isCanceled()) {
            return;
        }
        drawExhaustionOverlay(event.exhaustion, player, event.guiGraphics, event.x, event.y, (float) 1.0);
    }

    private static boolean shouldShowEstimatedHealth(Player player) {
        FoodData stats = player.getFoodData();
        if (player.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        } else if (stats.getFoodLevel() >= 18) {
            return false;
        } else if (player.hasEffect(MobEffects.POISON)) {
            return false;
        } else if (player.hasEffect(MobEffects.WITHER)) {
            return false;
        } else {
            return !player.hasEffect(MobEffects.REGENERATION);
        }
    }

    public static class HealthOverlay implements LayeredDraw.Layer {
        @Override
        public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && !mc.options.hideGui) {
                assert mc.gameMode != null;
                if (mc.gameMode.canHurtPlayer()) {
                    float health = mc.player.getHealth();
                    float modifiedHealth = heldFood.getPotentialHealth(mc.player);
                    drawHealthOverlay(health, modifiedHealth, mc.player, guiGraphics, guiGraphics.guiWidth() / 2 - 91, guiGraphics.guiHeight() - healthIconsOffset, flashAlpha, mc.gui.getGuiTicks());
                }
            }
        }
    }

    public static class HungerOverlay implements LayeredDraw.Layer {
        @Override
        public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && !mc.options.hideGui && !mc.player.isCreative() && !mc.player.isSpectator()) {
                int hungerRestored = heldFood.getPotentialHunger(mc.player);
                boolean useRottenTextures = mc.player.hasEffect(MobEffects.HUNGER);
                drawHungerOverlay(hungerRestored, mc.player.getFoodData().getFoodLevel(), mc.player, guiGraphics, guiGraphics.guiWidth() / 2 + 91, guiGraphics.guiHeight() - foodIconsOffset, flashAlpha, useRottenTextures, mc.gui.getGuiTicks());
            }
        }
    }

    public static class SaturationOverlay implements LayeredDraw.Layer {
        @Override
        public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && !mc.options.hideGui && !mc.player.isCreative() && !mc.player.isSpectator()) {
                float saturationGained = heldFood.getPotentialSaturation(mc.player);
                drawSaturationOverlay(saturationGained, mc.player.getFoodData().getSaturationLevel(), mc.player, guiGraphics, guiGraphics.guiWidth() / 2 + 91, guiGraphics.guiHeight() - foodIconsOffset, flashAlpha, mc.gui.getGuiTicks());
            }
        }
    }

    public static class ExhaustionOverlay implements LayeredDraw.Layer {
        @Override
        public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && !mc.options.hideGui && !mc.player.isCreative() && !mc.player.isSpectator()) {
                HUDOverlayEvent.Exhaustion event = new HUDOverlayEvent.Exhaustion(mc.player.getFoodData().getExhaustionLevel(), guiGraphics.guiWidth() / 2 + 91, guiGraphics.guiHeight() - foodIconsOffset, guiGraphics);
                NeoForge.EVENT_BUS.post(event);
                drawExhaustionOverlay(event, mc.player);
            }
        }
    }

    private static class OffsetsCache {
        private final List<IntPoint> defaults;

        public OffsetsCache() {
            this.defaults = new java.util.ArrayList<>();
            for (int i = 0; i < 10; i++) {
                this.defaults.add(new IntPoint());
            }
        }

        public List<IntPoint> foodBarOffsets(int guiTicks, Player player) {
            return defaults;
        }

        public List<IntPoint> healthBarOffsets(int guiTicks, Player player) {
            return defaults;
        }
    }

    private static class HeldFoodCache {
        public float getPotentialHealth(Player player) {
            return player.getHealth();
        }

        public int getPotentialHunger(Player player) {
            ItemStack stack = getFoodItem(player);
            FoodProperties food = stack.getFoodProperties(player);
            return food != null ? food.nutrition() : 0;
        }

        public float getPotentialSaturation(Player player) {
            ItemStack stack = getFoodItem(player);
            FoodProperties food = stack.getFoodProperties(player);
            return food != null ? food.nutrition() * food.saturation() * 2.0f : 0.0f;
        }

        private ItemStack getFoodItem(Player player) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getFoodProperties(player) == null) {
                stack = player.getOffhandItem();
            }
            return stack;
        }
    }
}