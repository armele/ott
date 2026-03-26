package com.otterly76.ott.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RawGameItem extends Item {
    public RawGameItem() {
        super(new Item.Properties()
                .stacksTo(64)
                .food(new FoodProperties.Builder()
                        .nutrition(1)
                        .saturationModifier(1.0F)
                        .alwaysEdible()
                        .build()));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level world, @NotNull LivingEntity entity) {
        if (!world.isClientSide) {
            entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 600, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 600, 1));
        }

        ItemStack result = super.finishUsingItem(stack, world, entity);

        if (stack.isEmpty()) {
            return new ItemStack(Items.BOWL);
        } else {
            if (entity instanceof Player player && !player.getAbilities().instabuild) {
                ItemStack bowl = new ItemStack(Items.BOWL);
                if (!player.getInventory().add(bowl)) {
                    player.drop(bowl, false);
                }
            }
            return result;
        }
    }
}