package com.otterly76.ott.entity.custom;

import org.jetbrains.annotations.NotNull;

import com.otterly76.ott.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CougarEntity extends TamableAnimal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public CougarEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true, (entity) -> !this.isTame()));
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.level().isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || itemstack.is(Items.BONE) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (this.isTame()) {
                if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    net.minecraft.world.food.FoodProperties foodProperties = itemstack.getFoodProperties(this);
                    if (foodProperties != null) {
                        this.heal((float) foodProperties.nutrition());
                    }
                    return InteractionResult.SUCCESS;
                }
                InteractionResult interactionresult = super.mobInteract(player, hand);
                if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(player)) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    this.jumping = false;
                    this.navigation.stop();
                    this.setTarget(null);
                    return InteractionResult.SUCCESS;
                }
                return interactionresult;
            } else if (itemstack.is(Items.BONE)) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                if (this.random.nextInt(3) == 0) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setOrderedToSit(true);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
                return InteractionResult.SUCCESS;
            }
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(Items.BEEF) || stack.is(Items.PORKCHOP);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob other) {
        return com.otterly76.ott.entity.ModEntities.COUGAR.get().create(level);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_COUGAR_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.ENTITY_COUGAR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ENTITY_COUGAR_DEATH.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event -> {
            if (this.isOrderedToSit()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.cougar.sit"));
            }
            if (event.isMoving()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.cougar.walk"));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.cougar.idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}