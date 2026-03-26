package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import com.otterly76.ott.entity.ai.navigation.SmoothSwimmingMoveControlButNotBad;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GiantSoftshellTurtleEntity extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation SWIM_ANIMATION = RawAnimation.begin().thenLoop("Swim");
    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("Walk");
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("Idle");

    public GiantSoftshellTurtleEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.moveControl = new SmoothSwimmingMoveControlButNotBad(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new AmphibiousPathNavigation(this, level);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob other) {
        return ModEntities.GIANT_SOFTSHELL_TURTLE.get().create(level);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event -> {
            if (this.isInWater()) {
                return event.setAndContinue(SWIM_ANIMATION);
            }
            if (event.isMoving()) {
                return event.setAndContinue(WALK_ANIMATION);
            }
            return event.setAndContinue(IDLE_ANIMATION);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}