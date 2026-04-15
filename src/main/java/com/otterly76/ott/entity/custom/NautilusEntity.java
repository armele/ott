package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Backport of the 1.21.11 Nautilus entity (Mounts of Mayhem).
 * <p>
 * Tameable aquatic mount — feeds on pufferfish/fish buckets (nautilus_taming_items),
 * attacks hostile creatures, and gives the rider Water Breathing.
 * Uses goal-based AI as a 1.21.1 substitute for the 1.21.11 NautilusAi brain system.
 */
public class NautilusEntity extends AbstractNautilusEntity {

    private static final int NAUTILUS_TOTAL_AIR_SUPPLY = 300;

    public NautilusEntity(EntityType<? extends NautilusEntity> type, Level level) {
        super(type, level);
    }

    // ── Goal AI ───────────────────────────────────────────────────────────────

    @Override
    protected void registerGoals() {
        // Taming / feeding tempt
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.1D, stack -> stack.is(NAUTILUS_TAMING_ITEMS) || stack.is(NAUTILUS_FOOD), false));
        // Attack pufferfish and anything that hurt it
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
        // TODO: WaterBoundPathNavigation is not supported by vanilla FollowOwnerGoal (only Ground/Flying).
        //       Replace with a custom aquatic follow-owner goal.
        // Random swimming
        this.goalSelector.addGoal(6, new RandomSwimmingGoal(this, 1.0D, 40));
        // Look at player
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        // Target: retaliate if hurt
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        // Hunt pufferfish
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Pufferfish.class, true));
    }

    // ── Breeding ──────────────────────────────────────────────────────────────

    @Override
    public @Nullable NautilusEntity getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob partner) {
        NautilusEntity baby = ModEntities.NAUTILUS.get().create(level);
        if (baby != null && this.isTame()) {
            baby.setTame(true, true);
            baby.setOwnerUUID(this.getOwnerUUID());
        }
        return baby;
    }

    // ── Sounds ────────────────────────────────────────────────────────────────

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return this.isUnderWater()
                ? (this.isBaby() ? ModSounds.BABY_NAUTILUS_AMBIENT.get()  : ModSounds.NAUTILUS_AMBIENT.get())
                : (this.isBaby() ? ModSounds.BABY_NAUTILUS_AMBIENT_ON_LAND.get() : ModSounds.NAUTILUS_AMBIENT_ON_LAND.get());
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return this.isUnderWater()
                ? (this.isBaby() ? ModSounds.BABY_NAUTILUS_HURT.get()  : ModSounds.NAUTILUS_HURT.get())
                : (this.isBaby() ? ModSounds.BABY_NAUTILUS_HURT_ON_LAND.get() : ModSounds.NAUTILUS_HURT_ON_LAND.get());
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return this.isUnderWater()
                ? (this.isBaby() ? ModSounds.BABY_NAUTILUS_DEATH.get()  : ModSounds.NAUTILUS_DEATH.get())
                : (this.isBaby() ? ModSounds.BABY_NAUTILUS_DEATH_ON_LAND.get() : ModSounds.NAUTILUS_DEATH_ON_LAND.get());
    }

    @Override
    @Nullable
    protected SoundEvent getDashSound() {
        return this.isUnderWater() ? ModSounds.NAUTILUS_DASH.get() : ModSounds.NAUTILUS_DASH_ON_LAND.get();
    }

    @Override
    @Nullable
    protected SoundEvent getDashReadySound() {
        return this.isUnderWater() ? ModSounds.NAUTILUS_DASH_READY.get() : ModSounds.NAUTILUS_DASH_READY_ON_LAND.get();
    }

    @Override
    protected void playEatingSound() {
        this.makeSound(this.isBaby() ? ModSounds.BABY_NAUTILUS_EAT.get() : ModSounds.NAUTILUS_EAT.get());
    }

    @Override
    protected @NotNull SoundEvent getSwimSound() {
        return this.isBaby() ? ModSounds.BABY_NAUTILUS_SWIM.get() : ModSounds.NAUTILUS_SWIM.get();
    }

    // ── Air supply (dies if out of water too long) ────────────────────────────

    @Override
    public int getMaxAirSupply() {
        return NAUTILUS_TOTAL_AIR_SUPPLY;
    }

    @Override
    public void baseTick() {
        int airSupply = this.getAirSupply();
        super.baseTick();
        if (this.isAlive() && !this.isInWater()) {
            this.setAirSupply(airSupply - 1);
            if (this.getAirSupply() <= -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().dryOut(), 2.0F);
            }
        } else {
            this.setAirSupply(NAUTILUS_TOTAL_AIR_SUPPLY);
        }
    }

    // ── Spawn ─────────────────────────────────────────────────────────────────


    // ── Leash ─────────────────────────────────────────────────────────────────

    @Override
    public boolean canBeLeashed() {
        // Cannot be leashed while aggravated (has attack target)
        return this.getTarget() == null;
    }
}