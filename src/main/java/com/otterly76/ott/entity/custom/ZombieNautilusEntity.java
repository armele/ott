package com.otterly76.ott.entity.custom;

import com.otterly76.ott.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Backport of the 1.21.11 ZombieNautilus entity (Mounts of Mayhem).
 * <p>
 * Hostile aquatic mob — attacks players, can be tempted with pufferfish but not tamed.
 * Variant system (temperate/warm) is skipped for 1.21.1 compatibility.
 * Uses goal-based AI as a substitute for the 1.21.11 ZombieNautilusAi brain system.
 */
public class ZombieNautilusEntity extends AbstractNautilusEntity {

    public ZombieNautilusEntity(EntityType<? extends ZombieNautilusEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractNautilusEntity.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 1.1);
    }

    // ── Goal AI ───────────────────────────────────────────────────────────────

    @Override
    protected void registerGoals() {
        // Temptation reduces aggression; does not lead to taming
        this.goalSelector.addGoal(1, new TemptGoal(this, 0.9D, stack -> stack.is(NAUTILUS_TAMING_ITEMS), false));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.5D, true));
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0D, 40));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // ── Breeding / babies ─────────────────────────────────────────────────────

    @Override
    public @Nullable ZombieNautilusEntity getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob partner) {
        return null;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    // ── Food / feeding ────────────────────────────────────────────────────────

    @Override
    public boolean isFood(@NotNull ItemStack itemStack) {
        // ZombieNautilus cannot be fed (no breeding, no healing via isFood path)
        return false;
    }

    // ── Leash / interaction ───────────────────────────────────────────────────

    @Override
    public boolean canBeLeashed() {
        return this.getTarget() == null && !this.isMobControlled();
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        // ZombieNautilus cannot be tamed or ridden — skip AbstractNautilusEntity interaction logic.
        // Still allow name tag / lead via item interaction.
        ItemStack itemStack = player.getItemInHand(hand);
        if (!itemStack.isEmpty()) {
            InteractionResult result = itemStack.interactLivingEntity(player, this, hand);
            if (result.consumesAction()) return result;
        }
        return InteractionResult.PASS;
    }

    // ── Sunburn (nautilus armor in BODY slot prevents burn) ───────────────────

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.isSunBurnTick()) {
            ItemStack bodyArmor = this.getItemBySlot(EquipmentSlot.BODY);
            if (bodyArmor.isEmpty()) {
                this.igniteForSeconds(8.0F);
            }
        }
    }

    // ── Spawn ─────────────────────────────────────────────────────────────────

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level,
                                                   @NotNull DifficultyInstance difficulty,
                                                   @NotNull MobSpawnType reason,
                                                   @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        if (reason == MobSpawnType.NATURAL && level instanceof ServerLevel serverLevel) {
            Drowned drowned = EntityType.DROWNED.create(serverLevel);
            if (drowned != null) {
                drowned.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
                drowned.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null);
                drowned.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                serverLevel.addFreshEntity(drowned);
                drowned.startRiding(this, true);
            }
        }
        return data;
    }

    // ── Sounds ────────────────────────────────────────────────────────────────

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return this.isUnderWater()
                ? ModSounds.ZOMBIE_NAUTILUS_AMBIENT.get()
                : ModSounds.ZOMBIE_NAUTILUS_AMBIENT_ON_LAND.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return this.isUnderWater()
                ? ModSounds.ZOMBIE_NAUTILUS_HURT.get()
                : ModSounds.ZOMBIE_NAUTILUS_HURT_ON_LAND.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return this.isUnderWater()
                ? ModSounds.ZOMBIE_NAUTILUS_DEATH.get()
                : ModSounds.ZOMBIE_NAUTILUS_DEATH_ON_LAND.get();
    }

    @Override
    @Nullable
    protected SoundEvent getDashSound() {
        return this.isUnderWater()
                ? ModSounds.ZOMBIE_NAUTILUS_DASH.get()
                : ModSounds.ZOMBIE_NAUTILUS_DASH_ON_LAND.get();
    }

    @Override
    @Nullable
    protected SoundEvent getDashReadySound() {
        return this.isUnderWater()
                ? ModSounds.ZOMBIE_NAUTILUS_DASH_READY.get()
                : ModSounds.ZOMBIE_NAUTILUS_DASH_READY_ON_LAND.get();
    }

    @Override
    protected void playEatingSound() {
        this.makeSound(ModSounds.ZOMBIE_NAUTILUS_EAT.get());
    }

    @Override
    protected @NotNull SoundEvent getSwimSound() {
        return ModSounds.ZOMBIE_NAUTILUS_SWIM.get();
    }
}