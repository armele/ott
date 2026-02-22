package com.otterly76.ott.mixin.common;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ExperienceOrb.class, priority = 1500)
public abstract class ExperienceOrbMixin extends Entity {

    @Shadow
    public int value;

    @Shadow
    private int count;

    @Shadow
    private int age;

    @Shadow
    protected abstract int repairPlayerItems(ServerPlayer player, int amount);

    public ExperienceOrbMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void ott$onTick(CallbackInfo ci) {
        if (OttConfig.CLUMPS.ENABLED.get() && !this.level().isClientSide && this.isAlive() && this.age % 20 == 0) {
            double radius = OttConfig.CLUMPS.RADIUS.get();
            List<ExperienceOrb> nearbyOrbs = this.level().getEntitiesOfClass(
                    ExperienceOrb.class,
                    this.getBoundingBox().inflate(radius),
                    orb -> orb != (Object) this && orb.isAlive()
            );

            for (ExperienceOrb other : nearbyOrbs) {
                this.value += other.getValue();
                this.count += ((ExperienceOrbAccessor) other).ott$getCount();
                other.discard();
            }
        }
    }

    @ModifyConstant(method = "tick", constant = @Constant(doubleValue = 8.0D))
    private double ott$getAttractionRadius(double constant) {
        return OttConfig.CLUMPS.ENABLED.get() ? OttConfig.CLUMPS.ATTRACTION_RADIUS.get() : constant;
    }

    @ModifyConstant(method = "tick", constant = @Constant(doubleValue = 64.0D))
    private double ott$getAttractionRadiusSqr(double constant) {
        if (OttConfig.CLUMPS.ENABLED.get()) {
            double radius = OttConfig.CLUMPS.ATTRACTION_RADIUS.get();
            return radius * radius;
        }
        return constant;
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 6000))
    private int ott$getDespawnTime(int constant) {
        return OttConfig.CLUMPS.ENABLED.get() && OttConfig.CLUMPS.EVERLASTING.get() ? Integer.MAX_VALUE : constant;
    }

    @Inject(method = "repairPlayerItems", at = @At("HEAD"), cancellable = true)
    private void ott$repairInventoryItems(ServerPlayer player, int amount, CallbackInfoReturnable<Integer> cir) {
        if (OttConfig.GENERAL.INVENTORY_MENDING.get()) {
            List<ItemStack> mendableItems = new ArrayList<>();
            for (ItemStack stack : player.getInventory().items) {
                if (!stack.isEmpty() && stack.isDamaged() && EnchantmentHelper.has(stack, EnchantmentEffectComponents.REPAIR_WITH_XP)) {
                    mendableItems.add(stack);
                }
            }
            for (ItemStack stack : player.getInventory().armor) {
                if (!stack.isEmpty() && stack.isDamaged() && EnchantmentHelper.has(stack, EnchantmentEffectComponents.REPAIR_WITH_XP)) {
                    mendableItems.add(stack);
                }
            }
            for (ItemStack stack : player.getInventory().offhand) {
                if (!stack.isEmpty() && stack.isDamaged() && EnchantmentHelper.has(stack, EnchantmentEffectComponents.REPAIR_WITH_XP)) {
                    mendableItems.add(stack);
                }
            }

            if (mendableItems.isEmpty()) {
                cir.setReturnValue(amount);
                return;
            }

            ItemStack itemstack = mendableItems.get(player.getRandom().nextInt(mendableItems.size()));
            int durabilityToRepair = (int) (amount * itemstack.getXpRepairRatio());
            durabilityToRepair = EnchantmentHelper.modifyDurabilityToRepairFromXp(player.serverLevel(), itemstack, durabilityToRepair);

            int actualRepair = Math.min(durabilityToRepair, itemstack.getDamageValue());
            itemstack.setDamageValue(itemstack.getDamageValue() - actualRepair);

            if (actualRepair > 0) {
                int xpUsed = actualRepair * amount / durabilityToRepair;
                int remainingXp = amount - xpUsed;
                if (remainingXp > 0) {
                    cir.setReturnValue(this.repairPlayerItems(player, remainingXp));
                } else {
                    cir.setReturnValue(0);
                }
            } else {
                cir.setReturnValue(0);
            }
        }
    }

    @Override
    public void gameEvent(@NotNull Holder<GameEvent> event, Entity entity) {
        // Experience orbs should not trigger game events (vibrations)
    }
}