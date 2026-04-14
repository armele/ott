package com.otterly76.ott.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.function.Consumer;

public class SpearItem extends SwordItem {

    /** Extra entity-interaction range added on top of the vanilla default (3.0 blocks). */
    private static final double RANGE_BONUS = 1.5;
    private static final ResourceLocation SPEAR_RANGE_ID =
            ResourceLocation.withDefaultNamespace("base_entity_reach_spear");

    /** Minimum charge time (ticks) before a charge attack triggers. */
    private static final int MIN_CHARGE_TICKS = 5;
    /** Maximum charge bonus damage (at full sprint speed ~0.3 m/tick). */
    private static final float MAX_CHARGE_BONUS = 8.0F;

    public SpearItem(Tier tier, Item.Properties properties) {
        super(tier, properties);
    }

    // ── Attribute helpers ─────────────────────────────────────────────────────

    /**
     * Builds spear item attribute modifiers: sword base stats + extra entity reach.
     * Use this instead of {@link SwordItem#createAttributes} during item registration.
     */
    public static @NotNull ItemAttributeModifiers createAttributes(Tier tier, int attackDamage, float attackSpeed) {
        return SwordItem.createAttributes(tier, attackDamage, attackSpeed)
                .withModifierAdded(
                        Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(SPEAR_RANGE_ID, RANGE_BONUS, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                );
    }

    // ── Use animation ─────────────────────────────────────────────────────────

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }

    /** Right-click starts the charge pose. */
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    // ── Charge attack on release ──────────────────────────────────────────────

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;
        int chargeTicks = getUseDuration(stack, player) - timeLeft;
        if (chargeTicks < MIN_CHARGE_TICKS) return;
        if (level.isClientSide) return;

        double range = 3.0 + RANGE_BONUS + 0.5;
        HitResult hit = ProjectileUtil.getHitResultOnViewVector(
                player,
                e -> !e.isSpectator() && e.isPickable() && e != player,
                range
        );

        if (hit.getType() != HitResult.Type.ENTITY) return;
        if (!(((EntityHitResult) hit).getEntity() instanceof LivingEntity target)) return;

        float speed = (float) player.getDeltaMovement().horizontalDistance();
        float bonusDamage = Math.min(speed * 28.0F, MAX_CHARGE_BONUS);

        target.hurt(level.damageSources().playerAttack(player), bonusDamage);
        stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
    }

    // ── First-person animation: lower and level ───────────────────────────────

    /**
     * Replaces the vanilla SPEAR (trident-raise) animation with the 1.21.11 spear
     * "lower and level" pose, derived from SpearAnimations.firstPersonUse bytecode.
     * <p>
     * Key values (from SpearAnimations.class):
     *   XP:  0° → −65° (easeInOutBack) around pivot (0, 0.1, 0)
     *   YN:  k * (0° → −90°) around pivot (k*0.15, 0, 0), starts at 50% charge
     */
    @Override
    @SuppressWarnings("removal")
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public boolean applyForgeHandTransform(@NotNull PoseStack poseStack, @NotNull LocalPlayer player,
                                                   @NotNull HumanoidArm arm, @NotNull ItemStack itemInHand,
                                                   float partialTick, float equipProgress,
                                                   float swingProgress) {
                // Only intercept the active-use state for the arm holding this spear.
                HumanoidArm usingArm = player.getUsedItemHand() == InteractionHand.MAIN_HAND
                        ? player.getMainArm() : player.getMainArm().getOpposite();
                if (!player.isUsingItem() || player.getUseItemRemainingTicks() <= 0
                        || arm != usingArm) {
                    return false; // vanilla handles idle/swing
                }

                int k = arm == HumanoidArm.RIGHT ? 1 : -1;

                // Replicate applyItemArmTransform (private in ItemInHandRenderer)
                poseStack.translate(k * 0.56F, -0.52F + equipProgress * -0.6F, -0.72F);

                // Charge progress 0→1 over 10 ticks
                float ticksUsed = itemInHand.getUseDuration(player)
                        - (float) player.getUseItemRemainingTicks() + partialTick;
                float rawP = Mth.clamp(ticksUsed / 10.0F, 0.0F, 1.0F);
                // easeInOutBack — matches SpearAnimations.java Ease.inOutBack call
                float raiseProgress = easeInOutBack(rawP);

                // Additional translate (mirrors SpearAnimations firstPersonUse,
                // simplified: no sway — approx (k*0.15*raiseProgress, -0.075*raiseProgress, -0.05))
                poseStack.translate(
                        k * 0.15F * raiseProgress,
                        -0.075F * raiseProgress,
                        -0.05F * raiseProgress
                );

                // X rotation: 0° at start → −65° at full charge
                // Pivot at (0, 0.1, 0) — matches bytecode offset 194 rotateAround call
                Quaternionf xRot = Axis.XP.rotationDegrees(-65.0F * raiseProgress);
                poseStack.rotateAround(xRot, 0.0F, 0.1F, 0.0F);

                // Y rotation: kicks in after 50% charge (raiseProgress 0.5→0.55 maps to 0→1)
                // Pivot at (k*0.15, 0, 0) — matches bytecode offset 251 rotateAround call
                float yProgress = Mth.clamp((raiseProgress - 0.5F) / 0.05F, 0.0F, 1.0F);
                Quaternionf yRot = Axis.YN.rotationDegrees((float) k * (-90.0F * yProgress));
                poseStack.rotateAround(yRot, (float) k * 0.15F, 0.0F, 0.0F);

                return true;
            }

            /** CSS easeInOutBack — overshoot slightly at both ends. */
            private float easeInOutBack(float t) {
                float c2 = 1.70158F * 1.525F;
                if (t < 0.5F) {
                    float x = 2.0F * t;
                    return (x * x * ((c2 + 1.0F) * x - c2)) / 2.0F;
                } else {
                    float x = 2.0F * t - 2.0F;
                    return (x * x * ((c2 + 1.0F) * x + c2) + 2.0F) / 2.0F;
                }
            }
        });
    }
}