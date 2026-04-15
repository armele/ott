package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.goal.SpearUseGoal;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CamelHuskEntity extends Camel {

    private static final TagKey<Item> CAMEL_HUSK_FOOD = TagKey.create(
            Registries.ITEM, ResourceLocation.parse("minecraft:camel_husk_food")
    );

    public CamelHuskEntity(EntityType<? extends Camel> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return true;
    }

    public boolean isMobControlled() {
        return this.getFirstPassenger() instanceof Mob;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        this.setPersistenceRequired();
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean canBeLeashed() {
        return !this.isMobControlled();
    }

    @Override
    public boolean isFood(@NotNull ItemStack itemStack) {
        return itemStack.is(CAMEL_HUSK_FOOD);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level,
                                        @NotNull DifficultyInstance difficulty,
                                        @NotNull MobSpawnType reason,
                                        @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        if (reason == MobSpawnType.NATURAL && level instanceof ServerLevel serverLevel) {
            // Spawn husk (first seat) with iron spear + SpearUseGoal
            Husk husk = EntityType.HUSK.create(serverLevel);
            if (husk != null) {
                husk.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR.get()));
                husk.goalSelector.addGoal(1, new SpearUseGoal<>(husk));
                husk.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null);
                husk.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                serverLevel.addFreshEntity(husk);
                husk.startRiding(this, true);
            }
            // Spawn parched (second seat)
            ParchedEntity parched = ModEntities.PARCHED.get().create(serverLevel);
            if (parched != null) {
                parched.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null);
                parched.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                serverLevel.addFreshEntity(parched);
                parched.startRiding(this, true);
            }
        }
        return data;
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return ModSounds.CAMEL_HUSK_AMBIENT.get();
    }

    @Override
    public boolean canMate(@NotNull Animal partner) {
        return false;
    }

    @Override
    @Nullable
    public Camel getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob partner) {
        return null;
    }

    @Override
    public boolean canFallInLove() {
        return false;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSounds.CAMEL_HUSK_DEATH.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSounds.CAMEL_HUSK_HURT.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        if (blockState.is(BlockTags.CAMEL_SAND_STEP_SOUND_BLOCKS)) {
            this.playSound(ModSounds.CAMEL_HUSK_STEP_SAND.get(), 0.4F, 1.0F);
        } else {
            this.playSound(ModSounds.CAMEL_HUSK_STEP.get(), 0.4F, 1.0F);
        }
    }

    @Override
    protected SoundEvent getEatingSound() {
        return ModSounds.CAMEL_HUSK_EAT.get();
    }

    @Override
    public @NotNull SoundEvent getSaddleSoundEvent() {
        return ModSounds.CAMEL_HUSK_SADDLE.get();
    }
}