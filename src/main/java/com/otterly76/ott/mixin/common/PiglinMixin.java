package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.ai.goal.SpearUseGoal;
import com.otterly76.ott.item.ModItems;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Piglin.class)
public abstract class PiglinMixin extends MobMixin {

    protected PiglinMixin(EntityType<? extends Piglin> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void ott$finalizeSubSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (this.getType() == EntityType.PIGLIN
                && reason == MobSpawnType.NATURAL
                && this.getRandom().nextFloat() < 0.2F) {
            Piglin piglin = (Piglin)(Object)this;
            piglin.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLDEN_SPEAR.get()));
            piglin.goalSelector.addGoal(1, new SpearUseGoal<>(piglin));
        }
    }
}
