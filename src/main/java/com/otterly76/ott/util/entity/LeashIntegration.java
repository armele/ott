package com.otterly76.ott.util.entity;

import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import org.jetbrains.annotations.Nullable;

public class LeashIntegration {
    public static InteractionResult onInteract(Player player, Entity entity, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!entity.level().isClientSide() && player.isSecondaryUseActive() && entity instanceof Leashable leashable) {
            if (leashable.canBeLeashed() && entity.isAlive()) {
                label73: {
                    if (entity instanceof LivingEntity living) {
                        if (living.isBaby()) {
                            break label73;
                        }
                    }

                    List<Leashable> nearbyMobs = LeashExtension.vb$leashableInArea(entity, (l) -> l.getLeashHolder() == player);
                    if (!nearbyMobs.isEmpty()) {
                        boolean attachedAny = false;

                        for(Leashable target : nearbyMobs) {
                            if (((LeashExtension)target).vb$canHaveALeashAttachedTo(entity)) {
                                target.setLeashedTo(entity, true);
                                attachedAny = true;
                            }
                        }

                        if (attachedAny) {
                            entity.level().gameEvent(GameEvent.ENTITY_ACTION, entity.blockPosition(), Context.of(player));
                            entity.playSound(SoundEvents.LEASH_KNOT_PLACE);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }

        if (stack.is(Items.SHEARS) && shearOffAllLeashConnections(entity, player)) {
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            return InteractionResult.SUCCESS;
        } else {
            if (entity.isAlive() && entity instanceof Leashable leashable) {
                if (leashable.getLeashHolder() == player) {
                    if (!entity.level().isClientSide()) {
                        leashable.dropLeash(true, !player.isCreative());
                        entity.level().gameEvent(GameEvent.ENTITY_INTERACT, entity.position(), Context.of(player));
                        entity.playSound(SoundEvents.LEASH_KNOT_BREAK);
                    }

                    return InteractionResult.SUCCESS;
                }

                if (stack.is(Items.LEAD) && !(leashable.getLeashHolder() instanceof Player)) {
                    if (!entity.level().isClientSide() && ((LeashExtension)leashable).vb$canHaveALeashAttachedTo(player)) {
                        if (leashable.isLeashed()) {
                            leashable.dropLeash(true, true);
                        }

                        leashable.setLeashedTo(player, true);
                        entity.playSound(SoundEvents.LEASH_KNOT_PLACE);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                    }

                    return InteractionResult.SUCCESS;
                }
            }

            return InteractionResult.PASS;
        }
    }

    private static boolean shearOffAllLeashConnections(Entity entity, Player player) {
        boolean sheared = dropAllLeashConnections(entity, player);
        if (sheared) {
            Level var5 = entity.level();
            if (var5 instanceof ServerLevel server) {
                server.playSound(null, entity.blockPosition(), SoundEvents.SHEEP_SHEAR, player != null ? player.getSoundSource() : entity.getSoundSource());
            }
        }

        return sheared;
    }

    public static boolean dropAllLeashConnections(Entity entity, @Nullable Player player) {
        List<Leashable> leashed = LeashExtension.vb$leashableLeashedTo(entity);
        boolean dropConnections = !leashed.isEmpty();
        if (entity instanceof Leashable leashable) {
            if (leashable.isLeashed()) {
                leashable.dropLeash(true, true);
                dropConnections = true;
            }
        }

        for(Leashable leashable : leashed) {
            leashable.dropLeash(true, true);
        }

        if (dropConnections) {
            entity.gameEvent(GameEvent.SHEAR, player);
            return true;
        } else {
            return false;
        }
    }
}
