package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.custom.HappyGhast;
import com.otterly76.ott.util.entity.LeashDataExtension;
import com.otterly76.ott.util.entity.LeashExtension;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Leashable.class})
public interface LeashableMixin extends LeashExtension {
    @Shadow
    private static <E extends Entity & Leashable> void restoreLeashFromSave(E entity, Leashable.LeashData leashData) {
    }

    @Invoker("restoreLeashFromSave")
    static <E extends Entity & Leashable> void callRestoreLeashFromSave(Entity entity, Leashable.LeashData leashData) {
        throw new UnsupportedOperationException();
    }

    @Invoker("dropLeash")
    static void callDropLeash(Entity entity, boolean broadcast, boolean drop) {
        throw new UnsupportedOperationException();
    }

    @Inject(
        method = {"tickLeash(Lnet/minecraft/world/entity/Entity;)V"},
        at = {@At("HEAD")},
        cancellable = true
    )
    private static <E extends Entity & Leashable> void vb$onTickLeash(E entity, CallbackInfo ci) {
        Leashable.LeashData data = entity.getLeashData();
        if (data != null && data.delayedLeashInfo != null) {
            restoreLeashFromSave(entity, data);
        }

        if (data != null && data.leashHolder != null) {
            if (!entity.isAlive() || !data.leashHolder.isAlive()) {
                entity.dropLeash(true, entity.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS));
            }

            Entity holder = entity.getLeashHolder();
            LeashExtension leashed = (LeashExtension)entity;
            if (holder != null && holder.level() == entity.level()) {
                double distance = leashed.vb$leashDistanceTo(holder);
                leashed.vb$whenLeashedTo(holder);
                
                if (entity instanceof HappyGhast) {
                    if (!entity.handleLeashAtDistance(holder, (float)distance)) {
                        ci.cancel();
                        return;
                    }
                }

                if (distance > leashed.vb$leashSnapDistance()) {
                    entity.level().playSound(null, holder.blockPosition(), SoundEvents.LEASH_KNOT_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    entity.leashTooFarBehaviour();
                } else if (distance > leashed.vb$leashElasticDistance() - (double)holder.getBbWidth() - (double)entity.getBbWidth() && leashed.vb$checkElasticInteractions(holder, data)) {
                    leashed.vb$onElasticLeashPull();
                    if (entity instanceof HappyGhast) {
                        entity.checkSlowFallDistance();
                    }
                } else {
                    entity.closeRangeLeashBehaviour(holder);
                }

                LeashDataExtension leashData = (LeashDataExtension)(Object)data;
                entity.setYRot((float)((double)entity.getYRot() - leashData.angularMomentum()));
                leashData.setAngularMomentum(leashData.angularMomentum() * (double)LeashExtension.vb$angularFriction(entity));
            }
        }

        ci.cancel();
    }
}
