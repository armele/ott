package com.otterly76.ott.mixin.common;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerMixin extends net.minecraft.world.entity.AgeableMob {

    protected VillagerMixin(EntityType<? extends net.minecraft.world.entity.AgeableMob> p_27350_, Level p_27351_) {
        super(p_27350_, p_27351_);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/npc/VillagerType;)V", at = @At("TAIL"))
    private void ott$addTemptGoal(EntityType<? extends Villager> type, Level level, VillagerType villagerType, CallbackInfo ci) {
        if (OttConfig.GENERAL.VILLAGERS_FOLLOW_EMERALD.get()) {
            this.goalSelector.addGoal(2, new TemptGoal(this, 0.6D, Ingredient.of(Items.EMERALD), false));
        }
    }
}
