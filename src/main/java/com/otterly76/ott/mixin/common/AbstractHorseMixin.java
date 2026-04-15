package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.ai.goal.SpearUseGoal;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * AbstractHorse-level injects for Mounts of Mayhem backport.
 * Methods that ZombieHorse inherits but doesn't override must be injected here
 * (Mixin without refMap can only target methods declared in the target class).
 */
@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin {

    private static final TagKey<Item> OTT$ZOMBIE_HORSE_FOOD =
            TagKey.create(Registries.ITEM, ResourceLocation.parse("minecraft:zombie_horse_food"));

    // ── isFood: zombie_horse_food tag ─────────────────────────────────────────

    @Inject(method = "isFood(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"), cancellable = true)
    private void ott$isFood(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ZombieHorse && itemStack.is(OTT$ZOMBIE_HORSE_FOOD)) {
            cir.setReturnValue(true);
        }
    }

    // ── finalizeSpawn: zombie jockey for ZombieHorse natural spawns ───────────

    @Inject(
        method = "finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;",
        at = @At("RETURN")
    )
    private void ott$finalizeSpawn(
            ServerLevelAccessor levelAccessor,
            DifficultyInstance difficulty,
            MobSpawnType reason,
            SpawnGroupData spawnData,
            CallbackInfoReturnable<SpawnGroupData> cir) {

        if (!((Object) this instanceof ZombieHorse self)) return;
        if (reason != MobSpawnType.NATURAL) return;
        if (!(levelAccessor instanceof ServerLevel serverLevel)) return;

        Zombie zombie = EntityType.ZOMBIE.create(serverLevel);
        if (zombie == null) return;

        zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR.get()));
        zombie.goalSelector.addGoal(1, new SpearUseGoal<>(zombie));
        zombie.finalizeSpawn(levelAccessor, difficulty, MobSpawnType.JOCKEY, null);
        zombie.moveTo(self.getX(), self.getY(), self.getZ(), self.getYRot(), 0.0F);
        serverLevel.addFreshEntity(zombie);
        zombie.startRiding(self, true);
    }

    // ── aiStep: ZombieHorse burns in sunlight unless wearing body armor ────────

    @Inject(method = "aiStep()V", at = @At("HEAD"))
    private void ott$aiStep(org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        AbstractHorse self = (AbstractHorse) (Object) this;
        if (!(self instanceof ZombieHorse)) return;
        if (!self.isAlive() || self.level().isClientSide || !self.level().isDay()) return;
        if (!self.level().canSeeSky(self.blockPosition()) || self.isInWaterRainOrBubble()) return;
        if (self.getItemBySlot(EquipmentSlot.BODY).isEmpty()) {
            self.igniteForSeconds(8.0F);
        }
    }
}
