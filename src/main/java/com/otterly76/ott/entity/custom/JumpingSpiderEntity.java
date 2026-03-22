package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.core.OttGeoEntity;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class JumpingSpiderEntity extends TamableAnimal implements OttGeoEntity {

    private static final TagKey<Item> FOODS_TAG = ModTags.ItemTags.JUMPING_SPIDER_FOOD;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private PanicGoal panicGoal;

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("jumping_spider_idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("jumping_spider_walk");
    private static final RawAnimation SIT = RawAnimation.begin().thenLoop("jumping_spider_sit");

    public JumpingSpiderEntity(EntityType<? extends JumpingSpiderEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new JumpingSpiderMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Spider.createAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.ATTACK_DAMAGE, 8.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.panicGoal = new PanicGoal(this, 1.0D);
        this.goalSelector.addGoal(1, this.panicGoal);

        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.0D, Ingredient.of(FOODS_TAG), false));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, Endermite.class, false, (LivingEntity::isAlive)));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, Silverfish.class, false, (LivingEntity::isAlive)));
        this.targetSelector.addGoal(2, new NonTameRandomTargetGoal<>(this, DragonflyEntity.class, false, (LivingEntity::isAlive)));
    }

    @Override
    protected void playStepSound(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.1F, 2.0F);
    }

    @NotNull
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    @NotNull
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.SPIDER_HURT;
    }

    @NotNull
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    @Override
    public void makeStuckInBlock(@NotNull BlockState blockState, @NotNull Vec3 p_33797_) {
        if (!blockState.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(blockState, p_33797_);
        }
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return null;
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand interactionHand) {
        ItemStack handStack = player.getItemInHand(interactionHand);

        if (!this.isTame() && handStack.is(FOODS_TAG)) {
            if (!player.getAbilities().instabuild) {
                handStack.shrink(1);
            }
            if (!this.level().isClientSide()) {
                if (this.random.nextInt(10) == 0) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        } else if (this.isTame() && this.isOwnedBy(player)) {
            if (!this.level().isClientSide()) {
                if (handStack.is(FOODS_TAG) && this.getHealth() < this.getMaxHealth()) {
                    this.gameEvent(GameEvent.EAT, this);
                    this.heal(1.0F);
                    if (!player.getAbilities().instabuild) {
                        handStack.shrink(1);
                    }
                } else {
                    this.setOrderedToSit(!this.isOrderedToSit());
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        } else {
            return super.mobInteract(player, interactionHand);
        }
    }

    @Override
    public void tame(@NotNull Player player) {
        super.tame(player);
        this.goalSelector.removeGoal(this.panicGoal);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    protected <T extends JumpingSpiderEntity> PlayState predicate(software.bernie.geckolib.animation.AnimationState<T> event) {
        if (this.isInSittingPose()) {
            event.getController().setAnimation(SIT);
        } else if (event.isMoving()) {
            event.getController().setAnimation(WALK);
        } else {
            event.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    static class JumpingSpiderMoveControl extends MoveControl {
        private final JumpingSpiderEntity spider;

        public JumpingSpiderMoveControl(JumpingSpiderEntity jumpingSpider) {
            super(jumpingSpider);
            this.spider = jumpingSpider;
        }

        @Override
        public void tick() {
            if (this.hasWanted() && this.spider.onGround() && this.spider.getRandom().nextFloat() <= 0.05F) {
                this.spider.setDeltaMovement(this.spider.getDeltaMovement().add(0.0D, 0.6D, 0.0D));
            }
            super.tick();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canBeAffected(MobEffectInstance effect) {
        return !effect.getEffect().equals(MobEffects.POISON) && super.canBeAffected(effect);
    }

    @Override
    public boolean wantsToAttack(@NotNull LivingEntity target, @NotNull LivingEntity owner) {
        if (target instanceof TamableAnimal tamable) {
            return !tamable.isTame() || tamable.getOwner() != owner;
        }

        return super.wantsToAttack(target, owner);
    }
}