package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Starfish1Entity extends Animal implements GeoEntity {
    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("animation.starfish.walk");
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.starfish.idle");
    private static final RawAnimation EAT_ANIMATION = RawAnimation.begin().thenLoop("animation.starfish.eat");
    private static final RawAnimation JUMP_ANIMATION = RawAnimation.begin().thenLoop("animation.starfish.jump");

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Starfish1Entity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public boolean canDrownInFluidType(@NotNull net.neoforged.neoforge.fluids.FluidType type) {
        return false;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader world) {
        return world.isUnobstructed(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.2d).add(Attributes.MAX_HEALTH, 10.0d).add(Attributes.ATTACK_DAMAGE, 1.0d);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, ShrimpEntity.class, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(ModItems.RAW_SHRIMP.get()), false));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new HurtByTargetGoal(this).setAlertOthers(Starfish1Entity.class));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 10.0F));
    }

    @Override
    public void rideTick() {
        Entity entity = this.getVehicle();
        if (entity != null) {
            if (entity instanceof Player player) {
                if (player.isShiftKeyDown()) {
                    this.stopRiding();
                }
            }

            if (!entity.isAlive() || (!(entity instanceof ShrimpEntity) && !(entity instanceof Player))) {
                this.stopRiding();
            } else {
                this.setDeltaMovement(0, 0, 0);
                this.tick();
                if (entity instanceof Player player) {
                    this.setPos(player.getX(), Math.max(player.getY() + player.getEyeHeight(), player.getY()), player.getZ());
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
                    if (player.getHealth() > 1.0f) {
                        player.hurt(this.damageSources().mobAttack(this), 1.0f);
                    }
                    if (!player.isAlive()) {
                        this.removeVehicle();
                    }
                } else if (entity instanceof ShrimpEntity shrimp) {
                    this.setPos(shrimp.getX(), Math.max(shrimp.getY() + shrimp.getEyeHeight(), shrimp.getY()), shrimp.getZ());
                    shrimp.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
                    shrimp.hurt(this.damageSources().mobAttack(this), 1.0f);
                    if (!shrimp.isAlive()) {
                        this.removeVehicle();
                    }
                }
            }
        } else {
            super.rideTick();
        }
    }

    public static boolean canSpawn(EntityType<Starfish1Entity> type, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return pos.getY() >= 45 && pos.getY() <= 64 && world.getBlockState(pos).is(Blocks.WATER);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(ModItems.RAW_SHRIMP.get())) {
            if (!this.level().isClientSide && this.canFallInLove()) {
                this.usePlayerItem(player, hand, itemstack);
                this.setInLove(player);
                this.gameEvent(GameEvent.ENTITY_INTERACT, this);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(ModItems.RAW_SHRIMP.get());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel world, @NotNull AgeableMob partner) {
        return ModEntities.STARFISH_1.get().create(world);
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    private PlayState predicate(AnimationState<Starfish1Entity> event) {
        if (this.isPassenger()) {
            return event.setAndContinue(EAT_ANIMATION);
        } else if (event.isMoving()) {
            return event.setAndContinue(WALK_ANIMATION);
        } else if (this.isSwimming()) {
            return event.setAndContinue(JUMP_ANIMATION);
        } else {
            return event.setAndContinue(IDLE_ANIMATION);
        }
    }
}
