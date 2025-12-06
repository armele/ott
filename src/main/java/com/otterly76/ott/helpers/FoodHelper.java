package com.otterly76.ott.helpers;

import com.otterly76.ott.api.event.FoodValuesEvent;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

public class FoodHelper {
    public static FoodProperties EMPTY_FOOD_PROPERTIES = (new FoodProperties.Builder()).build();
    public static float REGEN_EXHAUSTION_INCREMENT = 6.0F;
    public static float MAX_EXHAUSTION = 4.0F;

    public static boolean isFood(ItemStack itemStack, Player player) {
        return itemStack.getFoodProperties(player) != null;
    }

    public static boolean canConsume(Player player, FoodProperties foodProperties) {
        return player.canEat(foodProperties.canAlwaysEat());
    }

    public static FoodProperties getDefaultFoodValues(ItemStack itemStack, Player player) {
        FoodProperties properties = itemStack.getFoodProperties(player);
        return properties != null ? properties : EMPTY_FOOD_PROPERTIES;
    }

    public static @Nullable QueriedFoodResult query(ItemStack itemStack, Player player) {
        if (!isFood(itemStack, player)) {
            return null;
        } else {
            FoodProperties defaultFood = getDefaultFoodValues(itemStack, player);
            FoodValuesEvent foodValuesEvent = new FoodValuesEvent(player, itemStack, defaultFood, defaultFood);
            NeoForge.EVENT_BUS.post(foodValuesEvent);
            return new QueriedFoodResult(foodValuesEvent.defaultFoodProperties, foodValuesEvent.modifiedFoodProperties, itemStack);
        }
    }

    public static boolean isRotten(FoodProperties foodProperties) {
        for (FoodProperties.PossibleEffect effect : foodProperties.effects()) {
            MobEffectInstance effectInstance = effect.effect();
            if (effectInstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                return true;
            }
        }

        return false;
    }

    public static float getEstimatedHealthIncrement(Player player, FoodProperties foodProperties) {
        if (!player.isHurt()) {
            return 0.0F;
        } else {
            FoodData stats = player.getFoodData();
            Level world = player.getCommandSenderWorld();
            int foodLevel = Math.min(stats.getFoodLevel() + foodProperties.nutrition(), 20);
            float healthIncrement = 0.0F;
            if ((float) foodLevel >= 18.0F && world.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
                float saturationLevel = Math.min(stats.getSaturationLevel() + foodProperties.saturation(), (float) foodLevel);
                float exhaustionLevel = stats.getExhaustionLevel();
                healthIncrement = getEstimatedHealthIncrement(foodLevel, saturationLevel, exhaustionLevel);
            }

            for (FoodProperties.PossibleEffect effect : foodProperties.effects()) {
                MobEffectInstance effectInstance = effect.effect();
                if (effectInstance.is(MobEffects.REGENERATION)) {
                    int amplifier = effectInstance.getAmplifier();
                    int duration = effectInstance.getDuration();
                    healthIncrement += (float) (double) (duration / Math.max(50 >> amplifier, 1));
                    break;
                }
            }

            return healthIncrement;
        }
    }

    public static float getEstimatedHealthIncrement(int foodLevel, float saturationLevel, float exhaustionLevel) {
        float health = 0.0F;
        if (Float.isFinite(exhaustionLevel) && Float.isFinite(saturationLevel)) {
            while (foodLevel >= 18) {
                while (exhaustionLevel > MAX_EXHAUSTION) {
                    exhaustionLevel -= MAX_EXHAUSTION;
                    if (saturationLevel > 0.0F) {
                        saturationLevel = Math.max(saturationLevel - 1.0F, 0.0F);
                    } else {
                        --foodLevel;
                    }
                }

                if (foodLevel >= 20 && Float.compare(saturationLevel, Float.MIN_NORMAL) > 0) {
                    float limitedSaturationLevel = Math.min(saturationLevel, REGEN_EXHAUSTION_INCREMENT);
                    float exhaustionUntilAboveMax = Math.nextUp(MAX_EXHAUSTION) - exhaustionLevel;
                    int numIterationsUntilAboveMax = Math.max(1, (int) Math.ceil(exhaustionUntilAboveMax / limitedSaturationLevel));
                    health += limitedSaturationLevel / REGEN_EXHAUSTION_INCREMENT * (float) numIterationsUntilAboveMax;
                    exhaustionLevel += limitedSaturationLevel * (float) numIterationsUntilAboveMax;
                } else if (foodLevel >= 18) {
                    ++health;
                    exhaustionLevel += REGEN_EXHAUSTION_INCREMENT;
                }
            }

            return health;
        } else {
            return 0.0F;
        }
    }

    public static class QueriedFoodResult {
        public FoodProperties defaultFoodProperties;
        public FoodProperties modifiedFoodProperties;
        public final ItemStack itemStack;

        public QueriedFoodResult(FoodProperties defaultFoodProperties, FoodProperties modifiedFoodProperties, ItemStack itemStack) {
            this.defaultFoodProperties = defaultFoodProperties;
            this.modifiedFoodProperties = modifiedFoodProperties;
            this.itemStack = itemStack;
        }
    }
}