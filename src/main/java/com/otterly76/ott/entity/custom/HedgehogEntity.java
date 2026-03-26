package com.otterly76.ott.entity.custom;

import com.mojang.serialization.Dynamic;
import com.otterly76.ott.OttDamageTypes;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.registry.ModEntityDataSerializers;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.function.IntFunction;

public class HedgehogEntity extends Animal implements GeoEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.hedgehog.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.hedgehog.walk");
    private static final RawAnimation ROLL = RawAnimation.begin().then("animation.hedgehog.roll", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation UNROLL = RawAnimation.begin().then("animation.hedgehog.unroll", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SCARED = RawAnimation.begin().thenLoop("animation.hedgehog.scared");

    private static final EntityDataAccessor<HedgehogState> STATE = SynchedEntityData.defineId(HedgehogEntity.class, ModEntityDataSerializers.HEDGEHOG_STATE.get());
    private static final EntityDataAccessor<ItemStack> STACK = SynchedEntityData.defineId(HedgehogEntity.class, EntityDataSerializers.ITEM_STACK);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public long inStateTicks = 0L;

    public HedgehogEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.getNavigation().setCanFloat(true);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STATE, HedgehogState.IDLE);
        builder.define(STACK, ItemStack.EMPTY);
    }

    public HedgehogState getState() {
        return this.getEntityData().get(STATE);
    }

    public void setState(HedgehogState state) {
        this.getEntityData().set(STATE, state);
        this.inStateTicks = 0L;
    }

    public ItemStack getStack() {
        return this.getEntityData().get(STACK);
    }

    public void setStack(ItemStack stack) {
        this.getEntityData().set(STACK, stack);
    }

    public boolean isScared() {
        return this.getState() != HedgehogState.IDLE;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("State", this.getState().getSerializedName());
        if (!this.getStack().isEmpty()) {
            compound.put("Item", this.getStack().saveOptional(this.registryAccess()));
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setState(HedgehogState.fromName(compound.getString("State")));
        if (compound.contains("Item", 10)) {
            this.setStack(ItemStack.parseOptional(this.registryAccess(), compound.getCompound("Item")));
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        Level level = this.level();
        ItemStack back = this.getStack();
        ItemStack held = player.getItemInHand(hand);

        if (!back.isEmpty()) {
            if (!level.isClientSide()) {
                ItemStack toGive = back.copy();
                this.setStack(ItemStack.EMPTY);
                if (!player.addItem(toGive)) {
                    this.spawnAtLocation(toGive);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (!held.isEmpty()) {
            if (this.isFood(held)) {
                InteractionResult result = super.mobInteract(player, hand);
                if (result.consumesAction()) {
                    return result;
                }
            }

            if (!level.isClientSide()) {
                this.setStack(held.copy());
                if (!player.getAbilities().instabuild) {
                    held.shrink(held.getCount());
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) return;

        ++this.inStateTicks;

        if (!this.isBaby()) {
            var targets = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox(), (candidate) -> !(candidate instanceof HedgehogEntity));
            boolean damagedSomeone = false;
            for (var target : targets) {
                boolean damaged = target.hurt(OttDamageTypes.of(this.level(), OttDamageTypes.HEDGEHOG_SPIKES, this), this.getState() == HedgehogState.SCARED ? 5.0F : 1.0F);
                damagedSomeone = damagedSomeone || damaged;
            }
            if (damagedSomeone) {
                this.getBrain().setMemoryWithExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY, true, 80L);
            }
        }

        if (this.getState() == HedgehogState.UNROLLING && this.inStateTicks == (long) HedgehogState.UNROLLING.getAnimationDuration() - 10) {
            this.playSound(ModSounds.HEDGEHOG_UNROLL.get(), 1.0F, 1.0F);
        }
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("hedgehogBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();

        this.level().getProfiler().push("hedgehogActivityUpdate");
        HedgehogAI.updateActivity(this);
        this.level().getProfiler().pop();

        super.customServerAiStep();
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide()) {
            var attacker = source.getEntity();
            if (attacker instanceof LivingEntity entity && entity.getMainHandItem().isEmpty()) {
                entity.hurt(OttDamageTypes.of(this.level(), OttDamageTypes.HEDGEHOG_SPIKES, this), 1.0F);
            }
        }
        return result;
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new BodyRotationControl(this) {
            @Override
            public void clientTick() {
                if (HedgehogEntity.this.isScared()) return;
                super.clientTick();
            }
        };
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull ServerLevel level, @NotNull DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        ItemStack stack = this.getStack();
        if (!stack.isEmpty()) {
            this.spawnAtLocation(stack);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.HEDGEHOG_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSounds.HEDGEHOG_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.HEDGEHOG_DEATH.get();
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(ModTags.ItemTags.HEDGEHOG_FOOD);
    }

    @Override
    protected @NotNull Brain.Provider<HedgehogEntity> brainProvider() {
        return HedgehogAI.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return HedgehogAI.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<HedgehogEntity> getBrain() {
        return (Brain<HedgehogEntity>) super.getBrain();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob partner) {
        HedgehogEntity baby = ModEntities.HEDGEHOG.get().create(level);
        if (baby != null) {
            baby.setBaby(true);
        }
        return baby;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 0, this::mainPredicate));
    }

    private @NotNull PlayState mainPredicate(AnimationState<HedgehogEntity> state) {
        var controller = state.getController();
        HedgehogState hedgehogState = this.getState();

        if (hedgehogState != HedgehogState.IDLE) {
            switch (hedgehogState) {
                case ROLLING -> controller.setAnimation(ROLL);
                case SCARED -> controller.setAnimation(SCARED);
                case UNROLLING -> controller.setAnimation(UNROLL);
            }
        } else if (state.isMoving()) {
            controller.setAnimation(WALK);
        } else {
            controller.setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 8.0D);
    }

    public boolean canStayRolledUp() {
        return !this.isPanicking() && !this.isInLiquid() && !this.isLeashed() && !this.isPassenger() && !this.isVehicle();
    }

    public boolean isScaredBy(LivingEntity entity) {
        if (!this.getBoundingBox().inflate(5.0D, 2.0D, 5.0D).intersects(entity.getBoundingBox())) {
            return false;
        } else if (entity.getType().is(EntityTypeTags.UNDEAD)) {
            return true;
        } else if (this.getLastHurtByMob() == entity) {
            return true;
        } else if (entity instanceof Player player) {
            return !player.isSpectator() && (player.isSprinting() || player.isPassenger());
        } else {
            return false;
        }
    }

    public void rollUp() {
        if (this.isScared()) return;
        this.stopInPlace();
        this.resetLove();
        this.gameEvent(GameEvent.ENTITY_ACTION);
        this.makeSound(ModSounds.HEDGEHOG_ROLL.get());
        this.setState(HedgehogState.ROLLING);
    }

    public void rollOut() {
        if (!this.isScared()) return;
        this.gameEvent(GameEvent.ENTITY_ACTION);
        this.setState(HedgehogState.IDLE);
    }

    public enum HedgehogState implements StringRepresentable {
        IDLE("idle", false, 0, 0),
        ROLLING("rolling", true, 23, 1),
        SCARED("scared", true, 50, 2),
        UNROLLING("unrolling", true, 23, 3);

        private static final IntFunction<HedgehogState> BY_ID = ByIdMap.continuous(HedgehogState::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        @SuppressWarnings("deprecation")
        private static final StringRepresentable.EnumCodec<HedgehogState> CODEC = StringRepresentable.fromEnum(HedgehogState::values);
        public static final StreamCodec<ByteBuf, HedgehogState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, HedgehogState::getId);

        private final String name;
        private final boolean isThreatened;
        private final int animationDuration;
        private final int id;

        HedgehogState(String name, boolean isThreatened, int animationDuration, int id) {
            this.name = name;
            this.isThreatened = isThreatened;
            this.animationDuration = animationDuration;
            this.id = id;
        }

        public String getName() { return name; }
        public boolean isThreatened() { return isThreatened; }
        public int getAnimationDuration() { return animationDuration; }
        public int getId() { return id; }

        public static HedgehogState fromName(String name) {
            return CODEC.byName(name, IDLE);
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}

