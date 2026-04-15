package com.otterly76.ott.mixin.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieHorse.class)
public abstract class ZombieHorseMixin {

    private static final TagKey<Item> OTT$ZOMBIE_HORSE_FOOD =
            TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:zombie_horse_food"));

    // ── 1. Add FloatGoal + TemptGoal ─────────────────────────────────────────

    @Inject(method = "addBehaviourGoals()V", at = @At("HEAD"))
    private void ott$addBehaviourGoals(CallbackInfo ci) {
        ZombieHorse self = (ZombieHorse) (Object) this;
        self.goalSelector.addGoal(0, new FloatGoal(self));
        self.goalSelector.addGoal(3, new TemptGoal(self, 1.25F, stack -> stack.is(OTT$ZOMBIE_HORSE_FOOD), false));
    }

    // ── 2. Taming with zombie_horse_food ─────────────────────────────────────

    @Inject(method = "mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"), cancellable = true)
    private void ott$mobInteract(
            @NotNull Player player,
            @NotNull InteractionHand hand,
            CallbackInfoReturnable<InteractionResult> cir) {

        ZombieHorse self = (ZombieHorse) (Object) this;
        if (self.isTamed()) return;

        ItemStack itemStack = player.getItemInHand(hand);
        if (!itemStack.isEmpty() && itemStack.is(OTT$ZOMBIE_HORSE_FOOD)) {
            if (!self.level().isClientSide) {
                self.setPersistenceRequired();
                self.modifyTemper(3);
                itemStack.consume(1, player);
                if (self.getTemper() >= self.getMaxTemper()) {
                    self.tameWithName(player);
                    self.level().broadcastEntityEvent(self, (byte) 7);
                } else {
                    self.level().broadcastEntityEvent(self, (byte) 6);
                }
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(self.level().isClientSide));
        }
    }

    // ── 3. canBeLeashed: tamed OR no mob rider ────────────────────────────────
    // Note: canBeLeashed is declared in Mob, not ZombieHorse — handled via
    // vanilla default. ZombieHorse.canBeLeashed() falls through to AbstractHorse
    // which returns true only when tamed. Untamed zombie horses can't be leashed,
    // which is acceptable.
}
