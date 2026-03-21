package com.otterly76.ott.entity.custom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.registry.ModSensorTypes;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class SealAI {
    private static final ImmutableList<SensorType<? extends Sensor<? super SealEntity>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.HURT_BY,
            ModSensorTypes.SEAL_TEMPTATIONS.get(),
            SensorType.NEAREST_ADULT
    );

    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.IS_PANICKING,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.TEMPTING_PLAYER,
            MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
            MemoryModuleType.GAZE_COOLDOWN_TICKS,
            MemoryModuleType.IS_TEMPTED,
            MemoryModuleType.BREED_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_ADULT,
            MemoryModuleType.DANGER_DETECTED_RECENTLY
    );

    public static Brain.Provider<SealEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<SealEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initPanicActivity(brain);

        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();

        return brain;
    }

    private static void initCoreActivity(Brain<SealEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                        new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS),
                        new LayCooldownBehavior()
                )
        );
    }

    @SuppressWarnings("deprecation")
    private static void initIdleActivity(Brain<SealEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
                        Pair.of(1, new AnimalMakeLove(ModEntities.SEAL.get(), 1.0F, 1)),
                        Pair.of(2, new LayBehavior()),
                        Pair.of(3, new SealSwitchEnvironment(16, 0.01F)),
                        Pair.of(4, new SealHuntFish(1.2F, 16.0D)),
                        Pair.of(
                                5,
                                new RunOne<>(
                                        ImmutableList.of(
                                                Pair.of(new FollowTemptation(entity -> 1.25F, entity -> entity.isBaby() ? 1.0D : 2.0D), 1),
                                                Pair.of(BabyFollowAdult.create(UniformInt.of(3, 6), 1.25F), 1),
                                                Pair.of(new SealMoveToEnvironmentTarget(1.0F), 1),
                                                Pair.of(new SealLandStroll(1.0F, 60, 120, 30, 60), 1),
                                                Pair.of(new SealSwim(1.0F), 1)
                                        )
                                )
                        ),
                        Pair.of(6, new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F)),
                        Pair.of(
                                7,
                                new RunOne<>(
                                        ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                        ImmutableList.of(
                                                Pair.of(new DoNothing(30, 60), 1)
                                        )
                                )
                        )
                )
        );
    }

    private static void initPanicActivity(Brain<SealEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.PANIC,
                10,
                ImmutableList.of(
                        new AnimalPanic<>(
                                1.5F,
                                mob -> DamageTypeTags.PANIC_CAUSES
                        )
                ),
                MemoryModuleType.IS_PANICKING
        );
    }

    public static void updateActivity(SealEntity entity) {
        entity.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.PANIC, Activity.IDLE));
    }

    public static Predicate<ItemStack> getTemptations() {
        return stack -> stack.is(ModTags.ItemTags.SEAL_FOOD);
    }

    public static class LayCooldownBehavior extends Behavior<SealEntity> {
        public LayCooldownBehavior() {
            super(Map.of());
        }

        @Override
        protected boolean canStillUse(@NotNull ServerLevel level, @NotNull SealEntity entity, long gameTime) {
            return entity.isLaying();
        }

        @Override
        protected void tick(@NotNull ServerLevel level, @NotNull SealEntity entity, long gameTime) {
            if (entity.isPanicking() || entity.isInWater()) {
                entity.finishGettingUp();
            }
        }
    }

    public static class LayBehavior extends Behavior<SealEntity> {
        public LayBehavior() {
            super(Map.of(), 5 * TimeUtil.SECONDS_PER_MINUTE * 20);
        }

        @Override
        protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull SealEntity entity) {
            return entity.canStartLaying() && entity.getRandom().nextFloat() < 0.01F;
        }

        @Override
        protected void start(@NotNull ServerLevel level, @NotNull SealEntity entity, long gameTime) {
            entity.startLayingDown();
        }

        @Override
        protected boolean canStillUse(@NotNull ServerLevel level, @NotNull SealEntity entity, long gameTime) {
            return entity.isLaying() && !entity.isInWater() && !entity.isPanicking();
        }

        @Override
        protected void tick(@NotNull ServerLevel level, @NotNull SealEntity entity, long gameTime) {
            if (entity.getLayState() == SealEntity.LayState.LAYING_DOWN && entity.inStateTicks > 23) {
                entity.setLayState(SealEntity.LayState.LAY_IDLE);
            } else if (entity.getLayState() == SealEntity.LayState.LAY_IDLE) {
                if (entity.inStateTicks > 600 && entity.getRandom().nextFloat() < 0.01F) {
                    entity.startGettingUp();
                }
            } else if (entity.getLayState() == SealEntity.LayState.GETTING_UP && entity.inStateTicks > 23) {
                entity.finishGettingUp();
            }
        }

        @Override
        protected void stop(@NotNull ServerLevel level, @NotNull SealEntity entity, long gameTime) {
            if (entity.isLaying()) {
                entity.finishGettingUp();
            }
        }
    }

    public static class SealMoveToEnvironmentTarget extends Behavior<SealEntity> {
        private final float speed;

        public SealMoveToEnvironmentTarget(float speed) {
            super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
            this.speed = speed;
        }

        @Override
        protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull SealEntity seal) {
            return seal.hasEnvironmentTarget() && !seal.isAtEnvironmentTarget();
        }

        @Override
        protected void start(@NotNull ServerLevel level, @NotNull SealEntity seal, long gameTime) {
            BlockPos target = seal.getEnvironmentTarget();
            if (target != null) {
                seal.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(target, this.speed, 1));
            }
        }

        @Override
        protected boolean canStillUse(@NotNull ServerLevel level, @NotNull SealEntity seal, long gameTime) {
            return seal.hasEnvironmentTarget() && !seal.isAtEnvironmentTarget() && seal.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET);
        }

        @Override
        protected void tick(@NotNull ServerLevel level, @NotNull SealEntity seal, long gameTime) {
            if (seal.isAtEnvironmentTarget()) {
                seal.clearEnvironmentTarget();
                seal.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            }
        }

        @Override
        protected void stop(@NotNull ServerLevel level, @NotNull SealEntity seal, long gameTime) {
            seal.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        }
    }

    public static class SealLandStroll extends Behavior<SealEntity> {
        private final float speed;

        public SealLandStroll(float speed, int minRunTime, int maxRunTime, int minIdleTime, int maxIdleTime) {
            super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
            this.speed = speed;
        }

        @Override
        protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull SealEntity seal) {
            return !seal.isInWater() && seal.onGround();
        }

        @Override
        protected boolean canStillUse(@NotNull ServerLevel level, @NotNull SealEntity seal, long gameTime) {
            return !seal.isInWater() && seal.onGround();
        }

        @Override
        protected void start(@NotNull ServerLevel level, @NotNull SealEntity seal, long gameTime) {
            BlockPos pos = seal.blockPosition();
            BlockPos target = null;
            for (int i = 0; i < 10; i++) {
                BlockPos candidate = pos.offset(seal.getRandom().nextInt(10) - 5, 0, seal.getRandom().nextInt(10) - 5);
                if (level.getBlockState(candidate).isAir() && level.getBlockState(candidate.below()).isRedstoneConductor(level, candidate.below())) {
                    target = candidate;
                    break;
                }
            }
            if (target != null) {
                seal.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(target, this.speed, 1));
            }
        }
    }

    public static class SealSwim extends Behavior<SealEntity> {
        private final float speed;

        public SealSwim(float speed) {
            super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
            this.speed = speed;
        }

        @Override
        protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull SealEntity seal) {
            return seal.isInWater();
        }

        @Override
        protected void start(@NotNull ServerLevel level, @NotNull SealEntity seal, long gameTime) {
            BlockPos pos = seal.blockPosition();
            BlockPos target = null;
            for (int i = 0; i < 10; i++) {
                BlockPos candidate = pos.offset(seal.getRandom().nextInt(20) - 10, seal.getRandom().nextInt(10) - 5, seal.getRandom().nextInt(20) - 10);
                if (level.getFluidState(candidate).is(FluidTags.WATER)) {
                    target = candidate;
                    break;
                }
            }
            if (target != null) {
                seal.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(target, this.speed, 1));
            }
        }

        @Override
        protected boolean canStillUse(@NotNull ServerLevel level, @NotNull SealEntity seal, long gameTime) {
            return seal.isInWater() && seal.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET);
        }
    }

    public static class SealSwitchEnvironment extends Behavior<SealEntity> {
        private final int radius;
        private final float startChance;

        public SealSwitchEnvironment(int radius, float startChance) {
            super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
            this.radius = radius;
            this.startChance = startChance;
        }

        @Override
        protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull SealEntity seal) {
            return !seal.hasEnvironmentTarget() && seal.getRandom().nextFloat() < this.startChance;
        }

        @Override
        protected void start(@NotNull ServerLevel level, @NotNull SealEntity seal, long gameTime) {
            BlockPos target = this.findTarget(level, seal);
            if (target != null) {
                seal.setEnvironmentTarget(target);
            }
        }

        @Nullable
        private BlockPos findTarget(ServerLevel level, SealEntity seal) {
            BlockPos pos = seal.blockPosition();
            boolean inWater = seal.isInWater();
            for (int i = 0; i < 20; i++) {
                BlockPos candidate = pos.offset(seal.getRandom().nextInt(this.radius * 2) - this.radius, seal.getRandom().nextInt(10) - 5, seal.getRandom().nextInt(this.radius * 2) - this.radius);
                boolean candidateInWater = level.getFluidState(candidate).is(FluidTags.WATER);
                if (inWater != candidateInWater) {
                    if (candidateInWater || level.getBlockState(candidate.below()).isRedstoneConductor(level, candidate.below())) {
                        return candidate;
                    }
                }
            }
            return null;
        }

        @Override
        protected boolean canStillUse(@NotNull ServerLevel level, @NotNull SealEntity seal, long gameTime) {
            return false;
        }
    }

    public static class SealHuntFish extends Behavior<SealEntity> {
        private final float speed;
        private final double maxDistance;

        public SealHuntFish(float speed, double maxDistance) {
            super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
            this.speed = speed;
            this.maxDistance = maxDistance;
        }

        @Override
        protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull SealEntity seal) {
            if (!seal.isInWater() || seal.isBaby()) return false;
            return seal.getRandom().nextFloat() < 0.05F;
        }

        @Override
        protected void start(@NotNull ServerLevel level, @NotNull SealEntity seal, long gameTime) {
            LivingEntity target = this.findTarget(level, seal);
            if (target != null) {
                seal.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(target, false), this.speed, 1));
            }
        }

        @Nullable
        private LivingEntity findTarget(ServerLevel level, SealEntity seal) {
            var list = level.getEntitiesOfClass(LivingEntity.class, seal.getBoundingBox().inflate(this.maxDistance), entity -> entity instanceof WaterAnimal);
            if (list.isEmpty()) return null;
            return list.get(seal.getRandom().nextInt(list.size()));
        }

        @Override
        protected boolean canStillUse(@NotNull ServerLevel level, @NotNull SealEntity seal, long gameTime) {
            return seal.isInWater() && seal.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET);
        }
    }
}
