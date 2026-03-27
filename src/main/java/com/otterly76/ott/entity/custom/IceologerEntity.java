package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class IceologerEntity extends AbstractIllager {

    private int spellCooldown = 0;

    public IceologerEntity(EntityType<? extends AbstractIllager> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.FOLLOW_RANGE, 18.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.isAlive() && this.getTarget() != null) {
            if (spellCooldown > 0) {
                spellCooldown--;
            } else {
                summonIceChunk();
                spellCooldown = 100;
            }
        }
    }

    private void summonIceChunk() {
        LivingEntity target = this.getTarget();
        if (target == null) return;
        this.playSound(ModSounds.ICEOLOGER_PREPARE_SUMMON.get(), 1.0F, 1.0F);
        IceologerIceChunkEntity chunk = ModEntities.ICE_CHUNK.get().create(this.level());
        if (chunk != null) {
            chunk.moveTo(this.getX(), this.getY() + 4.0, this.getZ(), 0, 0);
            chunk.setOwner(this.getUUID());
            chunk.setTargetUUID(target.getUUID());
            this.level().addFreshEntity(chunk);
        }
    }

    @Override
    public @NotNull IllagerArmPose getArmPose() {
        return IllagerArmPose.NEUTRAL;
    }

    @Override
    public void applyRaidBuffs(@NotNull ServerLevel level, int wave, boolean unused) {}

    @Override
    public @NotNull SoundEvent getCelebrateSound() { return ModSounds.ICEOLOGER_AMBIENT.get(); }

    @Override
    protected @NotNull SoundEvent getAmbientSound() { return ModSounds.ICEOLOGER_AMBIENT.get(); }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) { return ModSounds.ICEOLOGER_HURT.get(); }

    @Override
    protected @NotNull SoundEvent getDeathSound() { return ModSounds.ICEOLOGER_DEATH.get(); }
}