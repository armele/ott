package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BewitchedGreywolfEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BewitchedGreywolfEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            Player player = this.level().getNearestPlayer(this, 6.0D);
            if (player != null && this.distanceToSqr(player) < 36.0D) {
                this.transformToSkinwalker();
            }
        }
    }

    private void transformToSkinwalker() {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 20, 0.5, 0.5, 0.5, 0.05);
            serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, this.getSoundSource(), 1.0F, 1.0F);
            SkinwalkerEntity skinwalker = ModEntities.SKINWALKER.get().create(this.level());
            if (skinwalker != null) {
                skinwalker.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                serverLevel.addFreshEntity(skinwalker);
                this.discard();
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event -> {
            if (event.isMoving()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.timber_wolf.walk"));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.timber_wolf.idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}